package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import models.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.LoginService;
import websocket.commands.JoinGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.Action;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
@WebSocket
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

    public void makeMove(Session session, String message){
        MakeMoveCommand cmd = gsonS.fromJson(message, MakeMoveCommand.class);
        int gameID = cmd.getGameID();
        String auth = cmd.getAuthToken();
        ChessMove move = cmd.getMove();

        try{
            GameData gameData = gameService.getGame(gameID);
            ChessGame game = gameData.game();
            game.makeMove(move);
            GameData temp = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            gameService.makeMove(auth, temp);
        } catch(Exception e){
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

