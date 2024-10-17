package dataaccess.storage;
import dataaccess.GameDA;
import models.GameData;
import java.util.List;
import java.util.ArrayList;

public class Games implements GameDA{
    List <GameData> gameList = new ArrayList<GameData>();

    public void clearGames(){
        gameList = new ArrayList<GameData>();
    }

    public void createGame(GameData game){
        gameList.add(game);
    }

    public void updateGame(GameData game){
        for(int i = 0; i < gameList.size(); i++){
            if(gameList.get(i).gameID() == game.gameID()){
                gameList.remove(i);
                break;
            }
        }
        gameList.add(game);
    }

    public GameData getGame(int gameID){
        for(int i = 0; i < gameList.size(); i++){
            if(gameList.get(i).gameID() == gameID){
                return gameList.get(i);
            }
        }
        return null;
    }

    public List<GameData> getGames(){
        return gameList;
    }
}
