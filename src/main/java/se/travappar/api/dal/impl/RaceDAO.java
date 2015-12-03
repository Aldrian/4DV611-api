package se.travappar.api.dal.impl;

import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Race;

public class RaceDAO extends AbstractDAO<Race> {
    public RaceDAO() {
        super(Race.class);
    }
}
