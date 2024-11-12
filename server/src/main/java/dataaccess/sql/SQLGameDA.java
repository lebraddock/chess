package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDA;
import models.results.GameResult;
import models.GameData;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDA implements GameDA{
    protected static Gson gsonS = new Gson();

    public GameData createGame(GameData game) throws DataAccessException{
        int gameID = game.gameID();
        String gameName = game.gameName();
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        String chessGameS = gsonS.toJson(game.game());
        String statement;
        try(var conn = DatabaseManager.getConnection()){
            if(gameName.matches("^[^;]*$")){
                statement = "INSERT INTO games (gameID, gameName, whiteUsername, blackUsername, chessGame) VALUES(?, ?, ?, ?, ?)";
                try (var prepStatement = conn.prepareStatement(statement)) {
                    prepStatement.setInt(1, gameID);
                    prepStatement.setString(2, gameName);
                    prepStatement.setString(3, whiteUsername);
                    prepStatement.setString(4, blackUsername);
                    prepStatement.setString(5, chessGameS);
                    prepStatement.executeUpdate();
                    return game;
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public void updateGame(GameData game) throws DataAccessException{
        int gameID = game.gameID();
        String gameName = game.gameName();
        String whiteUsername = game.whiteUsername();
        String blackUsername = game.blackUsername();
        String chessGameS = gsonS.toJson(game.game());
        String statement;
        try(var conn = DatabaseManager.getConnection()){
            if(gameName.matches("^[^;]*$")){
                statement = "UPDATE games SET gameName = ?, whiteUsername = ?, blackUsername = ?, chessGame = ? WHERE gameID = ?";
                try (var prepStatement = conn.prepareStatement(statement)) {

                    prepStatement.setString(1, gameName);
                    prepStatement.setString(2, whiteUsername);
                    prepStatement.setString(3, blackUsername);
                    prepStatement.setString(4, chessGameS);
                    prepStatement.setInt(5, gameID);
                    prepStatement.executeUpdate();
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT gameName, whiteUsername, blackUsername, chessGame from games WHERE gameID = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setInt(1, gameID);
                try(ResultSet result = prepStatement.executeQuery()){
                    if(result.next()){
                        String gameName = result.getString("gameName");
                        String whiteUsername = result.getString("whiteUsername");
                        String blackUsername = result.getString("blackUsername");
                        String game = result.getString("chessGame");
                        ChessGame chessGame = gsonS.fromJson(game, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                    return null;
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public List<GameResult> getGames() throws DataAccessException {
        List<GameResult> results = new ArrayList<GameResult>();
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM games";
            try(var prepStatement = conn.prepareStatement(statement)){
                try(ResultSet result = prepStatement.executeQuery()){
                    while(result.next()){
                        String gameName = result.getString("gameName");
                        String whiteUsername = result.getString("whiteUsername");
                        String blackUsername = result.getString("blackUsername");
                        ChessGame game = gsonS.fromJson(result.getString("chessGame"), ChessGame.class);
                        int gameID = result.getInt("gameID");
                        GameData temp = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                        results.add(convert(temp));
                    }
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
        return results;
    }

    public void clearGames() throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "TRUNCATE games";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private GameResult convert(GameData game){
        return new GameResult(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
    }
}