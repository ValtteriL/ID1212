package se.kth.id1212.appserv.bank.presentation.acct;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

class ConversionForm {
    @NotBlank(message = "Please specify currency to convert from")
    @NotNull(message = "Please specify currency to convert from")
    private String from;

    @NotBlank(message = "Please specify currency to convert to")
    @NotNull(message = "Please specify currency to convert to")
    private String to;

    @NotNull(message = "Please specify amount")
    private Integer amount;


    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}