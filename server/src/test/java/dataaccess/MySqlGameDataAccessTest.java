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
    String gameName = "Try Game";

    MySqlGameDataAccessTest() {
        try {
            dataAccessG = new MySqlGameDataAccess();
        }
        catch (DataAccessException e){
            throw Spark.halt(500, "{\"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccessG.deleteAllGames();
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
    }

    @Test
    void updateGameFail() {
    }

    @Test
    void deleteAllGames() {
    }
}