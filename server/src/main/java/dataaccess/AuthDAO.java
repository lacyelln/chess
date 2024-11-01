package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData createAuth(String u) throws DataAccessException;

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken) throws DataAccessException;

    void deleteAllAuth() throws DataAccessException;
}
