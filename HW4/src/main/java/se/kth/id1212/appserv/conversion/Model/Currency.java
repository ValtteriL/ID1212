package se.kth.id1212.appserv.conversion.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "CURRENCY")
public class Currency {

    @Id
    @Column(name = "NAME")
    private String name;

    @Column(name = "WORTHINGOLD")
    private float worthInGold;

    public Currency(String name, float worthInGold) {
        this.name = name;
        this.worthInGold = worthInGold;
    }

    /**
     * Required by JPA, should not be used.
     */
    protected Currency() {
    }

    public String getName() {
        return name;
    }

    public float getWorthInGold() {
        return worthInGold;
    }

    /**
     * @param worthInGold the worthInGold to set
     */
    public void setWorthInGold(float worthInGold) {
        this.worthInGold = worthInGold;
    }
}