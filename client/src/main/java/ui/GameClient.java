package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class GameClient{
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private WebsocketConnector ws;
    private ChessGame.TeamColor color;
    private ChessGame game;
    Scanner scanner = new Scanner(System.in);
    String authToken;
    int gameID;
    Collection<ChessPosition> highLightPositions = new ArrayList<>();
    ArrayList<ChessPosition> prevMove = new ArrayList<>();
    Boolean gameFinished;

    public GameClient(WebsocketConnector ws, ChessGame.TeamColor color, String authToken, int gameID){
        this.ws = ws;
        this.color = color;
        this.authToken = authToken;
        this.gameID = gameID;
        game = new ChessGame();
        gameFinished = false;
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

    public void printViewGameMenu() {
        printHeader("Options:");
        printBodyText("1: Help");
        printBodyText("2: Redraw Board");
        printBodyText("3: Show Legal Moves");
        printBodyText("4: Leave");
    }

    public String evaluateViewInput(int value){
        if(!(value >= 1 && value <= 4)){
            return "";
        }
        if(value == 1){
            printViewGameMenu();
        }else if(value == 2){
            printBoard();
        }else if(value == 3){
            showLegalMoves();
        }else if(value == 4){
            return "Exiting Game...";
        }else{
            printBodyText("Incorrect Input");
        }
        return "";
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
            makeMove();
        }else if(value == 4){
            showLegalMoves();
        }else if(value == 5){
            resign();
        }else if(value == 6){
            return "Exiting Game...";
        }else{
            printBodyText("Incorrect Input");
        }
        return "";
    }

    public void resign(){
        int result = 0;
        if(color == ChessGame.TeamColor.WHITE){
            result = 1;
        }else{
            result = 2;
        }
        gameFinished = true;
        ws.endGame(authToken, gameID);
    }

    public void setGameFinished(boolean b){
        gameFinished = b;
    }

    public void showLegalMoves(){
        if(gameFinished){
            printBodyText("The game has ended");
            return;
        }
        printHeader("Enter piece to move:");
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.print("[IN GAME]>>> ");
        String start = scanner.nextLine();
        try {
            ChessPosition pos = notationToNum(start);
            setHighLightPositions(pos);
            printBoard();
            resetHighlight();
        }catch(Exception e){
            printBodyText("Sorry! Please select a piece in standard chess notation");
        }
    }


    public void makeMove(){
        if(gameFinished){
            printBodyText("The game has ended");
            return;
        }
        printHeader("Enter move start position:");
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.print("[IN GAME]>>> ");
        String start = scanner.nextLine();
        printHeader("Enter move end position:");
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.print("[IN GAME]>>> ");
        String end = scanner.nextLine();
        try{
            ChessPosition startPos = notationToNum(start);
            ChessPosition endPos = notationToNum(end);
            ChessMove move = new ChessMove(startPos, endPos, null);
            ws.makeMove(authToken, gameID, move);
        }catch (Exception e){
            printBodyText("Sorry! Invalid move");
        }


    }

    public void printBoard() {
        if(color == ChessGame.TeamColor.WHITE){
            printBoardWhite(game.getBoard());
        }else{
            printBoardBlack(game.getBoard());
        }
        ChessGame.TeamColor turn = game.getTeamTurn();
        if(gameFinished){
            if(game.isInCheckmate(ChessGame.TeamColor.WHITE)){
                printBodyText("White has been checkmated");
            }else if(game.isInCheckmate(ChessGame.TeamColor.BLACK)){
                printBodyText("Black had been checkmated");
            }
            if(game.getResult() == 1){
                printHeader("White has won the game");
            }else{
                printHeader("Black has won the game");
            }
            return;
        }

        if(game.isInCheck(turn)){
            if(turn == ChessGame.TeamColor.WHITE){
                printBodyText("White is in check!");
            }else{
                printBodyText("Black is in check!");
            }
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
            setHighlight(rowNum, i, white);
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
            setHighlight(rowNum,i,white);
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

    private void setHighlight(int y, int x, boolean isWhite){
        boolean moveHighlight = false;
        boolean lastHighlight = false;
        ChessPosition pos = new ChessPosition(y,x);
        if(highLightPositions.contains(pos)){
            moveHighlight = true;
        }
        if(prevMove.contains(pos)){
            lastHighlight = true;
        }
        if(moveHighlight){
            if(isWhite){
                out.print(SET_BG_COLOR_BLUE);
            }else{
                out.print(SET_BG_COLOR_LIGHT_BLUE);
            }
        }
        if(lastHighlight){
            out.print(SET_BG_COLOR_YELLOW);
        }
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

    public void updateGame(ChessGame newGame){
        game = newGame;
    }

    private static void resetBG(PrintStream out){
        out.print(RESET_BG_COLOR);
    }

    ChessPosition notationToNum(String notation) throws Exception{
        notation = notation.toLowerCase();
        if(notation.length() != 2){
            throw new Exception();
        }
        int x = notation.charAt(0) - 'a' + 1;
        int y = notation.charAt(1) - 48;
        return new ChessPosition(y,x);
    }

    private void setPrevMove(ChessMove move){
        prevMove = new ArrayList<>();
        prevMove.add(move.getStartPosition());
        prevMove.add(move.getEndPosition());
    }

    private void setHighLightPositions(ChessPosition pos){
        resetHighlight();
        Collection<ChessMove> highLightPositionsT = game.getValidMoves(pos);
        for(ChessMove c : highLightPositionsT){
            highLightPositions.add(c.getEndPosition());
        }
    }

    private void resetHighlight(){
        highLightPositions = new ArrayList<ChessPosition>();
    }

    public String getAuth(){
        return authToken;
    }

    public int getGameID(){
        return gameID;
    }
}
