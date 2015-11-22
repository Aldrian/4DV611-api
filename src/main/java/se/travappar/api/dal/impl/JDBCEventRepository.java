package se.travappar.api.dal.impl;

import se.travappar.api.dal.EventRepository;
import se.travappar.api.model.Event;

import java.util.List;

/**
 * Repository which work with database
 */
public class JDBCEventRepository implements EventRepository {
    @Override
    public List<Event> getEventList() {
        return null;
    }

    @Override
    public Event getEvent(long id) {
        return null;
    }

    @Override
    public void deleteEvent(long id) {

    }

    @Override
    public Event createEvent(Event event) {
        return null;
    }

    @Override
    public Event updateEvent(Event event) {
        return null;
    }
}
