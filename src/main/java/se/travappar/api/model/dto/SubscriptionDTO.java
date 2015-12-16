package se.travappar.api.model.dto;

import java.util.List;

public class SubscriptionDTO implements CommonDTO {
    String deviceId;
    List<Long> trackIdList;

    public SubscriptionDTO() {
    }

    public SubscriptionDTO(String deviceId, List<Long> trackIdList) {
        this.deviceId = deviceId;
        this.trackIdList = trackIdList;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<Long> getTrackIdList() {
        return trackIdList;
    }

    public void setTrackIdList(List<Long> trackIdList) {
        this.trackIdList = trackIdList;
    }
}
