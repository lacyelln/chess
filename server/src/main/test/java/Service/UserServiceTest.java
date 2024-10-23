package Service;

import dataaccess.*;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserDAO mUser = new MemoryUserDAO();
    private final AuthDAO mAuth = new MemoryAuthDAO();
    UserService service = new UserService(mUser, mAuth);

    @Test
    void register_success() throws DataAccessException {
        var user = new UserData("a", "a", "a");
        var authData = service.register(user);
        assertEquals(user.username(), authData.username());
    }

    @Test
    void register_failed() throws DataAccessException{
        var user = new UserData("", null, "a");
        var bool = true;
        try {
            service.register(user);
        }
        catch(BadRequestException | DataAccessException | AlreadyTakenException e){
            bool = false;
        }
        assertFalse(bool);
    }

    @Test
    void login_success() throws DataAccessException, UnauthorizedException{
        var user = new UserData("a", "a", "a");
        service.register(user);
        var userData = service.login(user);
        assertEquals(user.username(), userData.username());
    }

    @Test
    void login_failed() throws DataAccessException {
        var user = new UserData("a", "a", "a");
        var result = true;
        try {
            service.login(user);
        }
        catch (UnauthorizedException e){
            result = false;
        }
        assertFalse(result);
    }

    @Test
    void logout_success() throws DataAccessException{
        var user = new UserData("a", "a", "a");
        var authData = service.register(user);
        var result = service.logout(authData.authToken());
        assertEquals(new HashMap<>(), result);
    }

    @Test
    void logout_failed(){

    }
}