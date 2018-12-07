package se.kth.id1212.appserv.bank.presentation.acct;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

class CurrencyCreateForm {
    
    @NotBlank(message = "Please specify name for currency")
    @NotNull(message = "Please specify name for currency")
    private String currency;

    @NotNull(message = "Please enter worth in gold")
    private Float amount;

    /**
     * @return the amount
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

}