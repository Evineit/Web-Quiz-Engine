package engine.services;

import engine.models.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    UserDetails loadUserByUsername(String username);

    UserDetails saveUser(User user);
}
