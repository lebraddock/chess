import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import dataaccess.storage.*;
import models.*;
import chess.*;
import service.*;

public class ServiceTests{
    loginService lserve = new loginService();
    @BeforeEach
    public void clearAllService(){
        lserve = new loginService();
    }
    //test create user method and get user method
    @Test
    void testCreateLogin() throws DataAccessException {
        UserData user = new UserData("cam", "1234", "paul@aol.com");
        lserve.registerUser(user);
        String loginName = "cam";
        String pw = "1234";
        UserData userTemp = lserve.loginUser(loginName, pw);
        Assertions.assertEquals(userTemp, user);
    }
    @Test
    void testCreateJoinGame() throws DataAccessException {
        UserData user = new UserData("cam", "1234", "paul@aol.com");
        UserData user2 = new UserData("paul", "2345", "cam@aol.com");
        lserve.registerUser(user);
        lserve.registerUser(user2);
        String at = lserve.getAuthToken("cam");
        GameData gameT = lserve.createGame(at, "fun game");
        gameT = lserve.updateGame(at, ChessGame.TeamColor.WHITE, gameT.gameID());
        at = lserve.getAuthToken(user2.userName());
        gameT = lserve.updateGame(at, ChessGame.TeamColor.BLACK, gameT.gameID());
        GameData gameC = new GameData(gameT.gameID(), "cam", "paul", "fun game", new ChessGame());
        Assertions.assertEquals(gameT, gameC);
    }
    @Test
    void testClearSevice() throws DataAccessException{
        UserData user = new UserData("cam", "1234", "paul@aol.com");
        UserData user2 = new UserData("paul", "2345", "cam@aol.com");
        lserve.registerUser(user);
        lserve.registerUser(user2);

    }


}