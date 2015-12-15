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
        StringBuilder stringBuilder = new StringBuilder("");
        if(filteringList != null && !filteringList.isEmpty()) {
            stringBuilder.append(" WHERE ");
            for (Filtering filtering : filteringList) {
                String relation = "";
                if (filtering.getRelationWithPrevious() != null) {
                    relation = filtering.getRelationWithPrevious();
                }
                stringBuilder.append(" ").append(relation).append(" ")
                        .append(filtering.getColumnName()).append(" ")
                        .append(filtering.getOperator()).append(" ")
                        .append(filtering.getValue()).append(" ");
            }
        }
        List<?> list = getHibernateTemplate().find("FROM " + Event.class.getSimpleName() + stringBuilder.toString() + " ORDER BY date DESC");
        return (List<Event>) list;
    }

}
