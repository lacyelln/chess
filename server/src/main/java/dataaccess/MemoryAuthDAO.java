package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private static HashMap<String, AuthData> AuthMap = new HashMap<>();

    @Override
    public AuthData createAuth(String u) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuthData = new AuthData(authToken, u);
        AuthMap.put(authToken, newAuthData);
        return newAuthData;
    }


    @Override
    public String getAuth(String authToken) throws DataAccessException {
        for (HashMap.Entry<String, AuthData> entry : AuthMap.entrySet()){
            if(entry.getValue().authToken().equals(authToken)){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for (HashMap.Entry<String, AuthData> entry : AuthMap.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                AuthMap.remove(authToken);
            }
        }
    }

    @Override
    public void deleteAllAuth() throws DataAccessException {
        AuthMap.clear();
    }
}
