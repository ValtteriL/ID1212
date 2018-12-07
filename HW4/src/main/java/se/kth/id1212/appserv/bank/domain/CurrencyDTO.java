package se.kth.id1212.appserv.bank.domain;

/**
 * Defines all operation that can be performed on an {@link Account} outside
 * the application and domain layers.
 */
public interface CurrencyDTO {
    
    //Returns the worth of currency in gold
    public float getWorthInGold();

    // Returns the name of the currency
    public String getName();

    // Sets new worth in gold for the curency
    public void setWorthInGold(float worthInGold);

}
