package se.travappar.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Users;

import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    UserDAO userDAO;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Users> getUserList() {
        return userDAO.getList();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Users> getUserListRoot() {
        return getUserList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Users getUser(@PathVariable long id) {
        return userDAO.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable long id) {
        Users user = userDAO.get(id);
        userDAO.delete(user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Users createUser(@RequestBody Users user) {
        return userDAO.create(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Users updateUser(@RequestBody Users user) {
        return userDAO.update(user);
    }
}
