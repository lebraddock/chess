package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.LoginService;
import websocket.messages.Action;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class WebSocketHandler{
    protected static Gson gsonS = new Gson();
    private final ConnectionManager connections = new ConnectionManager();
    private final LoginService gameService = new LoginService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        Action action = new Gson().fromJson(message, Action.class);
        switch (action.type()) {
            case JOIN_GAME -> joinGame(session, message);
        }
    }

    public void joinGame(Session session, String message){
        connections.add()
    }
}
