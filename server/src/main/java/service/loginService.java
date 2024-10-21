package service;

import dataaccess.DataAccessException;
import models.*;
import java.util.UUID;

public class loginService extends Service{

    public UserData registerUser(UserData user) throws DataAccessException {
        if (userDA.getUser(user.userName()) != null) {
            throw new DataAccessException("Username already taken");
        }
        userDA.createUser(user);
        String authToken = UUID.randomUUID().toString();
        authDA.createAuth(new AuthData(user.userName(), authToken));
        return user;
    }
    public UserData loginUser(String username, String password) throws DataAccessException{
        if(userDA.getUser(username) == null){
            throw new DataAccessException("User does not exist!");
        }
        UserData userTemp = userDA.getUser(username);
        if(!userTemp.password().equals(password)){
            throw new DataAccessException("Password incorrect!");
        }
        String authToken = UUID.randomUUID().toString();
        authDA.createAuth(new AuthData(username, authToken));
        return userTemp;
    }
    public void logout(String authToken) throws DataAccessException{
        if(authDA.getAuth(authToken) == null){
            throw new DataAccessException("Missing auth token");
        }
        authDA.deleteAuth(authToken);
    }
}