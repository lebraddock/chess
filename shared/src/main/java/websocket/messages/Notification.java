package messages;

import websocket.messages.ServerMessage;

public class Notification extends ServerMessage {

    private final String message;
    ServerMessageType serverMessageType;

    public Notification(ServerMessageType type, String message) {
        super(type);
        this.serverMessageType = ServerMessageType.NOTIFICATION;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}

