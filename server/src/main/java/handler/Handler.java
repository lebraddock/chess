package handler;

import dataaccess.DataAccessException;
import requests.logRequest;
import service.*;
import com.google.gson.Gson;
import spark.*;
import results.*;
import models.UserData;

import java.util.Map;

public class Handler{
    protected static Gson gsonS = new Gson();
    static loginService service = new loginService();

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
        logRequest request = getBody(req, logRequest.class);
        RegResult response;
        try{
            response = service.loginUser(request);
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
            return gsonS.toJson("{}");
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


}