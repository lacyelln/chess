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
    void createUserTestFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            dataAccess.createUser(tryUser);
        }, "Empty password");
        assertFalse(dataAccess.userExists("b"), "Should not exist.");
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


}