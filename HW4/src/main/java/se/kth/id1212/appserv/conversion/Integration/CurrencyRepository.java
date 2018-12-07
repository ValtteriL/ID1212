package se.kth.id1212.appserv.conversion.Integration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.kth.id1212.appserv.conversion.Model.*;

/**
 * Contains all database access concerning holders.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    // find currency by name
    Currency findCurrencyByName(String name);

}