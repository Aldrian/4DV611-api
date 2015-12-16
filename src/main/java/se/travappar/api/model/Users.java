package se.travappar.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Users")
public class Users implements CommonEntity {

    String email;
    String euid;
    String leid;
    String username;
    String password;
    String deviceId;
    String onesignalId;
    String role;
    Long trackId;
    Boolean enabled;

    public Users() {

    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @Column(name = "device_id")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String device_id) {
        this.deviceId = device_id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "track_id")
    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    @Column(name = "enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEuid() {
        return euid;
    }

    public void setEuid(String euid) {
        this.euid = euid;
    }

    public String getLeid() {
        return leid;
    }

    public String getOnesignalId() {
        return onesignalId;
    }

    public void setOnesignalId(String onesignalId) {
        this.onesignalId = onesignalId;
    }

    public void setLeid(String leid) {
        this.leid = leid;
    }
}