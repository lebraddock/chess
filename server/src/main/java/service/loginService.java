package service;

import dataaccess.DataAccessException;
import models.*;
import java.util.UUID;

public class loginService extends Service{

    public UserData registerUser(UserData user) throws DataAccessException{
        if(userDA.getUser(user.userName()) != null){
            throw new DataAccessException("Username already taken");
        }
        userDA.createUser(user);
        String authToken = UUID.randomUUID().toString();
        authDA.createAuth(new AuthData(user.userName(), authToken));
        return user;

    public UserData createUser(UserData userData){
        try {
            userDA.createUser(userData);
            return userData;
        }
        catch (Exception DataAccessException){
            return null;
        }
    }
}