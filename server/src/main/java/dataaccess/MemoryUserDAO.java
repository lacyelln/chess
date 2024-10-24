package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private static HashMap<String, UserData> UserMap = new HashMap<>();

    @Override
    public void createUser(UserData u) throws DataAccessException {
        UserMap.put(u.username(), u);
    }


    @Override
    public UserData getUser(UserData u) throws DataAccessException {
        if(UserMap.containsKey(u.username())){
            return UserMap.get(u.username());
        }
        return null;
    }

    @Override
    public void deleteUser(UserData u) throws DataAccessException {
        if(UserMap.containsKey(u.username())) {
            UserMap.remove(u.username(), u);
        }
    }

    public void deleteAllUsers() throws DataAccessException{
        UserMap.clear();
    }
}
