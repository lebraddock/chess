package dataaccess.requests;

import chess.ChessGame;

public record joinGameRequest(String playerColor, int gameID){}