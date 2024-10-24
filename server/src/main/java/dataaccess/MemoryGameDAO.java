package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
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
    public GameData getGame(Integer gameID) throws DataAccessException {
        for (HashMap.Entry<Integer, GameData> entry : GAME_MAP.entrySet()){
            if(entry.getValue().gameID() == gameID){
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
    public void updateGame(Integer gameID, String user, String playerColor) throws DataAccessException {
        GameData updatedData;
        if(GAME_MAP.containsKey(gameID)){
            if (Objects.equals(playerColor, "WHITE")){
                if (GAME_MAP.get(gameID).whiteUsername() == null){
                    updatedData = new GameData(gameID, user, GAME_MAP.get(gameID).blackUsername(), GAME_MAP.get(gameID).gameName(), new ChessGame());
                }
                else {
                    throw new AlreadyTakenException();
                }
            }
            else{
                if (GAME_MAP.get(gameID).blackUsername() == null) {
                    updatedData = new GameData(gameID, GAME_MAP.get(gameID).whiteUsername(), user, GAME_MAP.get(gameID).gameName(), new ChessGame());
                }
                else{
                    throw new AlreadyTakenException();
                }
            }

            GAME_MAP.put(gameID, updatedData);
        }
    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        GAME_MAP.clear();
    }
}
