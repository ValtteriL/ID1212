package se.kth.id1212.appserv.conversion.Model;

// message that tells that this client has joined the conversation

public class WebsocketErrormessage {


    private String msg;

    public WebsocketErrormessage() {
    }

    public WebsocketErrormessage(String msg) {
        this.msg = msg;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return msg;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String msg) {
        this.msg = msg;
    }
}