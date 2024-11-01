package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDA;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;
import dataaccess.DatabaseManager;

import java.sql.ResultSet;

public class SQLUserDA implements UserDA{

    public void createUser(UserData user) throws DataAccessException {
        String name = user.username();
        String password = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        String email = user.email();
        String statement;
        try(var conn = DatabaseManager.getConnection()){
            if(name.matches("^[^;]*$") && email.matches("^[^;]*$")){
                statement = "INSERT INTO users (username, password, email) VALUES(?, ?, ?)";
                try (var prepStatement = conn.prepareStatement(statement)) {
                    prepStatement.setString(1, name);
                    prepStatement.setString(2, password);
                    prepStatement.setString(3, email);
                    prepStatement.executeUpdate();
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT username, password, email from users WHERE username = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, username);
                try(ResultSet result = prepStatement.executeQuery()){
                    if(result.next()){
                        String password = result.getString("password");
                        String email = result.getString("email");
                        return new UserData(username, password, email);
                    }
                    return null;
                }
            }

        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clearUsers() throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "TRUNCATE users";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}