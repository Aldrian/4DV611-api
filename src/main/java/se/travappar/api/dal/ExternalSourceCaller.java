package se.travappar.api.dal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.dal.impl.RaceDAO;
import se.travappar.api.dal.impl.StartPositionDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.Race;
import se.travappar.api.model.StartPosition;
import se.travappar.api.model.Track;
import se.travappar.api.model.external.ExternalEvent;
import se.travappar.api.model.external.ExternalRace;
import se.travappar.api.model.external.ExternalStartList;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExternalSourceCaller {

    @Autowired
    StartPositionDAO startPositionDAO;
    @Autowired
    EventDAO eventDAO;
    @Autowired
    RaceDAO raceDAO;
    @Autowired
    TrackDAO trackDAO;

    Long norwayTrackId = 77L;

    OkHttpClient client = new OkHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    Set<Track> trackSet = new HashSet<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<Event> requestEventList() {
        try {
            String body = runQuery("http://178.62.191.16:8080/collector/collectorserver?ACTION=TRMEDIA_START_V2&JSON=1");
            ExternalEvent externalEventHolder = mapper.readValue(body, ExternalEvent.class);
            List<Event> resultList = new ArrayList<>();
            if (externalEventHolder.getTrackInfoArray() != null && externalEventHolder.getTrackInfoArray().getTracks() != null && externalEventHolder.getTrackInfoArray().getTracks().getEvents() != null) {
                List<ExternalEvent.RaceEvent> events = externalEventHolder.getTrackInfoArray().getTracks().getEvents();
                for (ExternalEvent.RaceEvent externalEvent : events) {
                    Track track = new Track();
                    track.setId(externalEvent.getTrackId());
                    if(norwayTrackId.equals(externalEvent.getTrackId())) {
                        track.setName("Norway");
                    } else {
                        track.setName(externalEvent.getTrack());
                    }
                    trackSet.add(track);

                    Event event = new Event();
                    event.setId(externalEvent.getId());
//                    event.setName(externalEvent.getTrack()); // TODO
                    event.setDate(externalEvent.getFirstracetime());
                    event.setTrack(track);
                    event.setRaceList(getRaceList(track, event));
                    resultList.add(event);
                }
            }
//            trackDAO.saveList(trackSet);
//            eventDAO.saveList(resultList);
            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    private List<Race> getRaceList(Track track, Event event) throws IOException {
        List<Race> raceList = new ArrayList<>();
        String body = runQuery("http://178.62.191.16:8080/collector/collectorserver?ACTION=GETRACES&DATE=" + dateFormat.format(event.getDate()) + "&TRACKID=" + track.getId() + "&JSON=1");
        ExternalRace externalRaceHolder = mapper.readValue(body, ExternalRace.class);
        if (externalRaceHolder != null && externalRaceHolder.getRaces() != null) {
            for (ExternalRace.Race externalRace : externalRaceHolder.getRaces()) {
                Race race = new Race();
                race.setId(externalRace.getRace());
                race.setHorseCount(externalRace.getHorses());
                race.setInformation(externalRace.getInformation());
                race.setShortInformation(externalRace.getInformationShort());
                race.setNumber(externalRace.getRaceNbr());
                race.setReservOrder(externalRace.getReservOrder());
                race.setStartTime(externalRace.getStartTime());
                race.setEvent(event);
                race.setStartList(getStartList(track, event, race));
                raceList.add(race);
            }
        }
//        raceDAO.saveList(raceList);
        return raceList;
    }

    private List<StartPosition> getStartList(Track track, Event event, Race race) throws IOException {
        List<StartPosition> startPositionList = new ArrayList<>();
        String body = runQuery("http://178.62.191.16:8080//collector/collectorserver?ACTION=GETRACEINFO&DATE=" + dateFormat.format(event.getDate()) + "&TRACKID=" + track.getId() + "&RACENBR=" + race.getNumber() + "&JSON=1");
        ExternalStartList externalStartListHolder = mapper.readValue(body, ExternalStartList.class);
        if (externalStartListHolder != null
                && externalStartListHolder.getRaceInfoArray() != null
                && externalStartListHolder.getRaceInfoArray().getRaceInfo() != null
                && externalStartListHolder.getRaceInfoArray().getRaceInfo().getRaceInfo() != null) {
            for (ExternalStartList.StartList externalStartList : externalStartListHolder.getRaceInfoArray().getRaceInfo().getRaceInfo()) {
                StartPosition startPosition = new StartPosition();
                startPosition.setId(externalStartList.getId());
                startPosition.setAge(externalStartList.getAge());
                startPosition.setStartNumber(externalStartList.getStartNr());
                startPosition.setDriver(externalStartList.getDriver());
                startPosition.setDriverShortName(externalStartList.getDriverShortName());
                startPosition.setTrainer(externalStartList.getTrainer());
                startPosition.setTrainerShortName(externalStartList.getTrainerShortName());
                startPosition.setHorse(externalStartList.getHorse());
                startPosition.setRecord(externalStartList.getRecord());
                startPosition.setSex(externalStartList.getSex());
                startPosition.setTotalWinnings(externalStartList.getTotalWinnings());
                startPosition.setRace(race);
                startPositionList.add(startPosition);
            }
        }
//        startPositionDAO.saveList(startPositionList);
        return startPositionList;
    }

    private String runQuery(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Set<Track> getTrackSet() {
        return trackSet;
    }

    public void setTrackSet(Set<Track> trackSet) {
        this.trackSet = trackSet;
    }

    @PostConstruct
    public void loadDataOnStartup() {
        /*List<Event> eventList = requestEventList();
        Set<Track> trackSet = getTrackSet();
        trackDAO.saveList(trackSet);
        eventDAO.saveList(eventList);*/
    }
}
