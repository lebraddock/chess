package ui;


import com.google.gson.Gson;
import models.requests.*;
import models.results.RegResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade{
    private String url;
    protected static Gson gsonS = new Gson();


    public ServerFacade(String url){
        this.url = url;
    }

    public String register(String username, String password, String email){
        try {
            String authToken = "";
            String path = "/user";
            RegRequest req = new RegRequest(username, password, email);
            String body = gsonS.toJson(req);
            RegResult res = makeRequest(path, body, "POST", authToken, RegResult.class);
            authToken = res.authToken();
            System.out.println(authToken);
            return authToken;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Username or password is incorrect");
        }
        return username;
    }

    public String login(String username, String password){
        try {
            String authToken = "";
            String path = "/session";
            LogRequest req = new LogRequest(username, password);
            String body = gsonS.toJson(req);
            RegResult res = makeRequest(path, body, "POST", authToken, RegResult.class);
            authToken = res.authToken();
            System.out.println(authToken);
            return authToken;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Username or password is incorrect");
        }
        return username;
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

}