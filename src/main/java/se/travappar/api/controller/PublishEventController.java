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
import se.travappar.api.utils.publish.PublishEventHelper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping(value = "/publish")
public class PublishEventController {

    @Autowired
    ImageHelper imageHelper;
    @Autowired
    PublishEventHelper publishEventHelper;
    @Autowired
    EventDAO eventDAO;
    private static final Logger logger = LogManager.getLogger(PublishEventController.class);

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    ResponseEntity<Event> updateEvent(HttpServletRequest request, @RequestBody Event event) {
        logger.info("Update event executed on / with event with id=" + event.getId());
        if (event.getOfferImageSource() != null) {
            try {
                String offerImageUrl = imageHelper.saveOfferImage(event.getOfferImageSource(), event.getId().toString());
                event.setOfferImage(offerImageUrl);
            } catch (Exception e) {
                logger.error("Error while saving image", e);
                if (e instanceof RuntimeException) {
                    return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<Event>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if (event.getPublished()) {
            try {
                // TODO publish
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<Event>(eventDAO.update(event), HttpStatus.OK);
    }
}
