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
    void createGame_failed(){

    }

    @Test
    void listGames_success() {
    }

    @Test
    void listGames_failed(){

    }

    @Test
    void joinGame_success(){
    }

    @Test
    void joinGame_failed(){

    }

    @Test
    void delete_success() throws DataAccessException, BadRequestException, UnauthorizedException {
        UserDAO mUser = new MemoryUserDAO();
        AuthDAO mAuth = new MemoryAuthDAO();
        GameDAO mGame = new MemoryGameDAO();
        UserData u = new UserData("a", "a", "a");
        GameData g = new GameData(123, null, null, "a", null);
        var user = new UserService(mUser, mAuth);
        var auth = user.register(u);
        var game = new GameService(mAuth, mGame);
        game.createGame(auth.authToken(), g);
        game.delete();
        assertDoesNotThrow(game::delete);
    }

    @Test
    void delete_failed(){

    }
}