package ui;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.GameData;
import models.requests.*;
import models.results.GameResult;
import models.results.RegResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerFacade{
    private String url;
    protected static Gson gsonS = new Gson();


    public ServerFacade(String url){
        this.url = url;
    }

    public String clear(){
        try {
            String authToken = "";
            String path = "/db";
            makeRequest(path, "", "DELETE", authToken, Map.class);
            return authToken;
        } catch (Exception e) {
            System.out.println("ERROR");
        }
        return "";
    }

    public String register(String username, String password, String email){
        try {
            String authToken = "";
            String path = "/user";
            RegRequest req = new RegRequest(username, password, email);
            String body = gsonS.toJson(req);
            RegResult res = makeRequest(path, body, "POST", authToken, RegResult.class);
            authToken = res.authToken();
            return authToken;
        } catch (Exception e) {
            System.out.println("Username already taken");
        }
        return null;
    }

    public String login(String username, String password){
        try {
            String authToken = "";
            String path = "/session";
            LogRequest req = new LogRequest(username, password);
            String body = gsonS.toJson(req);
            RegResult res = makeRequest(path, body, "POST", authToken, RegResult.class);
            authToken = res.authToken();
            return authToken;
        } catch (Exception e) {
            System.out.println("Username or password is incorrect");
        }
        return null;
    }

    public Map<String, List<GameResult>> listGames(String authToken){
        try {
            String path = "/game";
            Type temp = new TypeToken<Map<String, List<GameResult>>>() {}.getType();
            Map<String, List<GameResult>> res = makeRequest(path, "", "GET", authToken, temp);
            return res;
        } catch (Exception e) {
            System.out.println("No Games to List");
        }
        return null;
    }

    public int createGame(CreateGameRequest req, String authToken){
        try {
            String path = "/game";
            String body = gsonS.toJson(req);
            Type temp = new TypeToken<Map<String, Integer>>() {}.getType();
            Map<String, Integer> res = makeRequest(path, body, "POST", authToken, temp);
            return res.get("gameID");
        } catch (Exception e) {
            System.out.println("Not Logged In");
        }
        return 0;
    }

    public void joinGame(JoinGameRequest req, String authToken){
        try {
            String path = "/game";
            String body = gsonS.toJson(req);
            Map<String, Integer> res = makeRequest(path, body, "PUT", authToken, null);
        } catch (Exception e) {
            System.out.println("Error Joining Game");
        }
    }


    private <T> T makeRequest(String path, String body, String method, String authToken, Class<T> responseClass){
        try{
            URL reqUrl = (new URI(url + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) reqUrl.openConnection();
            http.setRequestMethod(method);
            if (!authToken.isEmpty()) {
                http.addRequestProperty("authorization", authToken);
            }
            writeRequestBody(body, http);
            http.connect();
            return readResponseBody(http, responseClass);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private <T> T makeRequest(String path, String body, String method, String authToken, Type responseType){
        try{
            URL reqUrl = (new URI(url + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) reqUrl.openConnection();
            http.setRequestMethod(method);
            if (!authToken.isEmpty()) {
                http.addRequestProperty("authorization", authToken);
            }
            writeRequestBody(body, http);
            http.connect();
            return readResponseBody(http, responseType);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static <T> T readResponseBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T responseBody = null;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, responseClass);
        }
        return responseBody;
    }

    private static <T> T readResponseBody(HttpURLConnection http, Type responseType) throws IOException {
        T responseBody = null;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, responseType);
        }
        return responseBody;
    }

}