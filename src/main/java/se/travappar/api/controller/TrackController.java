package se.travappar.api.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.model.Track;

import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping(value = "/tracks")
public class TrackController {

    @Autowired
    TrackDAO trackDAO;
    private static final Logger logger = LogManager.getLogger(TrackController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Track> getTrackList() {
        logger.info("Getting track list Executed on /");
        return trackDAO.getList();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Track> getTrackListRoot() {
        logger.info("Getting track list Executed on empty mapping");
        return getTrackList();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Track getTrack(@PathVariable long id) {
        logger.info("Getting track executed on / with id=" + id);
        return trackDAO.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTrack(@PathVariable long id) {
        logger.info("Delete track executed on / with id=" + id);
        Track track = trackDAO.get(id);
        trackDAO.delete(track);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Track createTrack(@RequestBody Track track) {
        logger.info("Creating track executed on /");
        return trackDAO.create(track);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.MULTIPART_FORM_DATA,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN})
    public
    @ResponseBody
    Track updateTrack(@RequestBody Track track) {
        logger.info("Update track executed on / with track with id=" + track.getId());
        return trackDAO.update(track);
    }
}
