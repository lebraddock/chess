package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import messages.Notification;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> connections = new ConcurrentHashMap<>();
    Gson gsonS = new Gson();

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

    public void remove(int gameID, String authToken) {
        ArrayList<Connection> temp = connections.get(gameID);
        for(int i = 0; i < temp.size(); i++){
            if(Objects.equals(temp.get(i).getAuthToken(), authToken)){
                temp.remove(i);
            }
        }
        temp.removeIf(conn -> Objects.equals(conn.authToken, authToken));
    }


    public void broadcast(int gameID, Session excludeUser, String notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.get(gameID)) {
            if (c.session.isOpen()) {
                if (c.getSession() != excludeUser) {
                    c.send(notification);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        /*for (var c : removeList) {
            ArrayList<Connection> temp = connections.get(gameID);
            temp.remove(c);
            connections.put(gameID, temp);
        }*/
        for (var c : removeList) {
            connections.get(gameID).remove(c);
        }
    }

    public void sendMessage(Session session, String message){
        try {
            session.getRemote().sendString(message);
        }catch(Exception e){
            System.out.println("Session Invalid");
        }
    }
}