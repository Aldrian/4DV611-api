package se.travappar.api.utils.publish.onesignal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class NotificationRequest implements Serializable {
    String app_id;
    HashMap<String, String> contents;
    HashMap<String, String> data;
    List<String> included_segments;

    public NotificationRequest() {
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public HashMap<String, String> getContents() {
        return contents;
    }

    public void setContents(HashMap<String, String> contents) {
        this.contents = contents;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public List<String> getIncluded_segments() {
        return included_segments;
    }

    public void setIncluded_segments(List<String> included_segments) {
        this.included_segments = included_segments;
    }
}
