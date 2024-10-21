package dataaccess.storage;
import dataaccess.GameDA;
import models.GameData;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Games implements GameDA{
    List <GameData> gameList = new ArrayList<GameData>();
    Random rand;

    public void clearGames(){
        gameList = new ArrayList<GameData>();
    }

    public GameData createGame(GameData game){
        gameList.add(game);
        return game;
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