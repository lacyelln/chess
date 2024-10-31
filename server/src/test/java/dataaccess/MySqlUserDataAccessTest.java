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
        //dataAccess.deleteAllAuth();
        dataAccess.deleteAllUsers();
    }

    @Test
    void createUserTestSuccess()  {
        try {
            assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
            assertTrue(dataAccess.userExists("b"), "Success!");

        } catch (DataAccessException e) {
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @Test
    void createUserTestFail() {
        try {
            assertThrows(DataAccessException.class, () -> dataAccess.createUser(new UserData("a", "", "c")),
                    "No password");
            assertFalse(dataAccess.userExists("a"), "User isn't added.");

        } catch (DataAccessException e) {
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }


    @Test
    void getUserTestSuccess()  {
        try {
            assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
            assertEquals(dataAccess.getUser(tryUser), tryUser);

        } catch (DataAccessException e) {
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @Test
    void getUserTestFail() {
        try {
            assertDoesNotThrow(() -> dataAccess.createUser(tryUser));
            assertNotEquals(dataAccess.getUser(tryUser), new UserData("tacos", "dogs", "cats"));
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