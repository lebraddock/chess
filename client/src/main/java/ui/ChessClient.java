package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import models.requests.CreateGameRequest;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient{
    ServerFacade server;
    PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    Scanner scanner = new Scanner(System.in);
    // 1 is logged out, 2 is logged in
    int loginState = 1;
    String url;
    String authToken;

    public ChessClient(String url){
        this.url = url;
        server = new ServerFacade(url);
    }

    public String evaluateInput(int value){
        if(loginState == 1){
            return evaluateLoginREPL(value);
        }else{
            return evaluateGameREPL(value);
        }
    }

    public String evaluateLoginREPL(int value){
        if(!(value >=1 && value <= 4)){
            return "";
        }
        if(value == 1){
            displayLoginMenu();
        }else if(value == 2){
            executeLogin();
        }else if(value == 3){
            executeRegister();
        }else{
            return "Finished";
        }
        return "";
    }

    public String evaluateGameREPL(int value){
        if(!(value >=1 && value <= 7)){
            return "";
        }
        if(value == 1){
            displayGameMenu();
        }else if(value == 2){
            executeCreateGame();
        }else if(value == 3){
            executeRegister();
        }else if(value == 4){
            executeRegister();
        }else if(value == 5){
            executeRegister();
        }else if(value == 6){
            executeLogout();
            return "Finished";
        }else{
            return "Finished";
        }
        return "";
    }

    public void displayLoginMenu(){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.println("Pick an option to get started:");
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("1: Help");
        out.println("2: Login");
        out.println("3: Register");
        out.println("4: Quit");
    }

    public void displayGameMenu(){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.println("Options:");
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("1: Help");
        out.println("2: Create Game");
        out.println("3: List Games");
        out.println("4: Join Game");
        out.println("5: Observe Game");
        out.println("6: Logout");
        out.println("7: Quit");

    }

    public void executeLogout(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println("Logging out...");
        server.logout(authToken);
        authToken = null;
        loginState = 1;
    }

    public void executeCreateGame(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println("Enter Game Name:");
        out.print("[LOGGED IN]>>> ");
        String gamename = scanner.nextLine();
        CreateGameRequest req = new CreateGameRequest(gamename);
        int gameID = server.createGame(req, authToken);
    }

    public void executeLogin(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        if(loginState == 2){
            out.println("Already Logged In");
            return;
        }
        out.println("Enter Username:");
        out.print("[LOGGED OUT]>>> ");
        String username = scanner.nextLine();
        out.println("Enter Password:");
        out.print("[LOGGED OUT]>>> ");
        String password = scanner.nextLine();
        authToken = server.login(username, password);
        loginState = 2;
    }

    public void executeRegister(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        if(loginState == 2){
            out.println("Already Logged In");
            return;
        }
        out.println("Enter Username:");
        out.print("[LOGGED OUT]>>> ");
        String username = scanner.nextLine();
        out.println("Enter Password:");
        out.print("[LOGGED OUT]>>> ");
        String password = scanner.nextLine();
        out.println("Enter Email:");
        out.print("[LOGGED OUT]>>> ");
        String email = scanner.nextLine();
        authToken = server.register(username, password,email);
        if(authToken != null){
            loginState = 2;
        }
    }


    public void printBoardWhite(){
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
    public void printBoardBlack(){
        ChessBoard temp = new ChessBoard();
        temp.resetBoard();
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

    public static void printRowRev(PrintStream out, int rowNum, ChessBoard board){
        out.print(RESET_TEXT_COLOR);
        boolean white = true;
        if(rowNum % 2 == 0){
            white = false;
        }
        out.print(" " + rowNum + " ");
        for(int i = 8; i >= 1; i--){
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

    public PrintStream getPrintStream(){
        return out;
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

    public int getLoginState(){
        return loginState;
    }

}