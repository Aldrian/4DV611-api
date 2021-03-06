package se.travappar.api.dal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import se.travappar.api.dal.impl.*;
import se.travappar.api.model.*;
import se.travappar.api.model.enums.UserRole;
import se.travappar.api.model.external.ExternalEvent;
import se.travappar.api.model.external.ExternalRace;
import se.travappar.api.model.external.ExternalStartList;
import se.travappar.api.utils.publish.MailChimpHelper;

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
    @Autowired
    UserDAO userDAO;
    @Autowired
    MailChimpHelper mailChimpHelper;

    Long norwayTrackId = 77L;
    Long denmarkTrackId = 54L;
    Long requestCount = 0L;

    OkHttpClient client = new OkHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    Set<Track> trackSet = new HashSet<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final String EXTERNAL_SERVER_HOST = "http://178.62.191.16:8080/";
    private static final Logger logger = LogManager.getLogger(ExternalSourceCaller.class);

    public List<Event> requestEventList() {
        requestCount = 0L;
        try {
            logger.info("Start fetching external event data.");
            String body = runQuery(EXTERNAL_SERVER_HOST + "collector/collectorserver?ACTION=TRMEDIA_START_V2&JSON=1");
            ExternalEvent externalEventHolder = mapper.readValue(body, ExternalEvent.class);
            List<Event> resultList = new ArrayList<>();
            if (externalEventHolder.getTrackInfoArray() != null && externalEventHolder.getTrackInfoArray().getTracks() != null && externalEventHolder.getTrackInfoArray().getTracks().getEvents() != null) {
                List<ExternalEvent.RaceEvent> events = externalEventHolder.getTrackInfoArray().getTracks().getEvents();
                for (ExternalEvent.RaceEvent externalEvent : events) {
                    Track track = new Track();
                    track.setId(externalEvent.getTrackId());
                    if (norwayTrackId.equals(externalEvent.getTrackId())) {
                        track.setName("Norway");
                    } else if (denmarkTrackId.equals(externalEvent.getTrackId())) {
                        track.setName("Denmark");
                    } else {
                        track.setName(externalEvent.getTrack());
                    }
                    trackSet.add(track);

                    Event event = new Event();
                    event.setId(externalEvent.getId());
                    event.setDate(externalEvent.getFirstracetime());
                    event.setTrack(track);
                    event.setRaceList(getRaceList(track, event));
                    resultList.add(event);
                }
            }
            logger.info("Finish fetching external event data. Executed " + requestCount + " requests to external source.");
            return resultList;
        } catch (Exception e) {
            logger.error("Error during fetching external event data.", e);
            return null;
        }
    }

    private List<Race> getRaceList(Track track, Event event) throws IOException {
        logger.info("Start fetching external race data for event: " + event.getId());
        List<Race> raceList = new ArrayList<>();
        String body = runQuery(EXTERNAL_SERVER_HOST + "collector/collectorserver?ACTION=GETRACES&DATE=" + dateFormat.format(event.getDate()) + "&TRACKID=" + track.getId() + "&JSON=1");
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
        logger.info("Finish fetching external race data for event: " + event.getId());
        return raceList;
    }

    private List<StartPosition> getStartList(Track track, Event event, Race race) throws IOException {
        logger.info("Start fetching external StartPosition data for Race: " + race.getId());
        List<StartPosition> startPositionList = new ArrayList<>();
        String body = runQuery(EXTERNAL_SERVER_HOST + "/collector/collectorserver?ACTION=GETRACEINFO&DATE=" + dateFormat.format(event.getDate()) + "&TRACKID=" + track.getId() + "&RACENBR=" + race.getNumber() + "&JSON=1");
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
        logger.info("Finish fetching external StartPosition data for Race: " + race.getId());
        return startPositionList;
    }

    private String runQuery(String url) throws IOException {
        requestCount = requestCount + 1;
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Set<Track> getTrackSet() {
        return trackSet;
    }

    @PostConstruct
    public void loadDataOnStartup() {
        /*List<Event> eventList = requestEventList();
        Set<Track> trackSet = getTrackSet();
        trackDAO.mergeList(trackSet);
        eventDAO.mergeList(eventList);*/
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void fetchAndSaveEvents() {
        logger.info("Start scheduled fetching external data.");
        List<Event> eventList = requestEventList();
        List<Track> storedTrackList = trackDAO.getList(new ArrayList<>());
        List<Track> newTrackList = new ArrayList<>();
        for (Track track : trackSet) {
            if (!storedTrackList.contains(track)) {
                newTrackList.add(track);
            }
        }
        trackDAO.saveList(newTrackList);
        for (Event event : eventList) {
            Event storedEvent = eventDAO.get(event.getId());
            if (storedEvent != null) {
                event.setOfferImage(storedEvent.getOfferImage());
                event.setOffer(storedEvent.getOffer());
                event.setHomeTeam(storedEvent.getHomeTeam());
                event.setName(storedEvent.getName());
                event.setHighlight(storedEvent.getHighlight());
                event.setPublished(storedEvent.getPublished());
                if(storedTrackList.contains(event.getTrack())) {
                    event.setTrack(storedTrackList.get(storedTrackList.indexOf(event.getTrack())));
                }
            }
        }
        eventDAO.mergeList(eventList);
        List<Users> adminList = new ArrayList<>();
        for (Track track : trackSet) {
            Users users = new Users();
            users.setRole(UserRole.ROLE_ADMIN.getCode());
            users.setUsername(track.getName().toLowerCase().replace(" ", ""));
            users.setTrackId(track.getId());
            users.setDeviceId(track.getId().toString());
            users.setEnabled(false);
            if (userDAO.get(track.getId().toString()) == null) {
                adminList.add(users);
            }
        }
        userDAO.mergeList(adminList);
        logger.info("Finish scheduled fetching external data.");
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void refreshMailChimpData() {
        logger.info("Start refreshMailChimpData.");
        try {
            List<Track> trackList = trackDAO.getList(new ArrayList<>());
            mailChimpHelper.refreshAndPairTrackSegments(trackList);
            trackDAO.mergeList(trackList);
            mailChimpHelper.subscribeUsersToSegments();
        } catch (Exception e) {
            logger.info("Error refreshMailChimpData. ", e);
            return;
        }
        logger.info("Finish refreshMailChimpData.");
    }
}
