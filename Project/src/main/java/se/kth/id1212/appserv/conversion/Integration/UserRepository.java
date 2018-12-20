package se.kth.id1212.appserv.conversion.Integration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.kth.id1212.appserv.conversion.Model.*;

/**
 * Contains all database access concerning holders.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // find currency by name
    User findByUsername(String username);

}