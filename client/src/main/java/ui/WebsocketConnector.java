package ui;

import chess.ChessGame;
import chess.ChessMove;
import exception.ResponseException;
import models.AuthData;
import ui.NotificationHandler;
import messages.Notification;
import com.google.gson.Gson;
import websocket.commands.JoinGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketConnector extends Endpoint{
    private Session session;
    NotificationHandler notificationHandler;
    Gson gsonS = new Gson();

    public WebsocketConnector(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = gsonS.fromJson(message, ServerMessage.class);
                    switch(serverMessage.getServerMessageType()){
                        case LOAD_GAME -> notificationHandler.loadGame(message);
                        case NOTIFICATION -> notificationHandler.notify(message);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    public void joinGamePlayer(String authToken, int gameID, ChessGame.TeamColor color) throws Exception{
        JoinGameCommand cmd = new JoinGameCommand(authToken, gameID, color);
        String mes = gsonS.toJson(cmd, JoinGameCommand.class);
        this.session.getBasicRemote().sendText(mes);
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception{
        MakeMoveCommand cmd = new MakeMoveCommand(authToken, gameID, move);
        String mes = gsonS.toJson(cmd, MakeMoveCommand.class);
        this.session.getBasicRemote().sendText(mes);
    }


    //public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}
    //public void onOpen(Session session, EndpointConfig endpointConfig) {}

}