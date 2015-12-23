package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.model.Event;
import se.travappar.api.utils.ImageHelper;
import se.travappar.api.utils.publish.OneSignalHelper;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping(value = "/publish")
public class PublishEventController {

    @Autowired
    ImageHelper imageHelper;
    @Autowired
    OneSignalHelper oneSignalHelper;
    @Autowired
    EventDAO eventDAO;
    private static final Logger logger = LogManager.getLogger(PublishEventController.class);

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    ResponseEntity updateEvent(@RequestBody Event event) {
        logger.info("Update event executed on / with event with id=" + event.getId());
        if (event.getOfferImageSource() != null) {
            try {
                String offerImageUrl = imageHelper.saveOfferImage(event.getOfferImageSource(), event.getId().toString());
                event.setOfferImage(offerImageUrl);
            } catch (Exception e) {
                logger.error("Error while saving image", e);
                if (e instanceof RuntimeException) {
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (event.getPublished()) {
            try {
                oneSignalHelper.sendEventNotification(event);
            } catch (Exception e) {
                logger.error("Error while sending notifications", e);
                return new ResponseEntity<>("Error while sending notifications: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<Event>(eventDAO.update(event), HttpStatus.OK);
    }
}
