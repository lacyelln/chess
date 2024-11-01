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
    public UserData getUser(String u) throws DataAccessException {
        if(USER_MAP.containsKey(u)){
            return USER_MAP.get(u);
        }
        return null;
    }


    public void deleteAllUsers() throws DataAccessException{
        USER_MAP.clear();
    }
}
