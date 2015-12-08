package se.travappar.api.dal.impl;

import org.springframework.transaction.annotation.Transactional;
import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Event;

import java.util.List;

public class EventDAO extends AbstractDAO<Event> {

    public EventDAO() {
        super(Event.class);
    }

    @Transactional
    @Override
    public List<Event> getList() {
        List<?> list = getHibernateTemplate().find("from " + Event.class.getSimpleName() + " order by date");
        return (List<Event>) list;
    }

}
