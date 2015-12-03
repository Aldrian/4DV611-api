package se.travappar.api.model.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalEvent implements Serializable {

    TrackInfoArray trackInfoArray;

    public ExternalEvent() {
    }

    public TrackInfoArray getTrackInfoArray() {
        return trackInfoArray;
    }

    public void setTrackInfoArray(TrackInfoArray trackInfoArray) {
        this.trackInfoArray = trackInfoArray;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackInfoArray implements Serializable {

        public TrackInfoArray() {

        }

        Tracks tracks;

        public Tracks getTracks() {
            return tracks;
        }

        public void setTracks(Tracks tracks) {
            this.tracks = tracks;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tracks implements Serializable {

        public Tracks() {

        }

        @JsonProperty("trackInfo")
        List<RaceEvent> events;

        public List<RaceEvent> getEvents() {
            return events;
        }

        public void setEvents(List<RaceEvent> events) {
            this.events = events;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RaceEvent implements Serializable {

        public RaceEvent() {

        }

        Long id;
        Long trackId;
        Boolean hasresult;
        String track;
        Date date;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS Z")
        Date firstracetime;
        Integer nbrOfRaces;
        Integer raceCount;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getTrackId() {
            return trackId;
        }

        public void setTrackId(Long trackId) {
            this.trackId = trackId;
        }

        public Boolean getHasresult() {
            return hasresult;
        }

        public void setHasresult(Boolean hasresult) {
            this.hasresult = hasresult;
        }

        public String getTrack() {
            return track;
        }

        public void setTrack(String track) {
            this.track = track;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Date getFirstracetime() {
            return firstracetime;
        }

        public void setFirstracetime(Date firstracetime) {
            this.firstracetime = firstracetime;
        }

        public Integer getNbrOfRaces() {
            return nbrOfRaces;
        }

        public void setNbrOfRaces(Integer nbrOfRaces) {
            this.nbrOfRaces = nbrOfRaces;
        }

        public Integer getRaceCount() {
            return raceCount;
        }

        public void setRaceCount(Integer raceCount) {
            this.raceCount = raceCount;
        }
    }
}
