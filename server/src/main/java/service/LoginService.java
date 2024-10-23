package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.results.GameResult;
import dataaccess.results.RegResult;
import models.*;

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
        if(!userTemp.password().equals(password)){
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
    public void clear(){
        userDA.clearUsers();
        gameDA.clearGames();
        authDA.clearAuths();
    }

}