package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.model.Event;
import se.travappar.api.utils.ImageHelper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    EventDAO eventDAO;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ImageHelper imageHelper;

    private static final Logger logger = LogManager.getLogger(EventController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> getEventList() {
        logger.info("Getting event list Executed on /");
        return eventDAO.getList();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> getEventListRoot() {
        logger.info("Getting event list Executed on empty mapping");
        return getEventList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Event getEvent(@PathVariable long id) {
        logger.info("Getting event executed on / with id=" + id);
        return eventDAO.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteEvent(@PathVariable long id) {
        logger.info("Delete event executed on / with id=" + id);
        Event event = eventDAO.get(id);
        eventDAO.delete(event);
        imageHelper.removeOfferImage(event.getOfferImage());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Event createEvent(@RequestBody Event event) {
        logger.info("Creating event executed on /");
        return eventDAO.create(event);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    ResponseEntity<Event> updateEvent(HttpServletRequest request, @RequestBody Event event) {
        logger.info("Update event executed on / with event with id=" + event.getId());
        try {
            String offerImageUrl = imageHelper.saveOfferImage(event.getOfferImageSource(), event.getId().toString());
            event.setOfferImage(offerImageUrl);
        } catch (Exception e) {
            logger.error("Error while saving image", e);
            if(e instanceof RuntimeException) {
                return new ResponseEntity<Event>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Event>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Event>(eventDAO.update(event), HttpStatus.OK);
    }
}
