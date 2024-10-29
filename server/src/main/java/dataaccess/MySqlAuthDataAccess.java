package dataaccess;

import model.AuthData;

public class MySqlAuthDataAccess implements AuthDAO{
    @Override
    public AuthData createAuth(String u) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void deleteAllAuth() throws DataAccessException {

    }
}
