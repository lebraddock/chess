package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import models.requests.CreateGameRequest;
import models.requests.JoinGameRequest;
import models.results.GameResult;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
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
            executeListGames();
        }else if(value == 4){
            executeJoinGame();
        }else if(value == 5){
            executeViewGame();
        }else if(value == 6){
            executeLogout();
            return "Finished";
        }else{
            printBodyText("Incorrect Input");
        }
        return "";
    }

    public void displayLoginMenu(){
        printHeader("Pick an option to get started:");
        printBodyText("1: Help");
        printBodyText("2: Login");
        printBodyText("3: Register");
        printBodyText("4: Quit");
    }

    public void displayGameMenu(){
        printHeader("Options:");
        printBodyText("1: Help");
        printBodyText("2: Create Game");
        printBodyText("3: List Games");
        printBodyText("4: Join Game");
        printBodyText("5: Observe Game");
        printBodyText("6: Logout");

    }

    public void executeViewGame(){
        int gameID;
        executeListGames();
        printBodyText("Enter game number to view games");
        out.print("[LOGGED IN]>>> ");
        try {
            out.print("[LOGGED IN]>>> ");
            String gameIDString = scanner.nextLine();
            gameID = Integer.parseInt(gameIDString);
            gameID = getIDFromNum(gameID);

            printBoardWhite();
            printBoardBlack();
        }catch (Exception e){
            printBodyText("Sorry! Incorrect input");
            return;
        }

    }

    public void executeLogout(){
        try {
            printHeader("Logging out...");
            server.logout(authToken);
            authToken = null;
            loginState = 1;
        } catch (Exception e) {
            printBodyText("Sorry! Unable to logout");
        }
    }

    public void executeCreateGame(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        printBodyText("Enter Game Name:");
        out.print("[LOGGED IN]>>> ");
        String gamename = scanner.nextLine();
        try {
            CreateGameRequest req = new CreateGameRequest(gamename);
            int gameID = server.createGame(req, authToken);
            printHeader("Game created!");
            printBodyText("Game name: " + gamename);
        } catch (Exception e) {
            printBodyText("Sorry! Game could not be created");
        }
    }

    public int executeListGames(){
        try {
            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            List<GameResult> chessGames = server.listGames(authToken).get("games");
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_BLACK);
            out.println("Games:");
            if (chessGames.size() == 0) {
                printBodyText("There are no games to display");
            } else {
                for (int i = 0; i < chessGames.size(); i++) {
                    printOneGame(chessGames.get(i), i + 1);
                }
            }
            return chessGames.size();
        } catch (Exception e) {
            printBodyText("No Games to display");
        }
        return 0;

    }

    private void printOneGame(GameResult game, int i){
        String wName = game.whiteUsername();
        String bName = game.blackUsername();
        if(wName == null){
            wName = "Empty";
        }
        if(bName == null){
            bName = "Empty";
        }
        printBodyText("Game Number: " + i);
        printBodyText("Name: " + game.gameName());
        printBodyText("White Player: " + wName);
        printBodyText("Black Player: " + bName);
        printHeader(".................................");
    }

    private int getIDFromNum(int num){
        try {
            List<GameResult> chessGames = server.listGames(authToken).get("games");
            num = num - 1;
            return chessGames.get(num).gameID();
        } catch (Exception e) {
            return 0;
        }
    }

    public void executeJoinGame(){
        int num = executeListGames();
        String colorS;
        int gameID;
        if(num == 0){
            return;
        }
        try {
            printBodyText("Enter game number:");
            out.print("[LOGGED IN]>>> ");
            String gameIDString = scanner.nextLine();
            gameID = Integer.parseInt(gameIDString);
            gameID = getIDFromNum(gameID);
            if(gameID == 0){
                throw new Exception();
            }
            printBodyText("Enter team color:");
            printBodyText("   1 for white:");
            printBodyText("   2 for black:");
            out.print("[LOGGED IN]>>> ");
            colorS = scanner.nextLine();
            int colorID = Integer.parseInt(colorS);
            if (colorID == 1) {
                colorS = "WHITE";
            } else if (colorID == 2) {
                colorS = "BLACK";
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            printBodyText("Sorry! Incorrect input");
            return;
        }
        JoinGameRequest req = new JoinGameRequest(colorS, gameID);
        try {
            server.joinGame(req, authToken);
            printHeader("Successfully joined game!");
        }catch(Exception e){
            String mes = e.getMessage();
            e.printStackTrace();
            if(mes == null){
                printBodyText("Sorry! An unknown error occured");
            }else if(mes.contains("400")){
                printBodyText("Sorry! Game id does not exist.");
            }else if(mes.contains("403")){
                printBodyText("Sorry! Space is already taken.");
            }else{
                printBodyText("Sorry! There was an unknown error.");
            }
        }
    }

    public void executeLogin(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        if(loginState == 2){
            printHeader("Already Logged In");
            return;
        }
        printBodyText("Enter Username:");
        out.print("[LOGGED OUT]>>> ");
        String username = scanner.nextLine();
        printBodyText("Enter Password:");
        out.print("[LOGGED OUT]>>> ");
        String password = scanner.nextLine();
        try {
            authToken = server.login(username, password);
            loginState = 2;
        } catch (Exception e) {
            printBodyText("Incorrect Username or Password");
        }
    }

    public void executeRegister(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        if(loginState == 2){
            printHeader("Already Logged In");
            return;
        }
        printBodyText("Enter Username:");
        out.print("[LOGGED OUT]>>> ");
        String username = scanner.nextLine();
        printBodyText("Enter Password:");
        out.print("[LOGGED OUT]>>> ");
        String password = scanner.nextLine();
        printBodyText("Enter Email:");
        out.print("[LOGGED OUT]>>> ");
        String email = scanner.nextLine();
        try {
            authToken = server.register(username, password, email);
            if (authToken != null) {
                loginState = 2;
            }
        }catch (Exception e){
            printBodyText("Sorry! Username already taken");
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

    private void printHeader(String str){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);
        out.println(str);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private void printBodyText(String str){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.println("   " + str);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

}