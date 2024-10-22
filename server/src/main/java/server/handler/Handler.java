package server.handler;

import dataaccess.results.GameResult;
import dataaccess.results.RegResult;
import models.GameData;
import dataaccess.requests.CreateGameRequest;
import dataaccess.requests.JoinGameRequest;
import dataaccess.requests.LogRequest;
import service.*;
import com.google.gson.Gson;
import spark.*;
import models.UserData;

import java.util.List;
import java.util.Map;

public class Handler{
    protected static Gson gsonS = new Gson();
    static LoginService service = new LoginService();

    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }


    public static Object register(Request req, Response res){
        UserData userInfo = getBody(req, UserData.class);
        RegResult userRes;
        Map<String, String> errorMes;
        try{
            userRes = service.registerUser(userInfo);
            res.status(200);
            return gsonS.toJson(userRes);
        }catch(Exception e){
            //set error message and codes!
            String mes = e.getMessage();
            errorMes = Map.of("message", mes);
            if(mes.equals("Error: already taken")){
                res.status(403);
            }else if(mes.equals("Error: bad request")){
                res.status(400);
            }else{
                res.status(500);
            }
        }
        return gsonS.toJson(errorMes);
    }

    public static Object clear(Request req, Response res){
        Map<String, String> response;
        try{
            service.clear();
            return "{}";
        } catch (Exception e){
            res.status(500);
            response = Map.of("message", e.getMessage());
        }
        return gsonS.toJson(response);
    }

    public static Object login(Request req, Response res){
        Map<String, String> errorMes;
        LogRequest request = getBody(req, LogRequest.class);
        RegResult response;
        String username = request.username();
        String password = request.password();
        try{
            response = service.loginUser(username, password);
            res.status(200);
            return gsonS.toJson(response);
        } catch (Exception e){
            String mes = e.getMessage();
            errorMes = Map.of("message", mes);
            if(mes.equals("Error: unauthorized")){
                res.status(401);
            }else{
                res.status(500);
            }
        }
        return gsonS.toJson(errorMes);
    }

    public static Object logout(Request req, Response res){
        String authToken = req.headers("authorization");
        Map<String, String> errorMes;
        try{
            service.logout(authToken);
            res.status(200);
            return gsonS.toJson(null);
        } catch (Exception e){
            String mes = e.getMessage();
            errorMes = Map.of("message", mes);
            if(mes.equals("Error: unauthorized")){
                res.status(401);
            } else {
                res.status(500);
            }
        }
        return gsonS.toJson(errorMes);
    }

    public static Object createGame(Request req, Response res){
        CreateGameRequest gameName;
        String authToken = req.headers("authorization");
        gameName = getBody(req, CreateGameRequest.class);
        Map<String, String> response;
        try{
            GameData gd = service.createGame(authToken, gameName.gameName());
            res.status(200);
            response = Map.of("gameID", Integer.toString(gd.gameID()));
            return gsonS.toJson(response);
        } catch (Exception e){
            String mes = e.getMessage();
            response = Map.of("message", mes);
            if(mes.equals("Error: bad request")){
                res.status(400);
            }else if(mes.equals("Error: unauthorized")){
                res.status(401);
            }else{
                res.status(500);
            }
        }
        return gsonS.toJson(response);
    }

    public static Object listGames(Request req, Response res){
        String authToken = req.headers("authorization");
        Map<String, String> errorMes;
        Map<String, List<GameResult>> response;
        try{
            List<GameResult> gameList = service.getGames(authToken);
            response = Map.of("games", gameList);
            return gsonS.toJson(response);
        } catch (Exception e){
            String mes = e.getMessage();
            errorMes = Map.of("message", mes);
            if(mes.equals("Error: unauthorized")){
                res.status(401);
            } else {
                res.status(500);
            }
        }
        return gsonS.toJson(errorMes);
    }

    public static Object joinGame(Request req, Response res){
        String authToken = req.headers("authorization");
        JoinGameRequest gReq = getBody(req, JoinGameRequest.class);
        System.out.println(gReq.playerColor());
        Map <String, String> errorMes;
        try {
            service.updateGame(authToken, gReq.playerColor(), gReq.gameID());
            return gsonS.toJson(null);
        }catch (Exception e){
            String mes = e.getMessage();
            errorMes = Map.of("message", mes);
            if(mes.equals("Error: unauthorized")){
                res.status(401);
            } else if(mes.equals("Error: bad request")) {
                res.status(400);
            } else if(mes.equals("Error: already taken")){
                res.status(403);
            } else {
                res.status(500);
            }
        }
        return gsonS.toJson(errorMes);
    }


}