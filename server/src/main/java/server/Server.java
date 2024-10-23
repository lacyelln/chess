package server;
import Service.UserService;
import Service.GameService;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import spark.*;

import java.util.HashMap;

public class Server {
    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private final MemoryGameDAO gameDAO = new MemoryGameDAO();
    private final MemoryUserDAO userDAO = new MemoryUserDAO();
    private final GameService gameService = new GameService(authDAO, gameDAO);
    private final UserService userService = new UserService(userDAO, authDAO);
    private final Gson serializer = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.post("/user", this::register);
        Spark.delete("/db", this::delete);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);


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

    public Object register(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData auth = null;
        try {
            auth = userService.register(user);
            res.status(200);
        }
        catch (DataAccessException e){
            Spark.halt(500, "{\"message\"Error: " + e.getMessage() + "\"})");
        }
        catch (BadRequestException e){
            Spark.halt(400, serializer.toJson(new GenericError("Error: bad request")));
        }
        catch (AlreadyTakenException e){
            Spark.halt(403, serializer.toJson(new GenericError("Error: already taken")));
        }
        return serializer.toJson(auth);
    }

    public Object delete(Request req, Response res) throws DataAccessException {
        gameService.delete();
        userService.delete();
        return serializer.toJson(new HashMap<>());
    }

    public Object login(Request req, Response res) {
        UserData user = new Gson().fromJson(req.body(), UserData.class);
        AuthData authData = null;
        try {
            authData = userService.login(user);
        }
        catch (UnauthorizedException e){
            Spark.halt(401, serializer.toJson(new GenericError("Error: unauthorized")));
        }
        catch (DataAccessException e){
            Spark.halt(500, "{\"message\": \"Error:\"" + e.getMessage() + "\"})");
        }
        return serializer.toJson(authData);
    }

    public Object logout(Request req, Response res) throws DataAccessException{
        String authToken = req.headers("authorization");
        var result = userService.logout(authToken);
        return serializer.toJson(result);
    }

}
