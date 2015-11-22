package se.travappar.api.dal.impl;

import se.travappar.api.dal.EventRepository;
import se.travappar.api.model.Event;
import se.travappar.api.model.Track;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Repository which work with database
 */
public class MockEventRepository implements EventRepository {
    @Override
    public List<Event> getEventList() {
        Track track = new Track();
        track.setId(1L);
        track.setName("Track1");

        Event event = new Event();
        event.setId(1L);
        event.setDate(new Date());
        event.setOffer("Test offer");
        event.setOfferImage("image/img.png");
        event.setTrack(track);
        event.setHighlight("some Highlight");
        event.setHomeTeam("Team 1");

        Event event1 = new Event();
        event1.setId(2L);
        event1.setDate(new Date());
        event1.setOffer("Test offer 1");
        event1.setOfferImage("image/img2.png");
        event1.setTrack(track);
        event1.setHighlight("some Highlight12");
        event1.setHomeTeam("Team 2");
        return Arrays.asList(event, event1);
    }

    @Override
    public Event getEvent(long id) {
        Track track = new Track();
        track.setId(1L);
        track.setName("Track1");

        Event event = new Event();
        event.setId(1L);
        event.setDate(new Date());
        event.setOffer("Test offer");
        event.setOfferImage("image/img.png");
        event.setTrack(track);
        event.setHighlight("some Highlight");
        event.setHomeTeam("Team 1");
        return event;
    }

    @Override
    public void deleteEvent(long id) {

    }

    @Override
    public Event createEvent(Event event) {
        event.setId(2L);
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        return event;
    }
}
