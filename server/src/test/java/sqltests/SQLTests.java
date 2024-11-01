package sqltests;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.results.GameResult;
import dataaccess.results.RegResult;
import dataaccess.sql.SQLAuthDA;
import dataaccess.sql.SQLGameDA;
import dataaccess.sql.SQLUserDA;
import models.AuthData;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SQLTests{
    SQLUserDA userDA = new SQLUserDA();
    SQLAuthDA authDA = new SQLAuthDA();
    SQLGameDA gameDA = new SQLGameDA();
    UserData user1 = new UserData("Cameron", "1234", "cam@gmail.com");
    GameData game1 = new GameData(1234, "Cameron", "Paul", "game", new ChessGame());
    GameData game2 = new GameData(2345, "Tony", "Charlie", "game2", new ChessGame());
    DatabaseManager databaseManager = new DatabaseManager();
    @BeforeEach
    void reset(){
        try {
            userDA.clearUsers();
            authDA.clearAuths();
            gameDA.clearGames();
        } catch (DataAccessException e) {
            System.out.println("Ruh Roh");
        }
    }


    @Test
    void createUserTest() throws DataAccessException {
        userDA.createUser(user1);
        UserData tempUser = userDA.getUser("Cameron");
        Assertions.assertEquals(user1.email(), tempUser.email());
    }

    @Test
    void createUserFail() throws DataAccessException {
        userDA.createUser(user1);
        String result;
        try{
            userDA.createUser(user1);
            result = "x";
        } catch (DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }

    @Test
    void testCreateGame() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        Assertions.assertEquals(r.gameID(), 1234);
    }

    @Test
    void createGameFail() throws DataAccessException{
        gameDA.createGame(game1);
        String result;
        try{
            gameDA.createGame(game1);
            result = "x";
        } catch (DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }

    @Test
    void testGetGame() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        GameData t1 = gameDA.getGame(r.gameID());
    }

    @Test
    void getGameFail() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        GameData t1 = gameDA.getGame(3);
        Assertions.assertNotEquals(r,t1);
    }

    @Test
    void testGetAllGames() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        GameData r2 = gameDA.createGame(game2);
        String x = "x";
        try {
            List<GameResult> gameResults = gameDA.getGames();
            for (int i = 0; i < gameResults.size(); i++) {
                System.out.println(gameResults.get(i).gameName());
            }
        } catch(Exception e){
            x = "o";
        }
        Assertions.assertEquals(x,"x");

    }

    @Test
    void getAllGamesFail() throws DataAccessException{
        List<GameResult> gameResults = gameDA.getGames();
        String x = "x";
        if(!gameResults.isEmpty()){
            x = "o";
        }
        Assertions.assertEquals(x,"x");
    }


    @Test
    void testUpdateGame() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        GameData game3 = new GameData(1234, "Tony", "Steven", "game", new ChessGame());
        gameDA.updateGame(game3);
        Assertions.assertEquals(gameDA.getGame(1234), game3);
    }

    @Test
    void updateGameFail() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        gameDA.updateGame(game2);
        Assertions.assertNotEquals(gameDA.getGame(1234), game2);
    }

    @Test
    void testCreateAuth() throws DataAccessException{
        AuthData temp = new AuthData("cam","12341243");
        authDA.createAuth(temp);
        Assertions.assertEquals(authDA.getAuth("12341243"), new AuthData("cam", "12341243"));
    }

    @Test
    void testGetAuth() throws DataAccessException{
        AuthData temp = new AuthData("cam","12341243");
        authDA.createAuth(temp);
        AuthData tempA = authDA.getAuth("12341243");
        Assertions.assertEquals(temp, tempA);
    }

    @Test
    void testDeleteAuth() throws DataAccessException{
        AuthData temp = new AuthData("cam","12341243");
        authDA.createAuth(temp);
        authDA.deleteAuth("12341243");
    }
}