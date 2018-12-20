package se.kth.id1212.appserv.conversion.Model;

// message that tells that this client has left the conversation

public class LogoutMessage {


    private String username;

    public LogoutMessage() {
    }

    public LogoutMessage(String username) {
        this.username = username;
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
}