package se.travappar.api.utils.publish.onesignal;

import java.io.Serializable;
import java.util.HashMap;

public class DeviceRequest implements Serializable {
    String app_id;
    String id;
    HashMap<String, String> tags;

    public DeviceRequest() {
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
