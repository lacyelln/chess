package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MemoryGameDAO implements GameDAO{
    final private static HashMap<Integer, GameData> GAME_MAP = new HashMap<>();

    @Override
    public GameData createGame(String g) throws DataAccessException {
        GameData newGame;
        for (HashMap.Entry<Integer, GameData> entry : GAME_MAP.entrySet()) {
            if (entry.getValue().gameName().equals(g)) {
                if (GAME_MAP.containsKey(entry.getKey())) {
                    return null;
                }
            }
        }
        int randomNumber = MemoryGameDAO.generateRandomNumber(1, 1000);
        newGame = new GameData(randomNumber, null, null, g, new ChessGame());
        GAME_MAP.put(newGame.gameID(), newGame);
        return newGame;
    }
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }


    @Override
    public GameData getGame(String gameName) throws DataAccessException {
        for (HashMap.Entry<Integer, GameData> entry : GAME_MAP.entrySet()){
            if(entry.getValue().gameName().equals(gameName)){
                return GAME_MAP.get(entry.getKey());
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> listOfGames = new ArrayList<>();
        for (HashMap.Entry<Integer, GameData> entry : GAME_MAP.entrySet()) {
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
        if(GAME_MAP.containsKey(g.gameID())){
            GameData updatedData = new GameData(g.gameID(), g.whiteUsername(), g.blackUsername(), g.gameName(), updateGame);
            GAME_MAP.put(g.gameID(), updatedData);
        }
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        GAME_MAP.clear();
    }
}
