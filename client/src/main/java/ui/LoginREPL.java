package ui;

import java.io.PrintStream;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class LoginREPL{

    ChessClient client = new ChessClient();

    public void loginMenuREPL(PrintStream out){
        String output;
        Scanner scanner = new Scanner(System.in);
        while(true) {
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
                client.evaluateInput(Integer.parseInt(lineS[0]));
            }
        }
    }

    public void loggedInREPL(PrintStream out){
        String output;
        Scanner scanner = new Scanner(System.in);
        while(true) {
            out.print("[LOGGED IN]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!isValidInt(lineS[0], 7)) {
                out.println("Error: Not a valid input");
            } else {
                out.println("Yay" + lineS[0]);
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