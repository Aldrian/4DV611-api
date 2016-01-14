package se.travappar.api.dal.impl;

import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Visit;

import java.util.List;

public class VisitDAO extends AbstractDAO<Visit> {
    public VisitDAO() {
        super(Visit.class);
    }

    public List<Visit> getTrackVisitList(long trackId) {
        List<Visit> list = (List<Visit>) getHibernateTemplate().find("from " + Visit.class.getSimpleName() + " where track_id=?", trackId);
        return list;
    }
}
