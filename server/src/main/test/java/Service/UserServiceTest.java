package Service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register() throws DataAccessException {
        var user = new UserData("a", "a", "a");
        var mUser = new MemoryUserDAO();
        var mAuth = new MemoryAuthDAO();
        UserService service = new UserService(mUser, mAuth);
        var authData = service.register(user);
        assertEquals(user.username(), authData.username());
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }
}