package dataaccess;

import chess.*;
import java.util.List;
import models.UserData;

public interface UserDA{
    void clearUsers() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String userName) throws DataAccessException;
}
