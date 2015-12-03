package se.travappar.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "StartPosition")
public class StartPosition implements CommonEntity {
    Long id;
    String sex;
    String horse;
    String driver;
    String driverShortName;
    String trainer;
    String trainerShortName;
    String record;
    Integer startNumber;
    Integer age;
    Long totalWinnings;
    Race race;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "sex")
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Column(name = "horse")
    public String getHorse() {
        return horse;
    }

    public void setHorse(String horse) {
        this.horse = horse;
    }

    @Column(name = "driver")
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Column(name = "driverShortName")
    public String getDriverShortName() {
        return driverShortName;
    }

    public void setDriverShortName(String driverShortName) {
        this.driverShortName = driverShortName;
    }

    @Column(name = "trainer")
    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    @Column(name = "trainerShortName")
    public String getTrainerShortName() {
        return trainerShortName;
    }

    public void setTrainerShortName(String trainerShortName) {
        this.trainerShortName = trainerShortName;
    }

    @Column(name = "record")
    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Column(name = "startNumber")
    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }

    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name = "totalWinnings")
    public Long getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(Long totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonBackReference
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
