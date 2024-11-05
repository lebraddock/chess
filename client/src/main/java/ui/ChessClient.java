package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessClient{
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    public void printBoard(){
        printHeader(out);
    }
    public static void printRow(PrintStream out, int rowNum, ChessGame game){
        boolean white = false;
        if(rowNum % 2 == 0){
            white = true;
        }
        
    }
    public static void printHeader(PrintStream out){
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for(int i = 0; i < 8; i++){
            out.print(" ");
            out.print(headers[i]);
            out.print("\u2003");
        }
        out.println();
    }

    private static void setHeaderColors(PrintStream out){
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
    }
}