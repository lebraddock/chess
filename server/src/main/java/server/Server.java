package server;

import server.websocket.WebSocketHandler;
import spark.*;
import server.handler.Handler;
public class Server {

    private final WebSocketHandler webSocketHandler = new WebSocketHandler();;


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        Spark.webSocket("/ws", webSocketHandler);

        Spark.post("/user", Handler::register);
        Spark.delete("/db", Handler::clear);
        Spark.post("/session", Handler::login);
        Spark.delete("/session", Handler::logout);
        Spark.post("/game", Handler::createGame);
        Spark.get("/game", Handler::listGames);
        Spark.put("/game", Handler::joinGame);

        // Register your endpoints and handle exceptions here.
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
