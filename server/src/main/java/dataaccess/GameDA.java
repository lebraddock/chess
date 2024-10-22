package dataaccess;

import chess.*;
import java.util.List;
import models.GameData;
import results.GameResult;

public interface GameDA{
    void clearGames();
    GameData createGame(GameData game) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<GameResult> getGames() throws DataAccessException;
}
