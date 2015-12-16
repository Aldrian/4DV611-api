package se.travappar.api.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Subscription")
public class Subscription implements CommonEntity {

    String deviceId;
    Long trackId;

    public Subscription() {
    }

    @Id
    @Column(name = "device_id", nullable = false)
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Id
    @Column(name = "track_id", nullable = false)
    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscription that = (Subscription) o;

        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        if (trackId != null ? !trackId.equals(that.trackId) : that.trackId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = deviceId != null ? deviceId.hashCode() : 0;
        result = 31 * result + (trackId != null ? trackId.hashCode() : 0);
        return result;
    }
}
