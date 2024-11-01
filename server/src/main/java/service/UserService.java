package service;


import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

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
        if (Objects.equals(user.email(), "") | user.email() == null) {
            throw new BadRequestException();
        }
        if(uData.getUser(user.username()) != null){
            throw new AlreadyTakenException();
        }
        UserData newUser = new UserData(
                user.username(),
                BCrypt.hashpw(user.password(), BCrypt.gensalt()),
                user.email());
        uData.createUser(newUser);
        return aData.createAuth(user.username());
    }
    public AuthData login(UserData user) throws UnauthorizedException, DataAccessException {
        if(uData.getUser(user.username()) == null){
            throw new UnauthorizedException();
        }
        UserData userData = uData.getUser(user.username());
        if (!BCrypt.checkpw(user.password(), userData.password())){
            throw new UnauthorizedException();
        }
        return aData.createAuth(user.username());
    }
    public void logout(String authToken) throws DataAccessException, UnauthorizedException {
        if(aData.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        aData.deleteAuth(authToken);
    }

    public void delete() throws DataAccessException{
        aData.deleteAllAuth();
        uData.deleteAllUsers();
    }

}
