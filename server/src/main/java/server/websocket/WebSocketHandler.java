package server.websocket;

import com.google.gson.Gson;
import models.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.LoginService;
import websocket.commands.JoinGameCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.Action;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;

public class WebSocketHandler{
    protected static Gson gsonS = new Gson();
    private final ConnectionManager connections = new ConnectionManager();
    private final LoginService gameService = new LoginService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = gsonS.fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_GAME -> joinGame(session, message);
        }
    }

    public void joinGame(Session session, String message) {
        JoinGameCommand action = gsonS.fromJson(message, JoinGameCommand.class);
        int gameID = action.getGameID();
        String auth = action.getAuthToken();
        connections.add(gameID, auth, session);

        try {
            GameData game = gameService.getGame(gameID);
            String userN = gameService.getName(auth);
            ServerMessage load = new LoadGameMessage(game.game());
            String mesJson = gsonS.toJson(load, ServerMessage.class);
            connections.sendMessage(session, mesJson);

            String mes = String.format("%s is now %s.", userN, gameRole(userN, game));
            ServerMessage notification = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            String note = gsonS.toJson(notification, messages.Notification.class);
            connections.broadcast(game.gameID(), session, note);
        }catch(Exception e){
            System.out.println("Error: Could not join game");
        }
    }

    private String gameRole(String username, GameData game){
        if(username.equals(game.whiteUsername())){
            return "white.";
        }else if(username.equals(game.blackUsername())){
            return "black.";
        }else{
            return "spectating.";
        }
    }
}
