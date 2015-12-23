package se.travappar.api.utils.publish;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import se.travappar.api.dal.impl.SubscriptionDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.Users;
import se.travappar.api.utils.publish.onesignal.DeviceRequest;
import se.travappar.api.utils.publish.onesignal.NotificationRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class OneSignalHelper {

    private static final String appId = "4c3907a3-0097-46c1-8059-21da20736620";
    private static final String authToken = "NzgwYWU4NzAtZjQwMS00NWM4LTk4NjAtYjcwY2ZkYmVjMjRh";
    private final String NOTIFICATION_URL = "https://onesignal.com/api/v1/notifications";
    private final String PLAYER_URL = "https://onesignal.com/api/v1/players/";

    OkHttpClient client = new OkHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    SubscriptionDAO subscriptionDAO;

    public void sendEventNotification(Event event) throws IOException {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setApp_id(appId);
        notificationRequest.setIncluded_segments(Arrays.asList(event.getTrack().getName()));
        HashMap<String, String> data = new HashMap<>();
        data.put("eventId", event.getId().toString());
        notificationRequest.setData(data);
        HashMap<String, String> contents = new HashMap<>();
        contents.put("en", "New event on " + event.getTrack().getName() + " has been published. Tap to open.");
        notificationRequest.setContents(contents);
        runQuery(NOTIFICATION_URL, RequestMethod.POST.toString(), mapper.writeValueAsString(notificationRequest));
    }

    public void updateDeviceFlags(Users users, HashMap<String, String> tags) throws IOException {
        if (users.getOneSignalId() != null) {
            DeviceRequest deviceRequest = new DeviceRequest();
            deviceRequest.setApp_id(appId);
            deviceRequest.setId(users.getOneSignalId());
            tags.put("All", "true");
            tags.put("Free User", "true");
            deviceRequest.setTags(tags);
            runQuery(PLAYER_URL + users.getOneSignalId(), RequestMethod.PUT.toString(), mapper.writeValueAsString(deviceRequest));
        }
    }

    private String runQuery(String url, String method, String body) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url);
        builder.addHeader("Content-Type", "application/json; charset=UTF-8");
        builder.addHeader("Authorization", "Basic " + authToken);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), body);
        builder.method(method, requestBody);
        Request request = builder.build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
