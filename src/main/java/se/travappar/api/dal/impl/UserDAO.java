package se.travappar.api.dal.impl;

import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Users;

public class UserDAO extends AbstractDAO<Users> {
    public UserDAO() {
        super(Users.class);
    }
}
