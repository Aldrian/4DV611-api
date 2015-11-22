package se.travappar.api.dal;

import se.travappar.api.model.Event;

import java.util.List;

public interface EventRepository {
    List<Event> getEventList();

    Event getEvent(long id);

    void deleteEvent(long id);

    Event createEvent(Event event);

    Event updateEvent(Event event);
}
