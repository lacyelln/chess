package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> GameMap = new HashMap<>();

    @Override
    public GameData createGame(GameData g) throws DataAccessException {
        GameData newGame = new GameData(g.GameID(), g.whiteUsername(), g.blackUsername(), g.gameName(), g.game());
        GameMap.put(g.GameID(), newGame);
        return newGame;
    }

    @Override
    public int getGame(GameData g) throws DataAccessException {
        for (HashMap.Entry<Integer, GameData> entry : GameMap.entrySet()){
            if(entry.getValue().GameID() == g.GameID()){
                return entry.getKey();
            }
        }
        return 0;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> listOfGames = new ArrayList<>();
        for (HashMap.Entry<Integer, GameData> entry : GameMap.entrySet()) {
            Integer gameId = entry.getKey();
            GameData gameData = entry.getValue();
            GameData game = new GameData(gameId, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
            listOfGames.add(game);
        }
        return listOfGames;
    }

    @Override
    public GameData updateGame(GameData g) throws DataAccessException {
        ChessGame updateGame = g.game();
        if(GameMap.containsKey(g.GameID())){
            GameData updatedData = new GameData(g.GameID(), g.whiteUsername(), g.blackUsername(), g.gameName(), updateGame);
            GameMap.put(g.GameID(), updatedData);
            return updatedData;
        }
        return null;
    }

    @Override
    public void deleteGame(GameData g) throws DataAccessException {
        for (HashMap.Entry<Integer, GameData> entry : GameMap.entrySet()){
            if(entry.getValue().GameID() == g.GameID()){
                GameMap.remove(entry.getKey(), g);
            }
        }
    }

    @Override
    public void deleteAllGames(GameData g) throws DataAccessException {
        GameMap.clear();
    }
}
