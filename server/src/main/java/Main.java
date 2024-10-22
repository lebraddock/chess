import chess.*;
import com.google.gson.Gson;
import spark.Spark;
import java.util.*;
import server.Server;


public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(8080);
        /*Spark.port(8080);
        Spark.staticFiles.location("/web");
        createRoutes();
        Spark.awaitInitialization();
        System.out.println("Listening on port " + 8080);
        */
    }
    /*
    private static void createRoutes() {
        Spark.post("/user", (req, res) -> "Hello BYU!");
    }
    */

}