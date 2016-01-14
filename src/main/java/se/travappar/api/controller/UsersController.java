package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.OfferDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.dal.impl.VisitDAO;
import se.travappar.api.model.Offer;
import se.travappar.api.model.Track;
import se.travappar.api.model.Visit;
import se.travappar.api.model.enums.OfferType;
import se.travappar.api.model.enums.UserRole;
import se.travappar.api.model.Users;
import se.travappar.api.utils.label.LabelUtils;
import se.travappar.api.utils.publish.mailchimp.MailChimpHelper;
import se.travappar.api.utils.security.CurrentUser;

import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    UserDAO userDAO;
    @Autowired
    OfferDAO offerDAO;
    @Autowired
    TrackDAO trackDAO;
    @Autowired
    VisitDAO visitDAO;
    @Autowired
    MailChimpHelper mailChimpHelper;
    private static final Logger logger = LogManager.getLogger(UsersController.class);

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ResponseEntity<Users> authorization(Principal principal) {
        CurrentUser user = (CurrentUser) ((Authentication) principal).getPrincipal();
        logger.info("Authorization user");
        return ResponseEntity.ok().body(userDAO.findByUsernameOrDeviceId(principal.getName()));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getUserList(Principal principal) {
        logger.info("Getting user list Executed on /");
        CurrentUser currentUser = (CurrentUser) ((Authentication) principal).getPrincipal();
        if (!UserRole.ROLE_SUPER_ADMIN.equals(currentUser.getRole())) {
            logger.info("User has no rights to access this resource");
            return new ResponseEntity<>("User has no rights to access this resource", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(userDAO.getList(new ArrayList<>()), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getUserListRoot(Principal principal) {
        logger.info("Getting user list Executed on empty mapping");
        return getUserList(principal);
    }

    @RequestMapping(value = "/{deviceId}/offers", method = RequestMethod.GET)
    public ResponseEntity<?> getUserOffers(@PathVariable String deviceId, Principal principal) {
        logger.info("Getting offer list for user " + deviceId);
        List<Offer> userOfferList = offerDAO.getUserOfferList(deviceId);
        CurrentUser currentUser = (CurrentUser) ((Authentication) principal).getPrincipal();
        if(currentUser.getRole().equals(UserRole.ROLE_ADMIN)) {
            logger.info("Creating visit entity for " + deviceId);
            Track track = trackDAO.get(currentUser.getTrackId());
            Visit visit = new Visit();
            visit.setTrack(track);
            visit.setDeviceId(deviceId);
            visit = visitDAO.create(visit);
            logger.info("Visit entity for " + deviceId + " created. Id :" + visit.getId());
        }
        return new ResponseEntity<List<Offer>>(userOfferList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Users getUser(@PathVariable String deviceId) {
        logger.info("Getting user executed on / with id=" + deviceId);
        return userDAO.get(deviceId);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable String deviceId) {
        logger.info("Delete user executed on / with id=" + deviceId);
        Users user = userDAO.get(deviceId);
        userDAO.delete(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public ResponseEntity<?> createUser(@RequestBody Users user) {
        logger.info("Creating user executed on /");
        if (user.getRole() == null) {
            user.setRole(UserRole.ROLE_USER.getCode());
        }
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
        if (UserRole.getByCode(user.getRole()) == UserRole.ROLE_USER) {
            user.setUsername(user.getDeviceId());
            user.setPassword(user.getDeviceId());
        }
        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = new ResponseEntity<>(userDAO.create(user), HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.info("Creating user exception " + e);
            Throwable rootCause = ((DataIntegrityViolationException) e).getRootCause();
            if (rootCause.getMessage().contains("duplicate key")) {
                responseEntity = new ResponseEntity<>("DeviceID is already in use.", HttpStatus.BAD_REQUEST);
            } else {
                responseEntity = new ResponseEntity<>(e.getMessage() + " : " + rootCause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return responseEntity;
        }
        if(user.getRole().equals(UserRole.ROLE_USER.getCode())) {
            logger.info("Creating offer for new user.");
            Offer offer = new Offer();
            offer.setDeviceId(user.getDeviceId());
            offer.setOfferType(OfferType.FREE_ENTRANCE.getCode());
            offer.setDescription(LabelUtils.ANY_TRACK_FREE_ENTRANCE.getMessage());
            offerDAO.create(offer);
            logger.info("Offer was created.");
        }
        return responseEntity;
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public ResponseEntity updateUser(@RequestBody Users user) {
        logger.info("Update user executed on / with user with device_id=" + user.getDeviceId());
        Users oldUser = userDAO.get(user.getDeviceId());
        if (oldUser.getEmail() != null && !oldUser.getEmail().isEmpty() && !oldUser.getEmail().equals(user.getEmail())) {
            try {
                mailChimpHelper.unSubscribeUserFromList(oldUser);
            } catch (Exception e) {
                logger.info("Error while unsubscribing user from MailChimp list. " + oldUser.getDeviceId(), e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        Users updatedUser = userDAO.update(user);
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            try {
                Users users = mailChimpHelper.subscribeUserToList(updatedUser);
                updatedUser = userDAO.update(users);
            } catch (Exception e) {
                logger.info("Error while subscribing user to MailChimp list. " + user.getDeviceId(), e);
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
