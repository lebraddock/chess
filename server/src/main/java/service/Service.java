package service;

import dataaccess.*;
import dataaccess.storage.*;

public class Service{
    protected UserDA userDA = new Users();
    protected GameDA gameDA = new Games();
    protected AuthDA authDA = new Auths();

}