package server;
import Service.UserService;
import Service.GameService;
import com.google.gson.Gson;
import dataaccess.*;
import model.UserData;
import spark.*;

public class Server {
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final GameService gameService = new GameService(authDAO, gameDAO, userDAO);
    private final UserService userService = new UserService(userDAO, authDAO);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.post("/user", this::register);
//        Spark.delete("/db", gameService.delete);
//        Spark.post("/session", userService.login);
//        Spark.delete("/session", userService.logout);
//        Spark.get("/game", gameService.listGames);
//        Spark.post("/game", gameService.createGame);
//        Spark.put("/game", gameService.joinGame);


        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Object register(Request req, Response res) throws DataAccessException{
//        UserData user = new Gson().fromJson(req.body(), //?);
//        user = userService.register(user);
        return "great";
    }


   ;
}
