package service;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import models.results.GameResult;
import models.results.RegResult;
import models.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.UUID;

public class LoginService extends Service{

    public RegResult registerUser(UserData user) throws DataAccessException {
        if (userDA.getUser(user.username()) != null) {
            throw new DataAccessException("Error: already taken");
        }
        if(user.username() == null || user.password() == null || user.email() == null){
            throw new DataAccessException("Error: bad request");
        }
        userDA.createUser(user);
        String authToken = UUID.randomUUID().toString();
        authDA.createAuth(new AuthData(user.username(), authToken));
        return new RegResult(user.username(), authToken);
    }
    public RegResult loginUser(String username, String password) throws DataAccessException{
        if(userDA.getUser(username) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        UserData userTemp = userDA.getUser(username);
        if(!BCrypt.checkpw(password, userTemp.password())){
            throw new DataAccessException("Error: unauthorized");
        }
        String authToken = UUID.randomUUID().toString();
        authDA.createAuth(new AuthData(username, authToken));
        return new RegResult(username, authToken);
    }
    public void logout(String authToken) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        authDA.deleteAuth(authToken);
    }
    public String getAuthToken(String username) throws DataAccessException{
        return authDA.getUserAuth(username).authToken();
    }
    public List<GameResult> getGames(String authToken) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return gameDA.getGames();
    }

    public GameData getGame(int gameID) throws DataAccessException{
        return gameDA.getGame(gameID);
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(gameName == null || authToken == null){
            throw new DataAccessException("Error: bad request");
        }
        int gameID = (int)(Math.random() * 9000) + 999;
        return gameDA.createGame(new GameData(gameID, null, null, gameName, new ChessGame()));
    }

    public GameData updateGame(String authToken, String color, int gameID) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        if(gameDA.getGame(gameID) == null || authToken == null || color == null){
            throw new DataAccessException("Error: bad request");
        }

        if(color.equals("WHITE")){
            if(!(gameDA.getGame(gameID).whiteUsername() == null)) {
                throw new DataAccessException("Error: already taken");
            }
        } else if (color.equals("BLACK")){
            if(!(gameDA.getGame(gameID).blackUsername() == null)) {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            throw new DataAccessException("Error: bad request");
        }
        GameData game = gameDA.getGame(gameID);
        GameData newGame;
        if(color.equals("WHITE")){
            newGame = new GameData(gameID, authDA.getAuth(authToken).userName(), game.blackUsername(), game.gameName(), game.game());
        }
        else{
            newGame = new GameData(gameID, game.whiteUsername(), authDA.getAuth(authToken).userName(), game.gameName(), game.game());
        }
        gameDA.updateGame(newGame);
        return newGame;
    }

    public void resign(String authToken, int gameID) throws DataAccessException{
        GameData temp = getGame(gameID);
        String name = getName(authToken);
        String bUsername = temp.blackUsername();
        String wUsername = temp.whiteUsername();
        int newRes = 0;
        if (name.equals(bUsername)) {
            newRes = 1;
        } else if(name.equals(wUsername)) {
            newRes = 2;
        }
        temp.game().setResult(newRes);
        gameDA.updateGame(temp);
    }

    public void leaveGame(String authToken, int gameID) throws DataAccessException{
        GameData temp = getGame(gameID);
        String name = getName(authToken);
        String bUsername = temp.blackUsername();
        String wUsername = temp.whiteUsername();
        //if game is finished, leave player in the game
        if(temp.game().getResult() == 0) {
            if (name.equals(bUsername)) {
                bUsername = null;
            } else if(name.equals(wUsername)) {
                wUsername = null;
            }
            GameData newGame = new GameData(gameID, wUsername, bUsername, temp.gameName(), temp.game());
            gameDA.updateGame(newGame);
        }
    }

    public void makeMove(String authToken, GameData game) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        gameDA.updateGame(game);
    }

    public String getName(String authToken){
        try {
            return authDA.getAuth(authToken).userName();
        }catch(Exception e){
            return null;
        }
    }
    public void clear(){
        try {
            userDA.clearUsers();
            gameDA.clearGames();
            authDA.clearAuths();
        } catch (DataAccessException e){}

    }

}