package service;

import dataaccess.DataAccessException;
import dataaccess.results.RegResult;
import models.GameData;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DATests{
    LoginService service = new LoginService();
    UserData user1;
    UserData user2;
    @BeforeEach
    void b4() {
        service = new LoginService();
        user1 = new UserData("Jon", "1234", "jon@aol.com");
        user2 = new UserData("Tom", "5678", "tom@aol.com");
    }


    @Test
    void registerUserTest(){
        RegResult result;
        try{
            result = service.registerUser(user1);
        } catch (DataAccessException e){
            result = new RegResult("","");
        }
        Assertions.assertEquals(result.username(), user1.username());
    }
    @Test
    void registerFailTest(){
        String result;
        try{
           service.registerUser(new UserData(null, "1234", "asd"));
            result = "x";
        } catch (DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }


    @Test
    void loginTest(){
        String result;
        try{
            service.registerUser(user1);
            service.loginUser(user1.username(), user1.password());
            result = "x";
        } catch (DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "x");
    }
    @Test
    void loginFail(){
        String result;
        try{
            service.registerUser(user1);
            service.loginUser(user1.username(), "11");
            result = "x";
        } catch (DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }


    @Test
    void logout(){
        String at;
        String result;
        try{
            RegResult r = service.registerUser(user1);
            service.logout(r.authToken());
            result = "x";
        } catch(DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "x");
    }
    @Test
    void logoutFail(){
        String result;
        try{
            RegResult r = service.registerUser(user1);
            service.logout("1111");
            result = "x";
        } catch(DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }


    @Test
    void createGame() throws DataAccessException{
        service.clear();
        RegResult r = service.registerUser(user1);
        service.createGame(r.authToken(), "game");
    }
    @Test
    void createGameFail(){
        String result;
        try{
            RegResult r = service.registerUser(user1);
            service.createGame("11", "game");
            result = "x";
        } catch(DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }


    @Test
    void joinGame(){
        String result;
        try{
            RegResult r = service.registerUser(user1);
            GameData g = service.createGame(r.authToken(), "game");
            service.updateGame(r.authToken(), "WHITE", g.gameID());
            result = "x";
        } catch(DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "x");
    }
    @Test
    void joinGameFail(){
        String result;
        try{
            RegResult r = service.registerUser(user1);
            GameData g = service.createGame(r.authToken(), "game");
            service.updateGame(r.authToken(), "WHIE", g.gameID());
            result = "x";
        } catch(DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }

    @Test
    void getAllGames(){
        String result;
        try{
            RegResult r = service.registerUser(user1);
            GameData g = service.createGame(r.authToken(), "game");
            GameData g2 = service.createGame(r.authToken(), "game2");
            service.getGames(r.authToken());
            result = "x";
        } catch(DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "x");
    }
    @Test
    void getAllGamesFail(){
        String result;
        try{
            RegResult r = service.registerUser(user1);
            GameData g = service.createGame(r.authToken(), "game");
            GameData g2 = service.createGame(r.authToken(), "game2");
            service.getGames("12");
            result = "x";
        } catch(DataAccessException e){
            result = "o";
        }
        Assertions.assertEquals(result, "o");
    }

    @Test
    void clearAll(){
        RegResult r;
        try{
            r = service.registerUser(user1);
            GameData g = service.createGame(r.authToken(), "game");
            GameData g2 = service.createGame(r.authToken(), "game2");
            service.clear();
            //i should be able to add same user now
            r = service.registerUser(user1);
        } catch(DataAccessException e){
            r = new RegResult("","");
        }
        Assertions.assertEquals(r.username(), user1.username());
    }

}