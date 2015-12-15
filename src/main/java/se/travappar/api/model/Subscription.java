package se.travappar.api.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Subscription")
public class Subscription implements CommonEntity {

    Users user;
    List<Track> trackList;

    public Subscription() {
    }

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id", nullable = false)
    public Users getUser() {
        return user;
    }

    public void setUser(Users users) {
        this.user = users;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }
}
