package dataaccess.storage;
import dataaccess.UserDA;
import models.UserData;
import java.util.List;
import java.util.ArrayList;

public class Users implements UserDA{
    List<UserData> userList = new ArrayList<UserData>();

    public void clearUsers(){
        userList = new ArrayList<UserData>();
    }

    public void createUser(UserData user){
        userList.add(user);
    }

    public UserData getUser(String username){
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).userName().equals(username)){
                return userList.get(i);
            }
        }
        return null;
    }
}