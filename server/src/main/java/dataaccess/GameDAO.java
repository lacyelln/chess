package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    GameData createGame(GameData g) throws DataAccessException;

    int getGame(GameData g) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(GameData g) throws DataAccessException;

    void deleteGame(GameData g) throws DataAccessException;

    void deleteAllGames() throws DataAccessException;
}
