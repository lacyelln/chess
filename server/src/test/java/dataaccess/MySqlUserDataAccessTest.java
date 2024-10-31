package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import static org.junit.jupiter.api.Assertions.*;

class MySqlUserDataAccessTest {

    MySqlUserDataAccess dataAccess;

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
            assertDoesNotThrow(() -> dataAccess.createUser(new UserData("b", "d", "e")));
            assertTrue(dataAccess.userExists("b"), "Success!");

        } catch (DataAccessException e) {
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @Test
    void createUserTestFail() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> {
            dataAccess.createUser(new UserData("b", "", "e"));
        }, "Empty password");
        assertFalse(dataAccess.userExists("b"), "Should not exist.");
    }


}