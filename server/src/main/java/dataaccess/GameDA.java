package dataaccess;

import java.util.List;
import models.GameData;
import dataaccess.results.GameResult;

public interface GameDA{
    void clearGames() throws DataAccessException;
    GameData createGame(GameData game) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<GameResult> getGames() throws DataAccessException;
}
