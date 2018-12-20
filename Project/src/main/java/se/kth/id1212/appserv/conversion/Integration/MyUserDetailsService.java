package se.kth.id1212.appserv.conversion.Integration;

import org.springframework.stereotype.Service;
import se.kth.id1212.appserv.conversion.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

// custom userdetailsservice to enable usage of Users in database for authentication
@Service
public class MyUserDetailsService implements UserDetailsService {
 
    @Autowired
    private UserRepository userRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }
}