package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class gameServiceTest {
    UserDAO mUser = new MemoryUserDAO();
    AuthDAO mAuth = new MemoryAuthDAO();
    GameDAO mGame = new MemoryGameDAO();
    UserData user = new UserData("a", "a", "a");
    gameservice service = new gameservice(mAuth, mGame);
    userservice userService = new userservice(mUser, mAuth);
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
    void listGamesSuccess() {
    }

    @Test
    void listGamesFailed(){

    }

    @Test
    void joinGameSuccess(){
        String authToken = "lalala";
        boolean var;
        try {
            GameData game = service.createGame(authToken, "taco");
            game = new GameData(game.GameID(), "WHITE", null, "taco", null);
            service.joinGame(authToken, game);
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
            game = new GameData(game.GameID(), "WHITE", null, "taco", null);
            service.joinGame(authToken, game);
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
        var user = new userservice(mUser, mAuth);
        var auth = user.register(u);
        var game = new gameservice(mAuth, mGame);
        game.createGame(auth.authToken(), g.gameName());
        game.delete();
        user.delete();
        assertDoesNotThrow(game::delete);
        assertDoesNotThrow(user::delete);
    }

}


