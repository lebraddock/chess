package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ChessClient;
import ui.LoginREPL;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:" + Integer.toString(port);
        facade = new ServerFacade(url);
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
    void register() throws Exception {
        var authToken = facade.register("player1", "password", "p1@email.com");
        Assertions.assertTrue(authToken.length() > 10);
    }

    @Test
    void registerFail() throws Exception {
        try{
            var authToken = facade.register("player1", "password", "p1@email.com");
            authToken = facade.register("player1", "paword", "p1@ema.com");
            Assertions.assertTrue(false);
        } catch(Exception e){
            Assertions.assertTrue(true);
        }
    }
}