package dataaccess;

import chess.*;
import java.util.List;
import models.AuthData;

public interface AuthDA{
    void clearAuths();
    void createAuth(String userName) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);

}
