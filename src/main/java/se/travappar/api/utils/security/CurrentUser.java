package se.travappar.api.utils.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import se.travappar.api.model.UserRole;
import se.travappar.api.model.Users;

import java.util.Arrays;
import java.util.Collection;

public class CurrentUser extends User {

    String email;
    String deviceId;
    Long trackId;
    UserRole role;

    public CurrentUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public CurrentUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public CurrentUser(Users users) {
        super(users.getUsername() != null ? users.getUsername() : users.getDeviceId(),
                users.getPassword() != null ? users.getPassword() : users.getDeviceId(),
                users.getEnabled(),
                true, true, true,
                Arrays.<GrantedAuthority>asList(new SimpleGrantedAuthority(users.getRole() != null ? UserRole.getByCode(users.getRole()).toString() : UserRole.ROLE_USER.toString())));
        deviceId = users.getDeviceId();
        role = UserRole.getByCode(users.getRole());
        email = users.getEmail();
        trackId = users.getTrackId();
    }

    public String getEmail() {
        return email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public UserRole getRole() {
        return role;
    }

    public Long getTrackId() {
        return trackId;
    }
}
