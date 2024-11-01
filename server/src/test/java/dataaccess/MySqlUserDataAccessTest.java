package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import static org.junit.jupiter.api.Assertions.*;

class MySqlUserDataAccessTest {

    MySqlUserDataAccess dataAccess;
    UserData tryUser = new UserData("b", "d", "e");

    MySqlUserDataAccessTest() {
        try {
            dataAccess = new MySqlUserDataAccess();
        }
        catch (DataAccessException e){
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess.deleteAllUsers();
    }

    @Test
    void createUserTestSuccess()  {
        assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
        assertDoesNotThrow(() -> dataAccess.userExists("b"), "user exists");
    }

    @Test
    void createUserTestFail() {
        assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
        assertThrows(DataAccessException.class, () -> dataAccess.createUser(tryUser), "No password");
    }


    @Test
    void getUserTestSuccess()  {
        try {
            assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
            assertEquals(dataAccess.getUser(tryUser.username()), tryUser);

        } catch (DataAccessException e) {
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @Test
    void getUserTestFail() {
        try {
            assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
            assertNotEquals(dataAccess.getUser(tryUser.username()), new UserData("tacos", "dogs", "cats"));
        } catch (DataAccessException e) {
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @Test
    void deleteUserTest() {
        try {
            assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
            assertTrue(dataAccess.userExists(tryUser.username()), "User exists.");
            dataAccess.deleteAllUsers();
            assertFalse(dataAccess.userExists(tryUser.username()), "No longer exists.");
            assertDoesNotThrow(() -> dataAccess.deleteAllUsers());
        } catch (DataAccessException e) {
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }



}