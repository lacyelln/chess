package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import static org.junit.jupiter.api.Assertions.*;

class MySqlAuthDataAccessTest {

    MySqlUserDataAccess dataAccessU;
    MySqlAuthDataAccess dataAccessA;
    UserData tryUser = new UserData("b", "d", "e");

    MySqlAuthDataAccessTest() {
        dataAccessA = new MySqlAuthDataAccess();
        dataAccessU = new MySqlUserDataAccess();

    }

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccessU.deleteAllUsers();
        dataAccessA.deleteAllAuth();
    }

    @Test
    void createAuthSuccess() {
        assertDoesNotThrow(() -> {
            dataAccessU.createUser(tryUser);
            UserData user = dataAccessU.getUser(tryUser.username());
            dataAccessA.createAuth(user.username());
            assertTrue(dataAccessA.authExists(user.username()));
        });
    }

    @Test
    void createAuthFail() {
        assertThrows(DataAccessException.class, () -> dataAccessA.createAuth("alex"));
    }

    @Test
    void getAuthSuccess() {
        assertDoesNotThrow(() -> {
            dataAccessU.createUser(tryUser);
            AuthData auth = dataAccessA.createAuth(tryUser.username());
            assertTrue(dataAccessA.authExists(tryUser.username()));
            dataAccessA.getAuth(auth.authToken());
        });
    }

    @Test
    void getAuthFail() {
        assertThrows(DataAccessException.class, () -> {
            dataAccessU.createUser(tryUser);
            UserData user = dataAccessU.getUser(tryUser.username());
            dataAccessA.createAuth(user.username());
            dataAccessA.getAuth("auth.authToken()");
        });
    }

    @Test
    void deleteAuth() {
        assertDoesNotThrow(() -> {
            dataAccessU.createUser(tryUser);
            UserData user = dataAccessU.getUser(tryUser.username());
            AuthData auth = dataAccessA.createAuth(user.username());
            assertTrue(dataAccessA.authExists(user.username()));
            dataAccessA.deleteAuth(auth.authToken());
            assertFalse(dataAccessA.authExists(user.username()));
        });
    }

    @Test
    void deleteAllAuth() {
        assertDoesNotThrow(() ->{
            dataAccessU.createUser(tryUser);
            dataAccessU.createUser(new UserData("lee","jee","bee"));
            assertTrue(dataAccessU.userExists(tryUser.username()), "User exists.");
            AuthData auth = dataAccessA.createAuth(tryUser.username());
            AuthData auth2 = dataAccessA.createAuth("lee");
            assertTrue(dataAccessA.authExists(auth.username()), "Auth exists.");
            assertTrue(dataAccessA.authExists(auth2.username()), "Auth2 exists.");
            dataAccessA.deleteAllAuth();
            assertFalse(dataAccessA.authExists(auth.authToken()), "Auth should no longer exist.");

        });
    }

}