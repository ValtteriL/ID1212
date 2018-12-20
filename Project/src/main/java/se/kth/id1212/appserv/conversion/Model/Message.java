package se.kth.id1212.appserv.conversion.Model;

// this is a message that gets sent to/from clients
public class Message {
    private String msg;
    private String username;

    public Message() {
    }

    public Message(String username, String msg) {
        this.username = username;
        this.msg = msg;
    }


    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }
    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}