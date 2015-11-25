package se.travappar.api.dal.impl;

import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Event;

public class EventDAO extends AbstractDAO<Event> {

    public EventDAO() {
        super(Event.class);
    }
}
