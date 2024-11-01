package sqltests;

import dataaccess.*;
import dataaccess.sql.SQLUserDA;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SQLTests{
    SQLUserDA userDA = new SQLUserDA();
    UserData user1 = new UserData("Cameron", "1234", "cam@gmail.com");
    @BeforeEach
    void resetDataaccess() {
        userDA = new SQLUserDA();
    }

    @Test
    void createUserTest() {
        String result;
        try {
            userDA.createUser(user1);
            result = userDA.getUser("Cameron").email();
        } catch (DataAccessException e) {
            result = "hahahaha";
        }
        Assertions.assertEquals(result, user1.email());
    }
}