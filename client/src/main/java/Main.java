import chess.*;
import ui.ChessClient;
import ui.LoginREPL;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        Server server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

        new LoginREPL(serverUrl).loginMenuREPL();

        server.stop();
    }
}