package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> AuthMap = new HashMap<>();

    @Override
    public AuthData createAuth(AuthData u) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuthData = new AuthData(authToken, u.username());
        AuthMap.put(authToken, newAuthData);
        return newAuthData;
    }

    @Override
    public String getAuth(AuthData u) throws DataAccessException {
        for (HashMap.Entry<String, AuthData> entry : AuthMap.entrySet()){
            if(entry.getValue().username().equals(u.username())){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData u) throws DataAccessException {
        for (HashMap.Entry<String, AuthData> entry : AuthMap.entrySet()){
            if(entry.getValue().username().equals(u.username())){
                AuthMap.remove(entry.getKey(), u);
            }
        }
    }

    @Override
    public void deleteAllAuth(AuthDAO u) throws DataAccessException {
        AuthMap.clear();
    }
}
