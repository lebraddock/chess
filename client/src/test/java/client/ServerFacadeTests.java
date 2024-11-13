package client;

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
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + Integer.toString(port);
        facade = new ServerFacade(url);
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
    void registerFail() throws Exception {
        var authToken = facade.register("player1", "password", "p1@email.com");
        var authToken2 = facade.register("player1", "paword", "p1@ema.com");
        Assertions.assertNull(authToken2);
    }

    @Test
    void loginTest() throws Exception{

        var authToken = facade.login("cam", "1234");
        Assertions.assertNotNull(authToken);
    }

    @Test
    void loginFail() throws Exception{
        var authToken = facade.login("cam", "12345");
        Assertions.assertNull(authToken);
    }

    @Test
    public void createGameTest() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int result = facade.createGame(req, authToken);
        Assertions.assertNotEquals(result, 0);
    }

    @Test
    public void createGameFail() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int result = facade.createGame(req, "a12a12a12a");
        Assertions.assertEquals(result, 0);
    }

    @Test
    public void listGamesTests() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int res = facade.createGame(req, authToken);

        Map<String, List<GameResult>> result = facade.listGames(authToken);
        Assertions.assertEquals(ArrayList.class, result.get("games").getClass());
    }

    @Test
    public void listGamesFail() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int res = facade.createGame(req, authToken);

        Map<String, List<GameResult>> result = facade.listGames("wow");
        Assertions.assertEquals(null, result);
    }

    @Test
    public void joinGameTest() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int res = facade.createGame(req, authToken);
        JoinGameRequest gr = new JoinGameRequest("white", res);
        facade.joinGame(gr, authToken);
    }

    @Test
    public void joinGameFail() throws Exception{
        CreateGameRequest req = new CreateGameRequest("game!");
        int res = facade.createGame(req, authToken);
        JoinGameRequest gr = new JoinGameRequest("white", 12);
        facade.joinGame(gr, "a");
    }


}