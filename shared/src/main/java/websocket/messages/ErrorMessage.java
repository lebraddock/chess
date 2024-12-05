package websocket.messages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage{
    private String errorMessage;

    public ErrorMessage(String message){
        super(ServerMessageType.ERROR);
        this.serverMessageType = ServerMessageType.ERROR;
        this.errorMessage = message;
    }

    public String getMessage(){
        return errorMessage;
    }
}