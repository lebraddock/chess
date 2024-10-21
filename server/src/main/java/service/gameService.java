package service;

import dataaccess.DataAccessException;
import models.*;
import chess.ChessGame;
import java.util.List;

public class gameService extends Service{

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
}