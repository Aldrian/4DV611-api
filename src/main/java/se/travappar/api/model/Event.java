package se.travappar.api.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "Event")
public class Event implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    Long id;
    @Column(name = "date")
    Date date;
    @Column(name = "eventName")
    String eventName;
    @Column(name = "highlight")
    String highlight;
    @Column(name = "homeTeam")
    String homeTeam;
    @Column(name = "offerImage")
    String offerImage;
    @Column(name = "offer")
    String offer;
    @Column (name = "track")
    Track track;
    @Column(name = "trackList")
    String trackList;

    public Event() {

    }
        @Id
        @GeneratedValue(generator="increment")
        @GenericGenerator(name="increment", strategy = "increment")
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

    @Column(name = "offer")
    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Column(name = "highlight")
    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    @Column(name = "homeTeam")
    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }
}
