package Service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserDAO mUser = new MemoryUserDAO();
    private final AuthDAO mAuth = new MemoryAuthDAO();
    UserService service = new UserService(mUser, mAuth);

    @Test
    void register() throws DataAccessException {
        var user = new UserData("a", "a", "a");
        var authData = service.register(user);
        assertEquals(user.username(), authData.username());
    }

    @Test
    void login() throws DataAccessException{
        var user = new UserData("a", "a", "a");
        var userData = service.login(user);
        assertEquals(user.username(), userData.username());
    }

    @Test
    void logout() throws DataAccessException{
        var user = new UserData("a", "a", "a");
        var authData = service.register(user);
        var result = service.logout(authData.authToken());
        assertEquals(new HashMap<>(), result);
    }
}