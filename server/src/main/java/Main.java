import chess.*;
import com.google.gson.Gson;
import spark.Spark;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Spark.port(8080);
        Spark.staticFiles.location("/web");
        createRoutes();
        Spark.awaitInitialization();
        System.out.println("Listening on port " + 8080);

    }

    private static void createRoutes() {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
    }
}