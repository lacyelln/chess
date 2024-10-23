package Service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void createGame() {
    }

    @Test
    void listGames() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void delete() throws DataAccessException {
        UserDAO mUser = new MemoryUserDAO();
        AuthDAO mAuth = new MemoryAuthDAO();
        GameDAO mGame = new MemoryGameDAO();
        UserData u = new UserData("a", "a", "a");
        GameData g = new GameData(123, null, null, "a", null);
        var user = new UserService(mUser, mAuth);
        var auth = user.register(u);
        var game = new GameService(mAuth, mGame, mUser);
        game.createGame(auth, g);
        var result = game.delete();
        assertEquals(new HashMap<>(), result);

    }
}