package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class GameClient{
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private WebsocketConnector ws;
    private ChessGame.TeamColor color;
    private ChessGame game;

    public GameClient(WebsocketConnector ws, ChessGame.TeamColor color){
        this.ws = ws;
        this.color = color;
    }

    public ChessGame.TeamColor getColor(){
        return color;
    }

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
            printBoard();
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

    public void printBoard() {
        if(color == ChessGame.TeamColor.WHITE){
            printBoardWhite(game.getBoard());
        }else{
            printBoardBlack(game.getBoard());
        }
    }

    public void printBoardWhite(ChessBoard temp){
        printHeader(out);
        printRow(out, 8, temp);
        printRow(out, 7, temp);
        printRow(out, 6, temp);
        printRow(out, 5, temp);
        printRow(out, 4, temp);
        printRow(out, 3, temp);
        printRow(out, 2, temp);
        printRow(out, 1, temp);
        printHeader(out);
    }
    public void printBoardBlack(ChessBoard temp){
        printHeaderReverse(out);
        printRowRev(out, 1, temp);
        printRowRev(out, 2, temp);
        printRowRev(out, 3, temp);
        printRowRev(out, 4, temp);
        printRowRev(out, 5, temp);
        printRowRev(out, 6, temp);
        printRowRev(out, 7, temp);
        printRowRev(out, 8, temp);
        printHeaderReverse(out);
    }
    public void printRow(PrintStream out, int rowNum, ChessBoard board){
        out.print(RESET_TEXT_COLOR);
        boolean white = false;
        if(rowNum % 2 == 0){
            white = true;
        }
        out.print(" " + rowNum + " ");
        for(int i = 1; i <= 8; i++){
            white = setColor(white);
            printPiece(board, rowNum, i);
        }
        resetBG(out);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" " + rowNum + " ");
        out.println("");
    }

    public void printRowRev(PrintStream out, int rowNum, ChessBoard board){
        out.print(RESET_TEXT_COLOR);
        boolean white = true;
        if(rowNum % 2 == 0){
            white = false;
        }
        out.print(" " + rowNum + " ");
        for(int i = 8; i >= 1; i--){
            white = setColor(white);
            printPiece(board, rowNum, i);
        }
        resetBG(out);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" " + rowNum + " ");
        out.println("");
    }

    private void printPiece(ChessBoard board, int rowNum, int i){
        if(board.getPiece(new ChessPosition(rowNum,i)) != null){
            out.print(getChessPiece(board.getPiece(new ChessPosition(rowNum, i))));
        }else{
            out.print(EMPTY_PIECE);
        }
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

    private void setBGLight(){
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }
    private void setBGDark(){
        out.print(SET_BG_COLOR_DARK_GREEN);
    }

    private boolean setColor(Boolean white){
        if(white){
            setBGLight();
            white = false;
        }else{
            setBGDark();
            white = true;
        }
        return white;
    }

    public static void printHeader(PrintStream out){
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print("   ");
        for(int i = 0; i < 8; i++){
            out.print(" ");
            out.print(headers[i]);
            out.print(" ");
        }
        out.println();
    }
    public static void printHeaderReverse(PrintStream out){
        String[] headers = {"h", "g", "f", "e", "d", "c", "b", "a"};
        out.print("   ");
        for(int i = 0; i < 8; i++){
            out.print(" ");
            out.print(headers[i]);
            out.print(" ");
        }
        out.println();
    }

    private static String getChessPiece(ChessPiece piece){
        ChessPiece.PieceType type = piece.getPieceType();
        String r = "";
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            r = r + SET_TEXT_COLOR_WHITE;
        }else{
            r = r + SET_TEXT_COLOR_DARK_GREY;
        }
        switch(type) {
            case PAWN:
                r = r + PAWN;
                break;
            case ROOK:
                r =  r + ROOK;
                break;
            case BISHOP:
                r = r + BISHOP;
                break;
            case KNIGHT:
                r = r + KNIGHT;
                break;
            case KING:
                r = r + KING;
                break;
            case QUEEN:
                r = r + QUEEN;
                break;
        }
        return r;
    }

    private static void resetBG(PrintStream out){
        out.print(RESET_BG_COLOR);
    }
}
