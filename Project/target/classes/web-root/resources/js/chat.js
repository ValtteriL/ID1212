// connect websocket, subscribe, send login message
function connect() {
    var socket = new WebSocket('ws://127.0.0.1:8080/gs-guide-websocket');

    ws = Stomp.over(socket);
    ws.connect({}, function (frame) {

        // successful connect (or reconnect)

        // subscribe for chat messages
        ws.subscribe("/topic/messages", function (message) {
            var newmsg = JSON.parse(message.body);
            $("#conversationarea").append("<p>" + newmsg.username + ": " + newmsg.msg + "</p>");
            $("#conversationarea").scrollTop($("#conversationarea").prop('scrollHeight')); // scroll to bottom of chat
        });

        // subscribe for logins
        ws.subscribe("/topic/logins", function (message) {
            var loginmsg = JSON.parse(message.body);
            $("#conversationarea").append("<p class=\"loginmessage\">" + loginmsg.username + " joined the conversation</p>");
            $("#conversationarea").scrollTop($("#conversationarea").prop('scrollHeight')); // scroll to bottom of chat
        });

        // subscribe for logouts
        ws.subscribe("/topic/logouts", function (message) {
            var logoutmsg = JSON.parse(message.body);
            $("#conversationarea").append("<p class=\"logoutmessage\">" + logoutmsg.username + " left.</p>");
            $("#conversationarea").scrollTop($("#conversationarea").prop('scrollHeight')); // scroll to bottom of chat
        });

        // subscribe for errors (if server throws when handling our message, we get error message here)
        ws.subscribe("/user/topic/error", function (message) {
            var errormsg = JSON.parse(message.body);
            tellabouterror("Error sending message: " + errormsg.message);
        });

        login(); // login when connected (tells other users we joined the convo)
    }, function (error) {
        // error connecting...
        tellabouterror("Error in connection, please reload the page to try again");
        $("button#sendbtn").attr("disabled", "disabled"); // disable sendbutton
        $("#messagetextbox").attr("disabled", "disabled"); // disable messagebox
    });
}

// disconnect from the chat
// used only in manual testing
function disconnect() {
    if (ws != null) {
        ws.disconnect();
    }
    console.log("Disconnected");
}

// send loginmessage to chat
function login() {
    if (ws.connected) {
        ws.send("/app/login", {}, JSON.stringify({ 'username': 'kek' }));
    } else {
        tellabouterror("Could not send login message. Not connected");
    }
}

// send logoutmessage to chat
function logout() {
    if (ws.connected) {
        ws.send("/app/logout", {}, JSON.stringify({ 'username': 'kek' }));
    } else {
        tellabouterror("Could not send logout message. Not connected");
    }
}

// send message to chat
function sendmsg(message) {
    if (ws.connected) {
        // the username here doesn't matter as the server won't believe it anyway
        ws.send("/app/message", {}, JSON.stringify({ 'username': 'kek', 'msg': message }))
    } else {
        tellabouterror("Could not send message. Not connected");
    }
}

// indicate user that the connection to chat is broken
function tellabouterror(message) {
    $("#conversationarea").append("<h3 class=\"errormessage\">" + message + "</h3>");
    $("#conversationarea").scrollTop($("#conversationarea").prop('scrollHeight')); // scroll to bottom of chat
}

// do these when page loads
$(document).ready(function () {

    // connect to websocket
    connect();

    // send message button clicked
    $("button#sendbtn").click(function () {
        sendmsg($("#messagetextbox").val()); // send message
        $("#messagetextbox").val(""); // empty the messagebox
    });

    // logout when exiting page
    $(window).on("beforeunload", function (e) {
        logout();
    });
});