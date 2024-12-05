package websocket.commands;

import chess.ChessGame;


public class JoinGameCommand extends UserGameCommand{

    private ChessGame.TeamColor color;
    public JoinGameCommand(String authToken, int gameID, ChessGame.TeamColor color){
        super(CommandType.JOIN_GAME, authToken, gameID);
        this.color = color;
    }

    public ChessGame.TeamColor getColor(){
        return color;
    }

}

