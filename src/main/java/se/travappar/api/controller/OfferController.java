package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.OfferDAO;
import se.travappar.api.model.Offer;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/offers")
public class OfferController {

    @Autowired
    OfferDAO offerDAO;
    private static final Logger logger = LogManager.getLogger(OfferController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Offer> getOfferList() {
        logger.info("Getting offer list Executed on /");
        return offerDAO.getList(new ArrayList<>());
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Offer> getOfferListRoot() {
        logger.info("Getting offer list Executed on empty mapping");
        return getOfferList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Offer getOffer(@PathVariable long id) {
        logger.info("Getting offer executed on / with id=" + id);
        return offerDAO.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteOffer(@PathVariable long id) {
        logger.info("Delete offer executed on / with id=" + id);
        Offer offer = offerDAO.get(id);
        offerDAO.delete(offer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Offer createOffer(@RequestBody Offer offer) {
        logger.info("Creating offer executed on /");
        return offerDAO.create(offer);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Offer updateOffer(@RequestBody Offer offer) {
        logger.info("Update offer executed on / with offer with id=" + offer.getId());
        return offerDAO.update(offer);
    }
}
