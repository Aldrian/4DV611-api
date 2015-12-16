package se.travappar.api.dal.impl;

import org.springframework.transaction.annotation.Transactional;
import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Event;
import se.travappar.api.model.filter.Filtering;

import java.util.List;

public class EventDAO extends AbstractDAO<Event> {

    public EventDAO() {
        super(Event.class);
    }

    @Transactional
    @Override
    public List<Event> getList(List<Filtering> filteringList) {
        StringBuilder stringBuilder = getWhereFiltering(filteringList);
        List<?> list = getHibernateTemplate().find("FROM " + Event.class.getSimpleName() + stringBuilder.toString() + " ORDER BY date DESC");
        return (List<Event>) list;
    }
}
