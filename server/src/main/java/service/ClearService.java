package service;

import dataaccess.*;

public class ClearService extends Service{
    public void clear(){
        UserDA.clear();
        GameDA.clear();
        AuthDA.clear();
    }

}