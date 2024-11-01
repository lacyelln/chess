package service;


import dataaccess.*;
import model.AuthData;
import model.UserData;
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
        if(uData.getUser(user.username()) != null){
            throw new AlreadyTakenException();
        }
        uData.createUser(user);
        return aData.createAuth(user.username());
    }
    public AuthData login(UserData user) throws UnauthorizedException, DataAccessException {
        if(uData.getUser(user.username()) == null){
            throw new UnauthorizedException();
        }
        UserData userData = uData.getUser(user.username());
        if (!Objects.equals(userData.password(), user.password())){
            throw new UnauthorizedException();
        }
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        if (!Objects.equals(userData.password(), hashedPassword)){
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
