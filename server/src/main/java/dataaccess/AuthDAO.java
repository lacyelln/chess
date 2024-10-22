package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData u) throws DataAccessException;

    String getAuth(AuthData u) throws DataAccessException;

    void deleteAuth(AuthData u) throws DataAccessException;

    void deleteAllAuth(AuthDAO u) throws DataAccessException;
}
