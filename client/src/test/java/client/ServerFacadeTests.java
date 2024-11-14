package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.UserService;
import ui.ResponseException;
import ui.ServerFacade;

import java.util.List;
import java.util.zip.DataFormatException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    UserData user = new UserData("lacy", "miller", "lemill@gmail.com");


    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:"+port);
    }

    @BeforeEach
    public void setUp() throws ResponseException {
        serverFacade.deleteAll();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerSuccess() throws ResponseException {
        AuthData auth = serverFacade.register(user);
        Assertions.assertNotNull(auth);
    }

    @Test
    public void registerFailed()  {
        Assertions.assertThrows(ResponseException.class, ()->serverFacade.register(new UserData("lacy", "miller", null)));
    }

    @Test
    public void loginSuccess() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        Assertions.assertNotNull(auth);
    }

    @Test
    public void loginFailed(){
        Assertions.assertThrows(ResponseException.class, ()-> serverFacade.login(new UserData("k", "c", "w")));
    }

    @Test
    public void createGameSuccess(){
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        int gameID = Assertions.assertDoesNotThrow(()-> serverFacade.createGame(auth,"firstGame"));
        Assertions.assertNotEquals(0, gameID, "correctly generated a gameID");
    }

    @Test
    public void createGameFail(){
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        Assertions.assertDoesNotThrow(()-> serverFacade.createGame(auth, "firstGame"));
        Assertions.assertThrows(ResponseException.class, ()->serverFacade.createGame(auth,"firstGame"));

    }

    @Test
    public void listGamesSuccess(){
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(auth,"firstGame"));
        Assertions.assertDoesNotThrow(()-> serverFacade.createGame(auth,"secondGame"));
        GameData[] gameInfo = Assertions.assertDoesNotThrow(()->serverFacade.listGames(auth));
        Assertions.assertEquals(2, gameInfo.length);
    }

    @Test
    public void listGamesFail(){
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(auth,"firstGame"));
        Assertions.assertDoesNotThrow(()-> serverFacade.createGame(auth,"secondGame"));
        Assertions.assertThrows(ResponseException.class, () ->serverFacade.listGames(new AuthData("not-a-real-auth", "username")));
    }

    @Test
    public void joinGameSuccess(){
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        int gameID = Assertions.assertDoesNotThrow(() -> serverFacade.createGame(auth,"testGame"));
        Assertions.assertDoesNotThrow(()-> serverFacade.joinGame(auth, gameID, "WHITE"));
    }

    @Test
    public void joinGameFail() {
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        int gameID = Assertions.assertDoesNotThrow(() -> serverFacade.createGame(auth, "testGame"));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(auth, gameID, "brown"));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(auth, 23, "WHITE"));
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(new AuthData("auth", user.username()), gameID, "WHITE"));
    }

    @Test
    public void logoutSuccess(){
        Assertions.assertDoesNotThrow(() -> serverFacade.register(user));
        AuthData auth = Assertions.assertDoesNotThrow(() -> serverFacade.login(user));
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(auth));
    }

    @Test
    public void logoutFail(){
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(new AuthData("not an auth", "no user")));
    }
}
