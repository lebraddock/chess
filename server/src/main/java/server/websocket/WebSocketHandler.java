package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import models.GameData;
import models.results.GameResult;
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
import java.util.List;

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
        switch (action.getCommandType()) {
            case JOIN_GAME -> joinGame(session, message);
            case CONNECT -> connect(session, message);
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

    private boolean idExists(int gameID, String auth) throws Exception{
        List<GameResult> gamesList = gameService.getGames(auth);
        for(GameResult game : gamesList){
            if(game.gameID() == gameID){
                return true;
            }
        }
        return false;
    }

    public void joinGame(Session session, String message){
        try {
            JoinGameCommand action = gsonS.fromJson(message, JoinGameCommand.class);
            int gameID = action.getGameID();
            String auth = action.getAuthToken();
            ChessGame.TeamColor color = action.getColor();
            String c;
            if (color == ChessGame.TeamColor.WHITE) {
                c = "WHITE";
            } else {
                c = "BLACK";
            }
            System.out.println(action.toString());
            gameService.updateGame(auth, c, gameID);
        }catch(Exception e){
            System.out.println("Could not join game");
        }
    }

    public void connect(Session session, String message) {
        try {
            UserGameCommand action = gsonS.fromJson(message, UserGameCommand.class);
            int gameID = action.getGameID();
            String auth = action.getAuthToken();


            connections.add(gameID, auth, session);

            if(gameService.getGame(gameID) == null || gameService.getName(auth) == null){
                String mes = "Sorry, could not join game";
                ErrorMessage notification = new ErrorMessage(mes);
                String note = gsonS.toJson(notification, ErrorMessage.class);
                connections.sendMessage(session, note);

                connections.remove(gameID, auth);
                return;
            }
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
            e.printStackTrace();
        }
    }

    public void makeMove(Session session, String message){
        MakeMoveCommand cmd = gsonS.fromJson(message, MakeMoveCommand.class);
        int gameID = cmd.getGameID();
        String auth = cmd.getAuthToken();
        ChessMove move = cmd.getMove();
        ChessGame game;


        try{
            GameData gameData = gameService.getGame(gameID);
            game = gameData.game();
            String userN = gameService.getName(auth);



            if(game.getResult() != 0){
                String winner;
                if(game.getResult() == 1){
                    winner = "white";
                }else{
                    winner = "black";
                }
                String mes = String.format("The game is over, %s already won!", winner);
                ErrorMessage notification = new ErrorMessage(mes);
                String note = gsonS.toJson(notification, ErrorMessage.class);
                connections.sendMessage(session, note);
                return;
            }
            ChessGame.TeamColor pieceColor = game.getBoard().getPiece(move.getStartPosition()).getTeamColor();
            if(userN.equals(gameData.blackUsername())){
                if(pieceColor == ChessGame.TeamColor.WHITE){
                    throw new Exception();
                }
            }
            if(userN.equals(gameData.whiteUsername())){
                if(pieceColor == ChessGame.TeamColor.BLACK){
                    throw new Exception();
                }
            }
            if(!userN.equals(gameData.blackUsername()) && !userN.equals(gameData.whiteUsername())){
                throw new Exception();
            }


            game.makeMove(move);
            GameData temp = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            ChessPiece piece = temp.game().getBoard().getPiece(move.getStartPosition());
            String p;
            gameService.makeMove(auth, temp);



            String mes = String.format("%s made a move!", userN);
            ServerMessage notification = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION,mes);
            String note = gsonS.toJson(notification, messages.Notification.class);
            connections.broadcast(gameID, session, note);

            ServerMessage load = new LoadGameMessage(game);
            String mesJson = gsonS.toJson(load, LoadGameMessage.class);
            connections.broadcast(gameID,session, mesJson);
            connections.sendMessage(session, mesJson);
        } catch(Exception e){
            String mes = String.format("Sorry, this is an invalid move");
            ErrorMessage notification = new ErrorMessage(mes);
            String note = gsonS.toJson(notification, ErrorMessage.class);
            connections.sendMessage(session, note);
        }
    }

    public void resignGame(Session session, String message){
        try {
            UserGameCommand action = gsonS.fromJson(message, UserGameCommand.class);
            int gameID = action.getGameID();
            String auth = action.getAuthToken();
            String userName = gameService.getName(auth);
            GameData game = gameService.getGame(gameID);
            if(!userName.equals(game.blackUsername()) && !userName.equals(game.whiteUsername())){
                String mes = String.format("You cannot resign, you are an observer");
                ErrorMessage errorMessage = new ErrorMessage(mes);
                String note = gsonS.toJson(errorMessage, ErrorMessage.class);
                connections.sendMessage(session, note);
                return;
            }

            if(game.game().getResult() != 0){
                String mes = String.format("You cannot resign, the game is already over.");
                ErrorMessage errorMessage = new ErrorMessage(mes);
                String note = gsonS.toJson(errorMessage, ErrorMessage.class);
                connections.sendMessage(session, note);
                return;
            }

            resign(gameID, auth);

            String mes1 = String.format("You have resigned");
            ServerMessage notification1 = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
            String note1 = gsonS.toJson(notification1, messages.Notification.class);
            connections.sendMessage(session, note1);

            String mes = String.format("%s has resigned.", userName);
            ServerMessage notification = new messages.Notification(ServerMessage.ServerMessageType.NOTIFICATION, mes);
            String note = gsonS.toJson(notification, messages.Notification.class);
            connections.broadcast(gameID, session, note);
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

