package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{
    final private static HashMap<Integer, GameData> GameMap = new HashMap<>();

    @Override
    public GameData createGame(String g) throws DataAccessException {
        GameData newGame;
        for (HashMap.Entry<Integer, GameData> entry : GameMap.entrySet()) {
            if (entry.getValue().gameName().equals(g)) {
                if (GameMap.containsKey(entry.getKey())) {
                    return null;
                }
            }
        }
        int randomNumber = MemoryGameDAO.generateRandomNumber(1, 1000);
        newGame = new GameData(randomNumber, null, null, g, new ChessGame());
        GameMap.put(newGame.GameID(), newGame);
        return newGame;
    }
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }


    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        for (HashMap.Entry<Integer, GameData> entry : GameMap.entrySet()){
            if(entry.getValue().gameName().equals(gameName)){
                return GameMap.get(entry.getKey());
            }
        }
        return null;
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
    public void updateGame(GameData g) throws DataAccessException {
        ChessGame updateGame = g.game();
        if(GameMap.containsKey(g.GameID())){
            GameData updatedData = new GameData(g.GameID(), g.whiteUsername(), g.blackUsername(), g.gameName(), updateGame);
            GameMap.put(g.GameID(), updatedData);
        }
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        GameMap.clear();
    }
}
