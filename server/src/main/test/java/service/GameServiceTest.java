package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    UserDAO mUser = new MemoryUserDAO();
    AuthDAO mAuth = new MemoryAuthDAO();
    GameDAO mGame = new MemoryGameDAO();
    UserData user = new UserData("a", "a", "a");
    GameService service = new GameService(mAuth, mGame);
    UserService userService = new UserService(mUser, mAuth);
    GameData game = new GameData(0, null, null, "happy chess", null);


    @Test
    void createGame() throws DataAccessException, UnauthorizedException, BadRequestException {
        AuthData authData = userService.register(user);
        service.createGame(authData.authToken(), "happy chess");
        boolean gameCreated = true;
        assertTrue(gameCreated);
    }


    @Test
    void createGameFailed() throws BadRequestException, DataAccessException{
        String authToken = null;
        boolean gameCreated;
        try {
            service.createGame(authToken, game.gameName());
            gameCreated = true;
        }
        catch (BadRequestException | UnauthorizedException e){
            gameCreated = false;

        }
        assertFalse(gameCreated);
    }


    @Test
    void listGamesSuccess() throws DataAccessException{
        AuthData authData = userService.register(user);
        boolean var = false;
        try {
            service.createGame(authData.authToken(), "taco");
            service.createGame(authData.authToken(), "cola");
            service.createGame(authData.authToken(), "burger");
            service.listGames(authData.authToken());
            var = true;

        }
        catch (DataAccessException | UnauthorizedException e) {
            var = false;
        }
        assertTrue(var);
    }

    @Test
    void listGamesFailed() throws DataAccessException {
        AuthData authData = userService.register(user);
        boolean var = false;
        try {
            service.createGame(authData.authToken(), null);
            service.createGame(authData.authToken(), "cola");
            service.createGame(null, "burger");
            service.listGames(authData.authToken());
            var = true;

        }
        catch (DataAccessException | UnauthorizedException | BadRequestException e) {
            var = false;
        }
        assertFalse(var);

    }

    @Test
    void joinGameSuccess(){
        String authToken = "lalala";
        boolean var;
        try {
            GameData game = service.createGame(authToken, "taco");
            service.joinGame(authToken, game.gameID(), "WHITE");
            var = true;
        }
        catch (DataAccessException | BadRequestException | UnauthorizedException e){
            var = false;
        }
        assertTrue(var);
    }

    @Test
    void joinGameFailed(){
        String authToken = "lalala";
        boolean var;
        try {
            GameData game = service.createGame(authToken, "taco");
            service.joinGame(authToken, game.gameID(), "WHITE");
            var = true;
        }
        catch (DataAccessException | BadRequestException | UnauthorizedException e){
            var = false;
        }
        assertFalse(var);
    }

    @Test
    void deleteSuccess() throws DataAccessException, UnauthorizedException {
        UserData u = new UserData("a", "a", "a");
        GameData g = new GameData(123, null, null, "a", null);
        var user = new UserService(mUser, mAuth);
        var auth = user.register(u);
        var game = new GameService(mAuth, mGame);
        game.createGame(auth.authToken(), g.gameName());
        game.delete();
        user.delete();
        assertDoesNotThrow(game::delete);
        assertDoesNotThrow(user::delete);
    }

}


