package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
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
    private final ConnectionManager connections;
    private final LoginService gameService;
    public WebSocketHandler(){
        this.connections = new ConnectionManager();
        this.gameService = new LoginService();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

        UserGameCommand action = gsonS.fromJson(message, UserGameCommand.class);
        System.out.println(action.getCommandType().toString());
        switch (action.getCommandType()) {
            case CONNECT -> joinGame(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leaveSession(session, message);
            case RESIGN -> resignGame(session, message);
        }
    }

    public void leaveSession(Session session, String message){
        try {
            UserGameCommand action = gsonS.fromJson(message, UserGameCommand.class);
            int gameID = action.getGameID();
            String auth = action.getAuthToken();
            String userName = gameService.getName(auth);
            String mes = String.format("%s has left.", userName);
            ServerMessage notification = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION, mes);
            String note = gsonS.toJson(notification, messages.Notification.class);
            connections.broadcast(gameID, session, note);

            connections.remove(gameID, auth);
            removeFromGame(gameID, auth);



        }catch(Exception e){
            System.out.println("Could not leave session");
        }
    }

    public void joinGame(Session session, String message) {
        try {
            JoinGameCommand action = gsonS.fromJson(message, JoinGameCommand.class);
            int gameID = action.getGameID();
            String auth = action.getAuthToken();
            connections.add(gameID, auth, session);

            //send message to user
            GameData game = gameService.getGame(gameID);
            String userN = gameService.getName(auth);
            ServerMessage load = new LoadGameMessage(game.game());
            String mesJson = gsonS.toJson(load, LoadGameMessage.class);
            connections.sendMessage(session, mesJson);

            //send message to everyone
            String mes = String.format("%s is now %s.", userN, gameRole(userN, game));
            ServerMessage notification = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION, mes);
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
            ChessPiece piece = temp.game().getBoard().getPiece(move.getStartPosition());
            String p;
            gameService.makeMove(auth, temp);
            String userN = gameService.getName(auth);


            String mes = String.format("%s made a move!", userN);
            ServerMessage notification = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION,mes);
            String note = gsonS.toJson(notification, messages.Notification.class);
            connections.broadcast(gameID, session, note);

            ServerMessage load = new LoadGameMessage(game);
            String mesJson = gsonS.toJson(load, LoadGameMessage.class);
            connections.broadcast(gameID,session, mesJson);
        } catch(Exception e){
            System.out.println("Error: Could not Make move");
        }
    }

    public void resignGame(Session session, String message){
        try {
            UserGameCommand action = gsonS.fromJson(message, UserGameCommand.class);
            int gameID = action.getGameID();
            String auth = action.getAuthToken();
            String userName = gameService.getName(auth);

            resign(gameID, auth);
            String mes = String.format("%s has resigned.", userName);
            ServerMessage notification = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION, mes);
            String note = gsonS.toJson(notification, messages.Notification.class);
            connections.broadcast(gameID, session, note);

            ChessGame game = gameService.getGame(gameID).game();

            ServerMessage load = new LoadGameMessage(game);
            String mesJson = gsonS.toJson(load, LoadGameMessage.class);
            connections.broadcast(gameID,session, mesJson);

        }catch(Exception e){
            System.out.println("Could not leave session");
        }
    }

    public void resign(int gameID, String auth){
        try{
            gameService.resign(auth, gameID);
        }catch(Exception e){
            System.out.println("Could not resign");
        }
    }

    public void removeFromGame(int gameID, String auth){
        try {
            gameService.leaveGame(auth, gameID);
        }catch(Exception e){
            System.out.println("Could not leave game");
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

