package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MySqlGameDataAccess implements GameDAO{
    public MySqlGameDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }

    @Override
    public GameData createGame(String g) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(Integer g) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(Integer g, String u, String p) throws DataAccessException {

    }

    @Override
    public void deleteAllGames() throws DataAccessException {

    }
}
