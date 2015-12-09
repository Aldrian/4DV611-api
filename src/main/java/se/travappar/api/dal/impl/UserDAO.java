package se.travappar.api.dal.impl;

import se.travappar.api.dal.AbstractDAO;
import se.travappar.api.model.Users;

import java.util.List;

public class UserDAO extends AbstractDAO<Users> {
    public UserDAO() {
        super(Users.class);
        id_column  = "device_id";
    }

    public Users get(String id) {
        List<Users> list = (List<Users>) getHibernateTemplate().find("from " + Users.class.getSimpleName() + " where " + id_column + "=?", id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public Users findByUsernameOrDeviceId(String identifier) {
        List<Users> list = (List<Users>) getHibernateTemplate().find("from " + Users.class.getSimpleName() + " where device_id=? OR username=?", identifier, identifier);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
