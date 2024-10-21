package service;

import models.*;

public class authService extends Service{

    public AuthData findAuth(String authToken){
        try{
            return authDA.getAuth();
        }
        catch (Exception DataAccessException){
            return null;
        }
    }
}