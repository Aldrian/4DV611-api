package se.travappar.api.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalStartList implements Serializable {
    RaceInfoArray raceInfoArray;

    public ExternalStartList() {
    }

    public RaceInfoArray getRaceInfoArray() {
        return raceInfoArray;
    }

    public void setRaceInfoArray(RaceInfoArray raceInfoArray) {
        this.raceInfoArray = raceInfoArray;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RaceInfoArray implements Serializable {

        RaceInfo raceInfo;

        public RaceInfoArray() {
        }

        public RaceInfo getRaceInfo() {
            return raceInfo;
        }

        public void setRaceInfo(RaceInfo raceInfo) {
            this.raceInfo = raceInfo;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RaceInfo implements Serializable {

        List<StartList> raceInfo;

        public RaceInfo() {
        }

        public List<StartList> getRaceInfo() {
            return raceInfo;
        }

        public void setRaceInfo(List<StartList> raceInfo) {
            this.raceInfo = raceInfo;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StartList implements Serializable {
        Boolean latestForeShoes;
        String sex;
        Boolean foreShoes;
        String horse;
        Boolean hindShoes;
        String driverShortName;
        Long race;
        String trainer;
        Long id;
        String record;
        String trainerShortName;
        Boolean latestHindShoes;
        Integer startNr;
        Integer age;
        String driver;
        Long totalWinnings;

        public StartList() {
        }

        public Boolean getLatestForeShoes() {
            return latestForeShoes;
        }

        public void setLatestForeShoes(Boolean latestForeShoes) {
            this.latestForeShoes = latestForeShoes;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Boolean getForeShoes() {
            return foreShoes;
        }

        public void setForeShoes(Boolean foreShoes) {
            this.foreShoes = foreShoes;
        }

        public String getHorse() {
            return horse;
        }

        public void setHorse(String horse) {
            this.horse = horse;
        }

        public Boolean getHindShoes() {
            return hindShoes;
        }

        public void setHindShoes(Boolean hindShoes) {
            this.hindShoes = hindShoes;
        }

        public String getDriverShortName() {
            return driverShortName;
        }

        public void setDriverShortName(String driverShortName) {
            this.driverShortName = driverShortName;
        }

        public Long getRace() {
            return race;
        }

        public void setRace(Long race) {
            this.race = race;
        }

        public String getTrainer() {
            return trainer;
        }

        public void setTrainer(String trainer) {
            this.trainer = trainer;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getRecord() {
            return record;
        }

        public void setRecord(String record) {
            this.record = record;
        }

        public String getTrainerShortName() {
            return trainerShortName;
        }

        public void setTrainerShortName(String trainerShortName) {
            this.trainerShortName = trainerShortName;
        }

        public Boolean getLatestHindShoes() {
            return latestHindShoes;
        }

        public void setLatestHindShoes(Boolean latestHindShoes) {
            this.latestHindShoes = latestHindShoes;
        }

        public Integer getStartNr() {
            return startNr;
        }

        public void setStartNr(Integer startNr) {
            this.startNr = startNr;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public Long getTotalWinnings() {
            return totalWinnings;
        }

        public void setTotalWinnings(Long totalWinnings) {
            this.totalWinnings = totalWinnings;
        }
    }
}
