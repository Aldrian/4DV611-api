package se.travappar.api.dal.impl;

import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Offer;

import java.util.List;

public class OfferDAO extends AbstractDAO<Offer> {
    public OfferDAO() {
        super(Offer.class);
    }

    public List<Offer> getUserOfferList(String deviceId) {
        List<Offer> list = (List<Offer>) getHibernateTemplate().find("from " + Offer.class.getSimpleName() + " where user_device_id=?", deviceId);
        return list;
    }

    public List<Offer> getTrackOfferList(Long trackId) {
        List<Offer> list = (List<Offer>) getHibernateTemplate().find("from " + Offer.class.getSimpleName() + " where track_id=?", trackId);
        return list;
    }
}
