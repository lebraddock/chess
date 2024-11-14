package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class LoginREPL{
    String url;
    ChessClient client;
    public LoginREPL(String url){
        this.url = url;
        this.client = new ChessClient(url);
    }
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public void loginMenuREPL(){
        String output;
        Scanner scanner = new Scanner(System.in);
        String result = "";
        client.printHeader("Welcome to the chess client!");
        while(!result.equals("Finished")) {
            client.printHeader("Enter option: (Press 1 for help)");
            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            out.print("[LOGGED OUT]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!isValidInt(lineS[0], 4)) {
                out.println("Error: Not a valid input");
            } else {
                result = client.evaluateInput(Integer.parseInt(lineS[0]));
            }
            if(client.getLoginState() == 2){
                loggedInREPL();
                client.printHeader("Welcome to the chess client! Press 1 for help");
            }
        }
        client.printHeader("Exiting program...");
    }

    public void loggedInREPL(){
        String output;
        String result = "";
        Scanner scanner = new Scanner(System.in);
        client.printHeader("Thanks for logging in!");
        while(!result.equals("Finished")) {
            client.printHeader("Enter option: (Press 1 for help)");
            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            out.print("[LOGGED IN]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!isValidInt(lineS[0], 6)) {
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
}