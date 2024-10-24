package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private static HashMap<String, UserData> USER_MAP = new HashMap<>();

    @Override
    public void createUser(UserData u) throws DataAccessException {
        USER_MAP.put(u.username(), u);
    }


    @Override
    public UserData getUser(UserData u) throws DataAccessException {
        if(USER_MAP.containsKey(u.username())){
            return USER_MAP.get(u.username());
        }
        return null;
    }

    @Override
    public void deleteUser(UserData u) throws DataAccessException {
        if(USER_MAP.containsKey(u.username())) {
            USER_MAP.remove(u.username(), u);
        }
    }

    public void deleteAllUsers() throws DataAccessException{
        USER_MAP.clear();
    }
}
