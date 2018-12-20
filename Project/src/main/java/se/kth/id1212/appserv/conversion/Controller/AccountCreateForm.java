package se.kth.id1212.appserv.conversion.Controller;

import javax.validation.constraints.NotNull;

class AccountCreateForm {
    
    @NotNull(message = "Please specify username")
    private String username;

    @NotNull(message = "Please enter password")
    private String password;

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

   /**
    * @return the username
    */
   public String getUsername() {
       return username;
   } 

   /**
    * @return the password
    */
   public String getPassword() {
       return password;
   }
}