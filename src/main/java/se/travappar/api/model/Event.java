package se.travappar.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@DynamicUpdate
@Table(name = "Event")
public class Event implements CommonEntity {

    Long id;
    String name;
    Date date;
    String highlight;
    String homeTeam;
    String offerImage;
    String offerImageSource;
    String offer;
    Track track;
    List<Race> raceList;
    Boolean published = false;

    public Event() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "offerImage")
    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }

    @Column(name = "offer", columnDefinition="text", length = 500)
    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Column(name = "highlight", columnDefinition="text", length = 1000)
    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    @Column(name = "published", nullable = false, columnDefinition = "boolean default false")
    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    @Column(name = "homeTeam", columnDefinition="text", length = 500)
    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String eventName) {
        this.name = eventName;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "event")
    @Cascade(CascadeType.ALL)
    @JsonManagedReference
    public List<Race> getRaceList() {
        return raceList;
    }

    public void setRaceList(List<Race> raceList) {
        this.raceList = raceList;
    }

    @Transient
    public String getOfferImageSource() {
        return offerImageSource;
    }

    public void setOfferImageSource(String offerImageSource) {
        this.offerImageSource = offerImageSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (!date.equals(event.date)) return false;
        if (!id.equals(event.id)) return false;
        if (raceList != null ? !raceList.equals(event.raceList) : event.raceList != null) return false;
        if (!track.equals(event.track)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + track.hashCode();
        result = 31 * result + (raceList != null ? raceList.hashCode() : 0);
        return result;
    }
}
