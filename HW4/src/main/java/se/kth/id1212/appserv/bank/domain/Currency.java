package se.kth.id1212.appserv.bank.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "CURRENCY")
public class Currency implements CurrencyDTO {

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

    @Override
    public String getName() {
        return name;
    }

    @Override
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