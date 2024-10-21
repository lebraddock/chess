package dataaccess;

import chess.*;
import java.util.List;
import models.GameData;

public interface GameDA{
    void clearGames();
    GameData createGame(GameData game) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    List<GameData> getGames() throws DataAccessException;
}
