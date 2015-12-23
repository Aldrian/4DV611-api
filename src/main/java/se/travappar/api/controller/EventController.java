package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.dal.impl.SubscriptionDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.Subscription;
import se.travappar.api.model.filter.Filtering;
import se.travappar.api.utils.ImageHelper;
import se.travappar.api.utils.security.CurrentUser;

import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    @Autowired
    EventDAO eventDAO;
    @Autowired
    SubscriptionDAO subscriptionDAO;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ImageHelper imageHelper;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger logger = LogManager.getLogger(EventController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> getEventList(Principal principal, @RequestParam(value = "fromDate") Optional<String> fromDate) {
        logger.info("Getting event list Executed on /");
        List<Filtering> filteringList = new ArrayList<>();
        if (fromDate.isPresent()) {
            try {
                dateFormat.parse(fromDate.get());
                Filtering dateFilter = new Filtering("date", ">=", "'" + fromDate.get() + "'", "AND");
                filteringList.add(dateFilter);
            } catch (ParseException e) {
                logger.info("Getting event list Executed on /. Bad date format.", e);
            }
        }
        CurrentUser currentUser = (CurrentUser) ((Authentication) principal).getPrincipal();
        switch (currentUser.getRole()) {
            case ROLE_SUPER_ADMIN:
                return eventDAO.getList(new ArrayList<>());
            case ROLE_ADMIN:
                Filtering adminFiltering = new Filtering("track_id", "=", "'" + Long.toString(currentUser.getTrackId()) + "'");
                filteringList.add(0, adminFiltering);
                return eventDAO.getList(filteringList);
            case ROLE_USER:
                Filtering publishFilter = new Filtering("published", "=", "'" + Boolean.TRUE.toString() + "'");
                filteringList.add(0, publishFilter);
                Filtering filtering = new Filtering("device_id", "=", "'" + currentUser.getDeviceId() + "'");
                List<Subscription> subscriptionList = subscriptionDAO.getList(Arrays.asList(filtering));
                if (!subscriptionList.isEmpty()) {
                    StringBuilder trackId = new StringBuilder("(");
                    for (Subscription subscription : subscriptionList) {
                        trackId.append(subscription.getTrackId()).append(",");
                    }
                    trackId.delete(trackId.length() - 1, trackId.length());
                    trackId.append(")");
                    Filtering trackFilter = new Filtering("track_id", "IN", trackId.toString(), "AND");
                    filteringList.add(1, trackFilter);
                }
                return eventDAO.getList(filteringList);
        }
        return eventDAO.getList(new ArrayList<>());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Event> getEventListRoot(Principal principal, @RequestParam(value = "fromDate") Optional<String> fromDate) {
        logger.info("Getting event list Executed on empty mapping");
        return getEventList(principal, fromDate);
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
        return new ResponseEntity<>(eventDAO.update(event), HttpStatus.OK);
    }
}
