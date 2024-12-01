package websocket.messages;

import com.google.gson.Gson;

public record Action(Type type, String authToken, int gameID) {

    public enum Type {
        JOIN_GAME,
        OBSERVE_GAME,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public String toString() {
        return new Gson().toJson(this);
    }


}