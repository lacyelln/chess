package dataaccess;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MySqlGameDataAccessTest {

    MySqlGameDataAccess dataAccessG;
    MySqlUserDataAccess dataAccessU;
    String gameName = "Try Game";
    UserData user = new UserData("tacos", "password", "e");

    MySqlGameDataAccessTest() {
        try {
            dataAccessG = new MySqlGameDataAccess();
            dataAccessU = new MySqlUserDataAccess();
        }
        catch (DataAccessException e){
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccessG.deleteAllGames();
        dataAccessU.deleteAllUsers();
    }

    @Test
    void createGameSuccess() {
        assertDoesNotThrow(()-> dataAccessG.createGame(gameName));
        assertTrue(() -> {
            try {
                return dataAccessG.gameExists(gameName);
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void createGameFail(){
        assertDoesNotThrow(()-> dataAccessG.createGame(gameName));
        assertThrows(DataAccessException.class, () -> dataAccessG.createGame(gameName), "already exists.");
    }

    @Test
    void getGameSuccess() {
        assertDoesNotThrow(()-> {
            GameData game = dataAccessG.createGame(gameName);
            GameData other = dataAccessG.getGame(game.gameID());
            assertEquals(game, other);
        });
    }

    @Test
    void getGameFail() {
        assertThrows(DataAccessException.class, ()-> dataAccessG.getGame(1234));
    }

    @Test
    void listGamesSuccess() {
        assertDoesNotThrow(()-> {
            dataAccessG.createGame(gameName);
            dataAccessG.createGame("tacos");
            dataAccessG.createGame("cats");
            ArrayList<GameData> games = dataAccessG.listGames();
            assertEquals(games.size(), 3);
        });

    }

    @Test
    void updateGameSuccess() {
        assertDoesNotThrow(()-> {
            GameData game = dataAccessG.createGame(gameName);
            dataAccessU.createUser(user);
            dataAccessG.updateGame(game.gameID(), user.username(), "WHITE");
            GameData g = dataAccessG.getGame(game.gameID());
            assertNotEquals(g, game);
        });
    }

    @Test
    void updateGameFail() {
        GameData game = assertDoesNotThrow(()-> dataAccessG.createGame(gameName));
        assertThrows(DataAccessException.class, () -> dataAccessG.updateGame(game.gameID(), "Taco", "WHITE"));
    }

    @Test
    void deleteAllGames() {
        assertDoesNotThrow(() ->
            {dataAccessG.createGame(gameName);
            assertTrue(dataAccessG.gameExists(gameName), "User exists.");
            assertDoesNotThrow(() -> dataAccessG.deleteAllGames());
            assertFalse(dataAccessG.gameExists(gameName), "No longer exists.");
        });
    }
}