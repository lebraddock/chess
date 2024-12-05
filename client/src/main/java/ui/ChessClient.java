package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import models.GameData;
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
            PrintFunctions.printBodyText("Incorrect Input");
        }
        return "";
    }

    public void displayLoginMenu(){
        PrintFunctions.printHeader("Pick an option to get started:");
        PrintFunctions.printBodyText("1: Help");
        PrintFunctions.printBodyText("2: Login");
        PrintFunctions.printBodyText("3: Register");
        PrintFunctions.printBodyText("4: Quit");
    }

    public void displayGameMenu(){
        PrintFunctions.printHeader("Options:");
        PrintFunctions.printBodyText("1: Help");
        PrintFunctions.printBodyText("2: Create Game");
        PrintFunctions.printBodyText("3: List Games");
        PrintFunctions.printBodyText("4: Join Game");
        PrintFunctions.printBodyText("5: Observe Game");
        PrintFunctions.printBodyText("6: Logout");

    }

    public void executeViewGame(){
        int gameID;
        executeListGames();
        PrintFunctions.printBodyText("Enter game number to view games");
        out.print("[LOGGED IN]>>> ");
        try {
            out.print("[LOGGED IN]>>> ");
            String gameIDString = scanner.nextLine();
            gameID = Integer.parseInt(gameIDString);
            gameID = getIDFromNum(gameID);
            GameplayREPL inGameREPL = new GameplayREPL(url, authToken, gameID, null);
            inGameREPL.viewREPL();
        }catch (Exception e){
            PrintFunctions.printBodyText("Sorry! Incorrect input");
        }

    }

    public void executeLogout(){
        try {
            PrintFunctions.printHeader("Logging out...");
            server.logout(authToken);
            authToken = null;
            loginState = 1;
        } catch (Exception e) {
            PrintFunctions.printBodyText("Sorry! Unable to logout");
        }
    }

    public void executeCreateGame(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        PrintFunctions.printBodyText("Enter Game Name:");
        out.print("[LOGGED IN]>>> ");
        String gamename = scanner.nextLine();
        try {
            CreateGameRequest req = new CreateGameRequest(gamename);
            int gameID = server.createGame(req, authToken);
            PrintFunctions.printHeader("Game created!");
            PrintFunctions.printBodyText("Game name: " + gamename);
        } catch (Exception e) {
            PrintFunctions.printBodyText("Sorry! Game could not be created");
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
                PrintFunctions.printBodyText("There are no games to display");
            } else {
                for (int i = 0; i < chessGames.size(); i++) {
                    printOneGame(chessGames.get(i), i + 1);
                }
            }
            return chessGames.size();
        } catch (Exception e) {
            PrintFunctions.printBodyText("No Games to display");
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
        PrintFunctions.printBodyText("Game Number: " + i);
        PrintFunctions.printBodyText("Name: " + game.gameName());
        PrintFunctions.printBodyText("White Player: " + wName);
        PrintFunctions.printBodyText("Black Player: " + bName);
        PrintFunctions.printHeader(".................................");
    }

    private int getIDFromNum(int num) throws Exception{
        try {
            List<GameResult> chessGames = server.listGames(authToken).get("games");
            num = num - 1;
            return chessGames.get(num).gameID();
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void executeJoinGame(){
        int num = executeListGames();
        String colorS;
        ChessGame.TeamColor tColor;
        int gameID;
        if(num == 0){
            return;
        }
        try {
            PrintFunctions.printBodyText("Enter game number:");
            out.print("[LOGGED IN]>>> ");
            String gameIDString = scanner.nextLine();
            gameID = Integer.parseInt(gameIDString);
            gameID = getIDFromNum(gameID);
            if(gameID == 0){
                throw new Exception();
            }
            PrintFunctions.printBodyText("Enter team color:");
            PrintFunctions.printBodyText("   1 for white:");
            PrintFunctions.printBodyText("   2 for black:");
            out.print("[LOGGED IN]>>> ");
            colorS = scanner.nextLine();
            int colorID = Integer.parseInt(colorS);
            if (colorID == 1) {
                colorS = "WHITE";
                tColor = ChessGame.TeamColor.WHITE;
            } else if (colorID == 2) {
                colorS = "BLACK";
                tColor = ChessGame.TeamColor.BLACK;
            }else{
                throw new Exception();
            }
            //JoinGameRequest req = new JoinGameRequest(colorS, gameID);

            List<GameResult> games = server.listGames(authToken).get("games");
            for(GameResult game :games){
                if(game.gameID() == gameID){
                    if(game.blackUsername() != null && colorID == 2){
                        throw new Exception("403");
                    }
                    if(game.whiteUsername() != null && colorID == 1){
                        throw new Exception("403");
                    }
                }
            }
            GameplayREPL inGameREPL = new GameplayREPL(url, authToken, gameID, tColor);
            inGameREPL.gameREPL();
        }catch(Exception e){
            String mes = e.getMessage();
            if(mes == null){
                PrintFunctions.printBodyText("Sorry! Input is incorrect");
            }else if(mes.contains("400")){
                PrintFunctions.printBodyText("Sorry! Game does not exist.");
            }else if(mes.contains("403")){
                PrintFunctions.printBodyText("Sorry! Space is already taken.");
            }else{
                PrintFunctions.printBodyText("Sorry! There was an unknown error.");
            }
        }
    }

    public void executeLogin(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        if(loginState == 2){
            PrintFunctions.printHeader("Already Logged In");
            return;
        }
        PrintFunctions.printBodyText("Enter Username:");
        out.print("[LOGGED OUT]>>> ");
        String username = scanner.nextLine();
        PrintFunctions.printBodyText("Enter Password:");
        out.print("[LOGGED OUT]>>> ");
        String password = scanner.nextLine();
        try {
            authToken = server.login(username, password);
            loginState = 2;
        } catch (Exception e) {
            PrintFunctions.printBodyText("Incorrect Username or Password");
        }
    }

    public void executeRegister(){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        if(loginState == 2){
            PrintFunctions.printHeader("Already Logged In");
            return;
        }
        PrintFunctions.printBodyText("Enter Username:");
        out.print("[LOGGED OUT]>>> ");
        String username = scanner.nextLine();
        PrintFunctions.printBodyText("Enter Password:");
        out.print("[LOGGED OUT]>>> ");
        String password = scanner.nextLine();
        PrintFunctions.printBodyText("Enter Email:");
        out.print("[LOGGED OUT]>>> ");
        String email = scanner.nextLine();
        try {
            authToken = server.register(username, password, email);
            if (authToken != null) {
                loginState = 2;
            }
        }catch (Exception e){
            PrintFunctions.printBodyText("Sorry! Username already taken");
        }
    }






    public PrintStream getPrintStream(){
        return out;
    }






    public int getLoginState(){
        return loginState;
    }



}