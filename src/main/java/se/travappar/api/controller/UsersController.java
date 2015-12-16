package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.UserRole;
import se.travappar.api.model.Users;
import se.travappar.api.utils.security.CurrentUser;

import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    UserDAO userDAO;
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
        ResponseEntity<?> responseEntity = null;
        try {
            responseEntity = new ResponseEntity<>(userDAO.create(user), HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.info("Creating user exception " + e);
            Throwable rootCause = ((DataIntegrityViolationException) e).getRootCause();
            if(rootCause.getMessage().contains("duplicate key")) {
                responseEntity = new ResponseEntity<>("DeviceID is already in use.", HttpStatus.BAD_REQUEST);
            } else {
                responseEntity = new ResponseEntity<>(e.getMessage() + " : " + rootCause.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return responseEntity;
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Users updateUser(@RequestBody Users user) {
        logger.info("Update user executed on / with user with device_id=" + user.getDeviceId());
        return userDAO.update(user);
    }
}
