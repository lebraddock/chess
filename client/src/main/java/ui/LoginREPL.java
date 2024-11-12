package ui;

import java.io.PrintStream;
import java.util.Scanner;

public class LoginREPL{

    public void loginMenuREPL(PrintStream out){
        String output;
        Scanner scanner = new Scanner(System.in);
        while(true) {
            out.print("[LOGGED OUT]>>> ");
            String line = scanner.nextLine();
            String[] lineS = line.split(" ");
            if (lineS.length >= 2) {
                out.println("Error: Too many inputs");
            } else if (!isValidInt(lineS[0])) {
                out.println("Error: Not a valid input");
            } else {
                out.println("Yay" + lineS[0]);
            }
        }
    }

    private boolean isValidInt(String value){
        if(value.isEmpty()){
            return false;
        }else if(!value.matches("-?\\d+")){
            return false;
        }
        int num = Integer.parseInt(value);
        if(num >= 1 && num <=4){
            return true;
        }
        return false;

    }
}