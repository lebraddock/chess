package client;

import dataaccess.DataAccessException;
import models.GameData;
import models.requests.CreateGameRequest;
import models.requests.JoinGameRequest;
import models.results.GameResult;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ChessClient;
import ui.LoginREPL;
import ui.ServerFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String authToken;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + Integer.toString(port);
        facade = new ServerFacade(url);
        facade.clear();
        authToken = facade.register("cam", "1234", "cam@email.com");
    }
    @BeforeEach
    public void clearServerPre() throws Exception{
        facade.clear();
        authToken = facade.register("cam", "1234", "cam@email.com");
    }

    @AfterEach
    public void clearServer(){
        facade.clear();
    }
    @AfterAll
    static void stopServer() {
        server.stop();

    }



    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void clearTest() throws Exception{
        facade.clear();
    }

    @Test
    void register() throws Exception {
        var authToken = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authToken.length() > 10);
    }

    @Test
    void registerFail() {
        try {
            var authToken = facade.register("player1", "password", "p1@email.com");
            var authToken2 = facade.register("player1", "paword", "p1@ema.com");
            Assertions.assertTrue(false);
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void loginTest() throws Exception{
        var authToken = facade.login("cam", "1234");
        Assertions.assertNotNull(authToken);
    }

    @Test
    void loginFail(){
        try {
            var authToken = facade.login("cam", "12345");
            Assertions.assertTrue(false);
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void createGameTest() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int result = facade.createGame(req, authToken);
        Assertions.assertNotEquals(result, 0);
    }

    @Test
    public void createGameFail(){
        try {
            CreateGameRequest req = new CreateGameRequest("game!");
            int result = facade.createGame(req, "a12a12a12a");
            Assertions.assertTrue(false);
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void listGamesTests() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int res = facade.createGame(req, authToken);

        Map<String, List<GameResult>> result = facade.listGames(authToken);
        Assertions.assertEquals(ArrayList.class, result.get("games").getClass());
    }

    @Test
    public void listGamesFail(){
        try {
            CreateGameRequest req = new CreateGameRequest("game!");
            int res = facade.createGame(req, authToken);

            Map<String, List<GameResult>> result = facade.listGames("wow");
            Assertions.assertTrue(false);
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void joinGameTest() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int res = facade.createGame(req, authToken);
        JoinGameRequest gr = new JoinGameRequest("WHITE", res);
        facade.joinGame(gr, authToken);
    }

    @Test
    public void joinGameFail(){
        try {
            CreateGameRequest req = new CreateGameRequest("game!");
            int res = facade.createGame(req, authToken);
            JoinGameRequest gr = new JoinGameRequest("white", 12);
            facade.joinGame(gr, "a");
            Assertions.assertTrue(false);
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void logoutTest() throws Exception {
        facade.logout(authToken);
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutFail(){
        try {
            facade.logout("12");
            Assertions.assertTrue(false);
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }


}