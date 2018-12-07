package se.kth.id1212.appserv.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.kth.id1212.appserv.bank.domain.*;

/**
 * Contains all database access concerning holders.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface ConversionRepository extends JpaRepository<Conversion, Long> {


}