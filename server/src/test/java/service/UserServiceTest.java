package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserDAO mUser = new MemoryUserDAO();
    private final AuthDAO mAuth = new MemoryAuthDAO();
    UserService service = new UserService(mUser, mAuth);

    @BeforeEach
    void setUp() throws DataAccessException {
        mAuth.deleteAllAuth();
        mUser.deleteAllUsers();
    }

    @Test
    void registerSuccess() throws DataAccessException {
        var user = new UserData("f", "a", "a");
        var authData = service.register(user);
        assertEquals(user.username(), authData.username());
    }

    @Test
    void registerFailed() throws DataAccessException{
        var user = new UserData("", null, "a");
        assertThrows(BadRequestException.class, () -> service.register(user));
    }

    @Test
    void loginSuccess() throws DataAccessException, UnauthorizedException{
        var user = new UserData("z", "b", "b");
        service.register(user);
        var userData = service.login(user);
        assertEquals(user.username(), userData.username());
    }

    @Test
    void loginFailed() throws DataAccessException {
        var user = new UserData("c", "c", "c");
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
    void logoutSuccess() throws DataAccessException{
        var user = new UserData("d", "d", "d");
        var authData = service.register(user);
        String result = "";
        try {
            service.logout(authData.authToken());
            result = "{}";
        }
        catch (UnauthorizedException e){
            result = "wrong";
        }
        assertEquals("{}", result);
    }

    @Test
    void logoutFailed() throws DataAccessException, UnauthorizedException{
        var user = new UserData("m", "m", "m");
        var authData = service.register(user);
        mAuth.deleteAuth(authData.authToken());
        String result = "";
        try {
            service.logout(authData.authToken());
            result = "{}";
        }
        catch (UnauthorizedException e){
            result = "caught";
        }
        assertEquals("caught", result);

    }
}