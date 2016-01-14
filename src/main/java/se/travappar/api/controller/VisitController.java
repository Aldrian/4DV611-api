package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.OfferDAO;
import se.travappar.api.dal.impl.VisitDAO;
import se.travappar.api.model.Visit;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/visits")
public class VisitController {

    @Autowired
    VisitDAO visitDAO;
    @Autowired
    OfferDAO offerDAO;
    private static final Logger logger = LogManager.getLogger(VisitController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Visit> getVisitList() {
        logger.info("Getting visit list Executed on /");
        return visitDAO.getList(new ArrayList<>());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Visit> getVisitListRoot() {
        logger.info("Getting visit list Executed on empty mapping");
        return getVisitList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Visit getVisit(@PathVariable long id) {
        logger.info("Getting visit executed on / with id=" + id);
        return visitDAO.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteVisit(@PathVariable long id) {
        logger.info("Delete visit executed on / with id=" + id);
        Visit visit = visitDAO.get(id);
        visitDAO.delete(visit);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Visit createVisit(@RequestBody Visit visit) {
        logger.info("Creating visit executed on /");
        return visitDAO.create(visit);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Visit updateVisit(@RequestBody Visit visit) {
        logger.info("Update visit executed on / with visit with id=" + visit.getId());
        return visitDAO.update(visit);
    }
}
