package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData createGame(String g) throws DataAccessException;

    GameData getGame(String g) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(GameData g) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;
}
