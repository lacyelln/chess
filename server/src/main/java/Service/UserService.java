package Service;


import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;


public class UserService {

    private final UserDAO uData;
    private final AuthDAO aData;

    public UserService(UserDAO uData, AuthDAO aData){
        this.uData = uData;
        this.aData = aData;
    }

    public AuthData register(UserData user) throws DataAccessException, AlreadyTakenException, BadRequestException {
        if(Objects.equals(user.username(), "") | user.username() == null){
            throw new BadRequestException();
        }
        if (Objects.equals(user.password(), "") | user.password() == null){
            throw new BadRequestException();
        }
        if(uData.getUser(user) != null){
            throw new AlreadyTakenException();
        }
        uData.createUser(user);
        return aData.createAuth(user.username());
    }
    public AuthData login(UserData user) throws UnauthorizedException, DataAccessException {
        if(uData.getUser(user) == null){
            throw new UnauthorizedException();
        }
        UserData userData = uData.getUser(user);
        if (!Objects.equals(userData.password(), user.password())){
            throw new UnauthorizedException();
        }
        return aData.createAuth(user.username());
    }
    public HashMap<String, String> logout(String authToken) throws DataAccessException {

        if(aData.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }
        aData.deleteAuth(authToken);
        return new HashMap<>();
    }

    public void delete() throws DataAccessException{
        aData.deleteAllAuth();
        uData.deleteAllUsers();
    }

}
