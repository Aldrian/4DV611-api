package se.travappar.api.model.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalRace implements Serializable {

    List<Race> races;

    public ExternalRace() {
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Race implements Serializable {
        Boolean vpresult;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS Z")
        Date startTime;
        String information;
        Integer raceNbr;
        Integer horses;
        String reservOrder;
        String informationShort;
        Long race;

        public Boolean getVpresult() {
            return vpresult;
        }

        public void setVpresult(Boolean vpresult) {
            this.vpresult = vpresult;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public String getInformation() {
            return information;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public Integer getRaceNbr() {
            return raceNbr;
        }

        public void setRaceNbr(Integer raceNbr) {
            this.raceNbr = raceNbr;
        }

        public Integer getHorses() {
            return horses;
        }

        public void setHorses(Integer horses) {
            this.horses = horses;
        }

        public String getReservOrder() {
            return reservOrder;
        }

        public void setReservOrder(String reservOrder) {
            this.reservOrder = reservOrder;
        }

        public String getInformationShort() {
            return informationShort;
        }

        public void setInformationShort(String informationShort) {
            this.informationShort = informationShort;
        }

        public Long getRace() {
            return race;
        }

        public void setRace(Long race) {
            this.race = race;
        }
    }
}
