package service;

import dataaccess.*;
import dataaccess.sql.SQLAuthDA;
import dataaccess.sql.SQLGameDA;
import dataaccess.sql.SQLUserDA;
import dataaccess.storage.*;

public class Service{
    protected UserDA userDA = new SQLUserDA();
    protected GameDA gameDA = new SQLGameDA();
    protected AuthDA authDA = new SQLAuthDA();

}