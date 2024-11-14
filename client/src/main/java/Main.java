import chess.*;
import ui.ChessClient;
import ui.LoginREPL;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var serverURL = "http://localhost:8080";

        LoginREPL repl = new LoginREPL(serverURL);
        repl.loginMenuREPL();
    }
}