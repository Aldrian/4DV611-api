package se.travappar.api.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Subscription")
public class Subscription implements Serializable {

    User user;
    Track track;

    public Subscription() {
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
