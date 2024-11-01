package dataaccess;

import model.UserData;

public interface UserDAO {

    void createUser(UserData u) throws DataAccessException;

    UserData getUser(String u) throws DataAccessException;

    void deleteAllUsers() throws DataAccessException;
}
