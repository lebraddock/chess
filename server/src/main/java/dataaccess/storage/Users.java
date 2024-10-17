package dataaccess.storage;
import dataaccess.UserDA;
import models.UserData;
import java.util.List;
import java.util.ArrayList;

public class Users implements UserDA{
    List<UserData> userList = new ArrayList<UserData>();
}