package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import messages.Notification;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        ArrayList<Connection> cons = connections.get(gameID);
        if(cons == null){
            ArrayList<Connection> temp = new ArrayList<Connection>();
            temp.add(connection);
            connections.put(gameID, temp);
        }else{
            cons.add(connection);
            connections.put(gameID, cons);
        }
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcast(String excludeVisitorName, Notification notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}