package dataaccess.sql;

import dataaccess.AuthDA;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import models.AuthData;
import models.UserData;

import java.sql.ResultSet;

public class SQLAuthDA implements AuthDA {

    public void createAuth(AuthData auth) throws DataAccessException{
        String authToken = auth.authToken();
        String username = auth.userName();
        String statement;
        try(var conn = DatabaseManager.getConnection()){
            if(username.matches("^[^;]*$")){
                statement = "INSERT INTO auths (username, authToken) VALUES(?, ?)";
                try (var prepStatement = conn.prepareStatement(statement)) {
                    prepStatement.setString(1, username);
                    prepStatement.setString(2, authToken);
                    prepStatement.executeUpdate();
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT username, authToken from auths WHERE authToken = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, authToken);
                try(ResultSet result = prepStatement.executeQuery()){
                    if(result.next()){
                        String username = result.getString("username");
                        authToken = result.getString("authToken");
                        return new AuthData(username, authToken);
                    }
                    return null;
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "DELETE username, authToken from auths WHERE authToken = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1,authToken);
                prepStatement.executeUpdate();
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getUserAuth(String username) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT username, authToken from auths WHERE username = ?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, username);
                try(ResultSet result = prepStatement.executeQuery()){
                    if(result.next()){
                        username = result.getString("username");
                        String authToken = result.getString("authToken");
                        return new AuthData(username, authToken);
                    }
                    return null;
                }
            }
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clearAuths() throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "TRUNCATE auths";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}