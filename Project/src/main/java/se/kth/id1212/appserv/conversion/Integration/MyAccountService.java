package se.kth.id1212.appserv.conversion.Integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.kth.id1212.appserv.conversion.Model.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
@Service
public class MyAccountService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // find user by name
    public User findAccount(String username) {
        return userRepo.findByUsername(username);
    }

    // add new Account
    public void newAcc(String username, String password) {
        userRepo.save(new User(username, passwordEncoder.encode(password)));
    }
}
