import org.junit.jupiter.api.*;
import dataaccess.storage.*;
import models.*;
import chess.*;

public class DATests{
    Auths authList = new Auths();
    Games gameList = new Games();
    Users userList = new Users();
    @BeforeEach
    public void reset(){
        authList = new Auths();
        gameList = new Games();
        userList = new Users();
    }
    //test create user method and get user method
    @Test
    public void addGetUserTest(){
        UserData userT = new UserData("cam", "1234", "bruh@aol.com");
        userList.createUser(new UserData("cam", "1234", "bruh@aol.com"));
        Assertions.assertEquals(userT, userList.getUser("cam"));
    }
    //test create and get game functions
    @Test
    public void createGetGameTest(){

        GameData gameT = new GameData(12, "wP", "bP", "game", new ChessGame());
        gameList.createGame(new GameData(12, "wP", "bP", "game", new ChessGame()));
        Assertions.assertEquals(gameT, gameList.getGame(12));
    }
    @Test
    public void updateGameTest(){
        GameData gameT = new GameData(12, "wP", "bP", "game", new ChessGame());
        gameList.createGame(new GameData(12, "wP", "tony", "game", new ChessGame()));
        gameList.updateGame(new GameData(12, "wP", "bP", "game", new ChessGame()));
        Assertions.assertEquals(gameT, gameList.getGame(12));
    }
    @Test
    public void setGetAuthTest(){
        AuthData authT = new AuthData("cam", "1234abcd");
        authList.createAuth(new AuthData("cam", "1234abcd"));
        Assertions.assertEquals(authT, authList.getAuth("1234abcd"));
    }
    @Test
    public void deleteAuthTest(){
        authList.createAuth(new AuthData("cam", "1234abcd"));
        authList.deleteAuth("1234abcd");
        Assertions.assertEquals(null, authList.getAuth("1234abcd"));
    }
    @Test
    public void getAuthTest(){
        AuthData temp = new AuthData("cam", "1234abcd");
        authList.createAuth(temp);
        AuthData value = authList.getUserAuth("cam");
        Assertions.assertEquals(temp, value);
    }


}