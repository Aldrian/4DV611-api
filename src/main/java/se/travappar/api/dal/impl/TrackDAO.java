package se.travappar.api.dal.impl;

import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Track;

public class TrackDAO extends AbstractDAO<Track> {
    public TrackDAO() {
        super(Track.class);
    }
}
