package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(UserData u) throws DataAccessException;

    String getAuth(UserData u) throws DataAccessException;

    void deleteAuth(AuthData u) throws DataAccessException;

    void deleteAllAuth() throws DataAccessException;
}
