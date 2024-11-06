package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessClient{
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    public void printBoard(){
        ChessBoard temp = new ChessBoard();
        temp.resetBoard();
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
    public static void printRow(PrintStream out, int rowNum, ChessBoard board){
        out.print(RESET_TEXT_COLOR);
        boolean white = false;
        if(rowNum % 2 == 0){
            white = true;
        }
        out.print(" " + rowNum + " ");
        for(int i = 1; i <= 8; i++){
            if(white){
                setBGLight(out);
                white = false;
            }else{
                setBGDark(out);
                white = true;
            }
            if(board.getPiece(new ChessPosition(rowNum,i)) != null){
                out.print(getChessPiece(board.getPiece(new ChessPosition(rowNum, i))));
            }else{
                out.print(EMPTY_PIECE);
            }
        }
        resetBG(out);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(" " + rowNum + " ");
        out.println("");
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

    private static void setHeaderColors(PrintStream out){
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_BLACK);
    }
    private static void setBGLight(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }
    private static void setBGDark(PrintStream out){
        out.print(SET_BG_COLOR_DARK_GREEN);
    }
    private static void resetBG(PrintStream out){
        out.print(RESET_BG_COLOR);
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
}