package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.SubscriptionDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Subscription;
import se.travappar.api.model.Track;
import se.travappar.api.model.Users;
import se.travappar.api.model.dto.SubscriptionDTO;
import se.travappar.api.model.filter.Filtering;
import se.travappar.api.utils.converter.SubscriptionConverter;
import se.travappar.api.utils.publish.OneSignalHelper;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/subscriptions")
public class SubscriptionController {

    @Autowired
    SubscriptionDAO subscriptionDAO;
    @Autowired
    OneSignalHelper oneSignalHelper;
    @Autowired
    TrackDAO trackDAO;
    @Autowired
    UserDAO userDAO;
    SubscriptionConverter subscriptionConverter = new SubscriptionConverter();
    private static final Logger logger = LogManager.getLogger(SubscriptionController.class);

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
    public
    @ResponseBody
    SubscriptionDTO getSubscription(@PathVariable String deviceId) {
        logger.info("Getting subscription executed on / with id=" + deviceId);
        Filtering filtering = new Filtering("device_id", "=", "'" + deviceId + "'");
        List<Subscription> subscriptionList = subscriptionDAO.getList(Arrays.asList(filtering));
        return subscriptionConverter.convertToDTO(subscriptionList);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    SubscriptionDTO createSubscription(@RequestBody SubscriptionDTO subscription) {
        logger.info("Creating subscription executed on /");
        Filtering filtering = new Filtering("device_id", "=", "'" + subscription.getDeviceId() + "'");
        subscriptionDAO.delete(Arrays.asList(filtering));
        subscriptionDAO.saveList(subscriptionConverter.convertToEntity(subscription));
        updateDeviceFlags(subscription);
        return subscription;
    }

    private void updateDeviceFlags(SubscriptionDTO subscription) {
        List<Track> trackList = trackDAO.getList(new ArrayList<>());
        HashMap<String, String> tags = new HashMap<>();
        for(Track track : trackList) {
            if(subscription.getTrackIdList().contains(track.getId())) {
                tags.put(track.getName(), "true");
            } else {
                tags.put(track.getName(), "");
            }
        }
        Users user = userDAO.get(subscription.getDeviceId());
        try {
            oneSignalHelper.updateDeviceFlags(user, tags);
        } catch (IOException e) {
            logger.info("Error while updating device flags in oneSignal.", e);
        }
    }
}
