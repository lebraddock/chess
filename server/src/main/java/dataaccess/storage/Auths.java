package dataaccess.storage;
import dataaccess.AuthDA;
import models.AuthData;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Auths implements AuthDA{
    List<AuthData> authList = new ArrayList<AuthData>();
    public void clearAuths(){
        authList = new ArrayList<AuthData>();
    }

    public void createAuth(AuthData auth){
        authList.add(auth);
    }

    public AuthData getAuth(String authToken){
        for(int i = 0; i < authList.size(); i++){
            if(authList.get(i).authToken().equals(authToken)){
                return authList.get(i);
            }
        }
        return null;
    }

    public void deleteAuth(String authToken){
        for(int i = 0; i < authList.size(); i++){
            if(authList.get(i).authToken().equals(authToken)){
                authList.remove(i);
                break;
            }
        }
    }

    public AuthData getUserAuth(String username){
        for(int i = 0; i < authList.size(); i++){
            if(authList.get(i).userName().equals(username)){
                return authList.get(i);
            }
        }
        return null;
    }
}