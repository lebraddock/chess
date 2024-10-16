package service;

import dataaccess.*;

public class ClearService extends Service{
    public void clear(){
        UserDA.clearUsers();
        GameDA.clearGames();
        AuthDA.clearAuths();
    }

}