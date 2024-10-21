import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import dataaccess.storage.*;
import models.*;
import chess.*;
import service.*;

public class ServiceTests{
    loginService lserve = new loginService();

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


}