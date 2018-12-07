// stores the amount of conversion done using the app
package se.kth.id1212.appserv.conversion.Model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@Table(name = "CONVERSIONS")
public class Conversion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONVERSION_ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "AMOUNT")
    private float amount = 0;

    /**
     * Required by JPA, should not be used.
     */
    protected Conversion() {
    }

    // constructor
    public Conversion(float amount) {
        this.amount = amount;
    }
}