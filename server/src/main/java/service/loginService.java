package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import models.*;
import requests.logRequest;
import results.*;

import java.util.List;
import java.util.UUID;

public class loginService extends Service{

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
    public RegResult loginUser(logRequest request) throws DataAccessException{
        String username = request.username();
        String password = request.password();
        if(userDA.getUser(username) == null){
            throw new DataAccessException("User does not exist!");
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
    public List<GameData> getGames(String authToken) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Missing auth token");
        }
        return gameDA.getGames();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Missing auth token");
        }
        int gameID = (int)(Math.random() * 1000) + 1;
        return gameDA.createGame(new GameData(gameID, "", "", gameName, new ChessGame()));
    }

    public GameData updateGame(String authToken, ChessGame.TeamColor color, int gameID) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Missing auth token");
        }
        if(gameDA.getGame(gameID) == null){
            throw new DataAccessException("Game does not exists");
        }
        GameData game = gameDA.getGame(gameID);
        GameData newGame;
        if(color == ChessGame.TeamColor.WHITE){
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