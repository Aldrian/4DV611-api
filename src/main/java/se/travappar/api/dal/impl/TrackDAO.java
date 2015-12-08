package se.travappar.api.dal.impl;

import org.springframework.transaction.annotation.Transactional;
import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Track;
import se.travappar.api.model.Users;

import java.util.List;

public class TrackDAO extends AbstractDAO<Track> {
    public TrackDAO() {
        super(Track.class);
    }

    @Override
    @Transactional
    public void delete(Track entity) {
        getHibernateTemplate().delete(entity);
        List<Users> list = (List<Users>) getHibernateTemplate().find("from " + Users.class.getSimpleName() + " where track_id=" + entity.getId());
        if(!list.isEmpty()) {
            getHibernateTemplate().delete(list.get(0));
        }
    }
}
