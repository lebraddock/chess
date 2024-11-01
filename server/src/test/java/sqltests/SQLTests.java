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
        userDA.getUser("Cameron");
    }

    @Test
    void testCreateGame() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
    }

    @Test
    void testGetGame() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        GameData t1 = gameDA.getGame(r.gameID());
    }

    @Test
    void testGetAllGames() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        GameData r2 = gameDA.createGame(game2);
        List<GameResult> gameResults = gameDA.getGames();
        for(int i = 0; i < gameResults.size(); i++){
            System.out.println(gameResults.get(i).gameName());
        }
    }

    @Test
    void testUpdateGame() throws DataAccessException{
        GameData r = gameDA.createGame(game1);
        GameData game3 = new GameData(1234, "Tony", "Steven", "game", new ChessGame());
        gameDA.updateGame(game3);
    }

    @Test
    void testCreateAuth() throws DataAccessException{
        AuthData temp = new AuthData("cam","12341243");
        authDA.createAuth(temp);
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