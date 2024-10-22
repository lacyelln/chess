package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public interface GameDAO {
    GameData createGame(GameData g) throws DataAccessException;

    int getGame(GameData g) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    GameData updateGame(GameData g) throws DataAccessException;

    void deleteGame(GameData g) throws DataAccessException;

    void deleteAllGames(GameData g) throws DataAccessException;
}
