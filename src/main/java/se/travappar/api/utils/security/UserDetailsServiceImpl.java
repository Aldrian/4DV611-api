package se.travappar.api.utils.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import se.travappar.api.dal.impl.UserDAO;
import se.travappar.api.model.Users;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Users users = userDAO.findByUsernameOrDeviceId(s);
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        CurrentUser user = new CurrentUser(users);
        return user;
    }
}
