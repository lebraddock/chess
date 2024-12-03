package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayREPL implements NotificationHandler{
    GameClient client;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public GameplayREPL(String url, String authToken, int gameID, ChessGame.TeamColor color)throws Exception{
        WebsocketConnector ws = new WebsocketConnector(url, this);
        ws.joinGamePlayer(authToken, gameID, color);
        this.client = new GameClient(ws, color);
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

    public void notify(messages.Notification notification) {
        System.out.println(notification.getMessage());
    }
}
