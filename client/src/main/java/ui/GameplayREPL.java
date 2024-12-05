package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
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
    boolean leave = false;

    public GameplayREPL(String url, String authToken, int gameID, ChessGame.TeamColor color)throws Exception{
        ws = new WebsocketConnector(url, this);
        if(color == null){
            ws.joinGameObserver(authToken, gameID);
            this.client = new GameClient(ws, color, authToken, gameID);
        }else {
            ws.joinGameObserver(authToken, gameID);
            TimeUnit.MILLISECONDS.sleep(250);
            ws.joinGamePlayer(authToken, gameID, color);
            this.client = new GameClient(ws, color, authToken, gameID);
        }
    }
    public void gameREPL(){
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("Exiting Game...")){
            PrintFunctions.printHeader("Enter option: (Press 1 for help)");
            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            out.print("[IN GAME]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!PrintFunctions.isValidInt(lineS[0], 6)) {
                out.println("Error: Not a valid input");
            } else {
                result = client.evaluateInput(Integer.parseInt(lineS[0]));
            }
        }
        ws.closeSession(client.getAuth(), client.getGameID());
    }

    public void viewREPL(){
        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("Exiting Game...")){
            PrintFunctions.printHeader("Enter option: (Press 1 for help)");
            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            out.print("[IN GAME]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!PrintFunctions.isValidInt(lineS[0], 4)) {
                out.println("Error: Not a valid input");
            } else {
                result = client.evaluateViewInput(Integer.parseInt(lineS[0]));
            }
        }
    }


    public void handleMessage(String message){
        ServerMessage serverMessage = gsonS.fromJson(message, ServerMessage.class);
        switch(serverMessage.getServerMessageType()){
            case LOAD_GAME -> loadGame(message);
            case NOTIFICATION -> sendNotify(message);
            case ERROR -> doError(message);
        }
    }

    public void doError(String message){
        ErrorMessage notification = gsonS.fromJson(message, ErrorMessage.class);
        System.out.println("");
        System.out.println(notification.getMessage());
        System.out.print("[In GAME]>>> ");
    }

    public void sendNotify(String message) {
        messages.Notification notification = gsonS.fromJson(message, messages.Notification.class);
        System.out.println("");
        System.out.println(notification.getMessage());
        System.out.print("[In GAME]>>> ");
    }

    public void loadGame(String message){
        try {
            ChessGame game = gsonS.fromJson(message, LoadGameMessage.class).getGame();
            TimeUnit.SECONDS.sleep(1);
            if(game.getResult() != 0) {
                client.setGameFinished(true);
                client.printResult();
            }
            client.updateGame(game);
            System.out.println("");
            client.printBoard();
            System.out.print("[IN GAME]>>> ");
        }catch(Exception e){
            System.out.println("Could not update game");
        }
    }


}
