package dataaccess;

import chess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import models.AuthData;

public interface AuthDA{
    void clearAuths();
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);
    AuthData getUserAuth(String userName);

}
