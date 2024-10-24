package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData createGame(String g) throws DataAccessException;

    GameData getGame(Integer g) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(Integer g, String u, String p) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;
}
