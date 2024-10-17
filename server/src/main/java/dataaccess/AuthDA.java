package dataaccess;

import chess.*;
import java.util.List;
import models.AuthData;

public interface AuthDA{
    void clearAuths();
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);

}
