package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static ui.EscapeSequences.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

public class GameplayREPL implements NotificationHandler{
    GameClient client;
    Gson gsonS = new Gson();
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    WebsocketConnector ws;

    public GameplayREPL(String url, String authToken, int gameID, ChessGame.TeamColor color)throws Exception{
        ws = new WebsocketConnector(url, this);
        ws.joinGamePlayer(authToken, gameID, color);
        this.client = new GameClient(ws, color, authToken, gameID);
    }
    public void gameREPL(){
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("Exiting Game...")){
            client.printHeader("Enter option: (Press 1 for help)");
            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            out.print("[IN GAME]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!isValidInt(lineS[0], 4)) {
                out.println("Error: Not a valid input");
            } else {
                result = client.evaluateInput(Integer.parseInt(lineS[0]));
            }
        }
    }

    public void viewREPL(){
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("Exiting Game...")){
            client.printHeader("Enter option: (Press 1 for help)");
            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            out.print("[IN GAME]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!isValidInt(lineS[0], 4)) {
                out.println("Error: Not a valid input");
            } else {
                result = client.evaluateViewInput(Integer.parseInt(lineS[0]));
            }
        }
    }

    private boolean isValidInt(String value, int x){
        if(value.isEmpty()){
            return false;
        }else if(!value.matches("-?\\d+")){
            return false;
        }
        int num = Integer.parseInt(value);
        if(num >= 1 && num <= x){
            return true;
        }
        return false;

    }

    public void handleMessage(String message){
        ServerMessage serverMessage = gsonS.fromJson(message, ServerMessage.class);
        switch(serverMessage.getServerMessageType()){
            case LOAD_GAME -> loadGame(message);
            case NOTIFICATION -> sendNotify(message);
        }
    }

    public void sendNotify(String message) {
        messages.Notification notification = gsonS.fromJson(message, messages.Notification.class);
        System.out.println(notification.getMessage());
    }

    public void loadGame(String message){
        try {
            ChessGame game = gsonS.fromJson(message, LoadGameMessage.class).getGame();
            System.out.println("test     test");
            TimeUnit.SECONDS.sleep(2);
            client.updateGame(game);
        }catch(Exception e){
            System.out.println("Could not update game");
        }
    }

}
