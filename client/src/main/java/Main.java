import chess.*;
import ui.ChessClient;
import ui.LoginREPL;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        ChessClient temp = new ChessClient();
        LoginREPL repl = new LoginREPL();
        repl.loginMenuREPL(temp.getPrintStream());
    }
}