package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    final private static HashMap<String, AuthData> AUTH_MAP = new HashMap<>();

    @Override
    public AuthData createAuth(String u) throws DataAccessException {
        String authToken;
        do {
            authToken = UUID.randomUUID().toString();
        } while (AUTH_MAP.containsKey(authToken));

        AuthData newAuthData = new AuthData(authToken, u);
        AUTH_MAP.put(authToken, newAuthData);
        return newAuthData;
    }


    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (HashMap.Entry<String, AuthData> entry : AUTH_MAP.entrySet()){
            if(entry.getValue().authToken().equals(authToken)){
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for (HashMap.Entry<String, AuthData> entry : AUTH_MAP.entrySet()) {
            if (entry.getValue().authToken().equals(authToken)) {
                AUTH_MAP.remove(authToken);
            }
        }
    }

    @Override
    public void deleteAllAuth() throws DataAccessException {
        AUTH_MAP.clear();
    }
}
