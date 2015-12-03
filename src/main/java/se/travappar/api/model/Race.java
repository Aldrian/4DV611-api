package se.travappar.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Race")
public class Race implements CommonEntity {
    Long id;
    Date startTime;
    String information;
    Integer number;
    Integer horseCount;
    String reservOrder;
    String shortInformation;
    Event event;
    List<StartPosition> startList;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "startTime")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "information", columnDefinition="text", length = 1000)
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Column(name = "number")
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Column(name = "horseCount")
    public Integer getHorseCount() {
        return horseCount;
    }

    public void setHorseCount(Integer horseCount) {
        this.horseCount = horseCount;
    }

    @Column(name = "reservOrder")
    public String getReservOrder() {
        return reservOrder;
    }

    public void setReservOrder(String reservOrder) {
        this.reservOrder = reservOrder;
    }

    @Column(name = "shortInformation")
    public String getShortInformation() {
        return shortInformation;
    }

    public void setShortInformation(String shortInformation) {
        this.shortInformation = shortInformation;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @JsonBackReference
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "race")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonManagedReference
    public List<StartPosition> getStartList() {
        return startList;
    }

    public void setStartList(List<StartPosition> startList) {
        this.startList = startList;
    }
}
