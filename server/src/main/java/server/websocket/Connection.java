package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;

public class Connection {
    public String authToken;
    public Session session;

    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public String getAuthToken(){
        return authToken;
    }

    public Session getSession(){
        return session;
    }
}
