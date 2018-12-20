package se.kth.id1212.appserv.conversion.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.util.HtmlUtils;

import se.kth.id1212.appserv.conversion.Model.LoginMessage;
import se.kth.id1212.appserv.conversion.Model.LogoutMessage;
import se.kth.id1212.appserv.conversion.Model.Message;
import se.kth.id1212.appserv.conversion.Model.WebsocketErrormessage;

// handle received websocket messages

@Controller
public class WebSocketController {

    // log in
    @MessageMapping("/login")
    @SendTo("/topic/logins") // the return value is broadcast here to all subscribers
    public LoginMessage login(Authentication auth, LoginMessage loginMessage) throws Exception {
        // broadcast the message escaped to all subscribers
        return new LoginMessage(HtmlUtils.htmlEscape(auth.getName()));
    }

    // log out
    @MessageMapping("/logout")
    @SendTo("/topic/logouts") // the return value is broadcast here to all subscribers
    public LogoutMessage logout(Authentication auth, LoginMessage logoutMessage) throws Exception {
        // broadcast the message escaped to all subscribers
        return new LogoutMessage(HtmlUtils.htmlEscape(auth.getName()));
    }

    // send message
    @MessageMapping("/message") 
    @SendTo("/topic/messages") // the return value is broadcast here to all subscribers
    public Message message(Authentication auth, Message message) throws Exception {

        // truncate username and msg to at most 1000 character
        String truncatedmsg = message.getMsg().substring(0, Math.min(1000, message.getMsg().length()));

        // broadcast the message escaped to all subscribers
        return new Message(HtmlUtils.htmlEscape(auth.getName()),HtmlUtils.htmlEscape(truncatedmsg));
    }

    // Handle Websocket errors
    @MessageExceptionHandler
    @SendToUser("/topic/error") // send message to the error channel of the causing client
    public WebsocketErrormessage handleException(Exception e) {
        return new WebsocketErrormessage("Something went wrong!");
    }
}