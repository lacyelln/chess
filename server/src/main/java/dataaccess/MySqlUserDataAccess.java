package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public class MySqlUserDataAccess implements UserDAO {
    @Override
    public void createUser(UserData u) throws DataAccessException {

    }

    @Override
    public UserData getUser(UserData u) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {

    }
}
