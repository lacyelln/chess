package Service;


import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;


public class UserService {

    private final UserDAO uData;
    private final AuthDAO aData;

    public UserService(UserDAO uData, AuthDAO aData){
        this.uData = uData;
        this.aData = aData;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if(uData.getUser(user) != null){
            throw new DataAccessException("Error: already taken");
        }
        uData.createUser(user);
        return aData.createAuth(user);
    }
    public AuthData login(UserData user) throws DataAccessException {
        if(uData.getUser(user) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        return aData.createAuth(user);
    }
    public void logout(AuthData auth) throws DataAccessException {
        String username = auth.username();
        UserData user = new UserData(username, null, null);
        if(aData.getAuth(user) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        aData.deleteAuth(auth);
    }

}
