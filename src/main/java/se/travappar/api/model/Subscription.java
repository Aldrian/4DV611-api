package se.travappar.api.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Subscription")
public class Subscription implements CommonEntity {

    Users user;
    Track track;

    public Subscription() {
    }

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    public Users getUser() {
        return user;
    }

    public void setUser(Users users) {
        this.user = users;
    }

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
