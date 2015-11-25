package se.travappar.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.travappar.api.dal.impl.EventDAO;
import se.travappar.api.dal.impl.TrackDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.HelloWorld;
import se.travappar.api.model.Track;

import java.util.Date;

@RestController
public class HelloWorldController {

    @Autowired
    EventDAO eventDAO;
    @Autowired
    TrackDAO trackDAO;

    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public @ResponseBody
    HelloWorld helloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setField("Hello World!");
        return helloWorld;
    }

    @RequestMapping(value = "/helloworldCreate", method = RequestMethod.GET)
    public @ResponseBody
    HelloWorld helloWorldCreate() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setField("Hello World!");

        Track track = new Track();
        track.setName("Test Track");
        track.setAddress("Address");
        track = trackDAO.create(track);

        Event event = new Event();
        event.setDate(new Date());
        event.setName("EVent Name");
        event.setOffer("Test offer");
        event.setOfferImage("image/img.png");
        event.setTrack(track);
        event.setTrackList("track 1, track 2");
        event.setHighlight("some Highlight");
        event.setHomeTeam("Team 1");

        Event event1 = new Event();
        event1.setDate(new Date());
        event1.setOffer("Test offer 2");
        event1.setName("EVent Name2");
        event1.setOfferImage("image/img2.png");
        event1.setTrack(track);
        event1.setTrackList("track 1, track 3");
        event1.setHighlight("some Highlight12");
        event1.setHomeTeam("Team 2");

        eventDAO.create(event);
        eventDAO.create(event1);

        return helloWorld;
    }
}
