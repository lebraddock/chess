package sqltests;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.results.RegResult;
import dataaccess.sql.SQLAuthDA;
import dataaccess.sql.SQLGameDA;
import dataaccess.sql.SQLUserDA;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SQLTests{
    SQLUserDA userDA = new SQLUserDA();
    SQLAuthDA authDA = new SQLAuthDA();
    SQLGameDA gameDA = new SQLGameDA();
    UserData user1 = new UserData("Cameron", "1234", "cam@gmail.com");
    GameData game1 = new GameData(1234, "Cameron", "Paul", "game", new ChessGame());
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
}