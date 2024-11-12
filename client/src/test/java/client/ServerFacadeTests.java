package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.UserService;
import ui.ResponseException;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;


    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:"+port);
        serverFacade.deleteAll();

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }



    @Test
    public void registerSuccess() throws ResponseException {
        AuthData auth = serverFacade.register(new UserData("lacy", "miller", "lemill@gmail.com"));
        Assertions.assertNotNull(auth);
    }

}
