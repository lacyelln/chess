package dataaccess;

import model.UserData;

public interface UserDAO {

    void createUser(UserData u) throws DataAccessException;

    UserData getUser(UserData u) throws DataAccessException;

    void deleteUser(UserData u) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}
