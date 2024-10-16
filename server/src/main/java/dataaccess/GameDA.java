package dataaccess;

import chess.*;
import java.util.List;
import models.GameData;

public interface GameDA{
    void clearGames();
    void createGame(String gameName) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    ChessGame getGame(String gameID) throws DataAccessException;
    List<GameData> getGames() throws DataAccessException;
}
