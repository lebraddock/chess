package service;

import dataaccess.*;

public class ClearService extends Service{
    public void clear(){
        userDA.clearUsers();
        gameDA.clearGames();
        authDA.clearAuths();
    }
}