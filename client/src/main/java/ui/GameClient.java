package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class GameClient{
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public void printInGameMenu() {
        printHeader("Options:");
        printBodyText("1: Help");
        printBodyText("2: Redraw Board");
        printBodyText("3: Make Move");
        printBodyText("4: Show Legal Moves");
        printBodyText("5: Resign");
        printBodyText("6: Leave");
    }

    public String evaluateInput(int value){
        if(!(value >= 1 && value <= 6)){
            return "";
        }
        if(value == 1){
            printInGameMenu();
        }else if(value == 2){

        }else if(value == 3){

        }else if(value == 4){

        }else if(value == 5){

        }else if(value == 6){

            return "Finished";
        }else{
            printBodyText("Incorrect Input");
        }
        return "";
    }

    public void printHeader(String str){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.println(str);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    public void printBodyText(String str){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("   " + str);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }
}
