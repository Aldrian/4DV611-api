package se.travappar.api.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Subscription")
public class Subscription implements Serializable {

    @Column(name = "user", nullable = false, unique = true)
    User user;
    @Column(name = "track", nullable = false, unique = true)
    Track track;

    public Subscription() {
    }

    @Id
    @GeneratedValue
    @ManyToOne(fetch = FetchType.LAZY)
    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    @Id
    @GeneratedValue
    @ManyToOne(fetch = FetchType.LAZY)
    public Track getTrack() {return track;}

    public void setTrack(Track track) {
        this.track = track;
    }
}
