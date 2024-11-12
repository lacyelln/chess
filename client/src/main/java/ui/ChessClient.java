package ui;


import model.UserData;
import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.zip.DataFormatException;

public class ChessClient {
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private ServerFacade server;

    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public Enum<State> getCurrentState(){
        return state;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataFormatException | ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws DataFormatException, ResponseException {
        System.out.println("Registering ...");
        if (params.length == 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            server.register(user);
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", user.username());
        }
        throw new DataFormatException();

    }

    public String login(String ... params) throws DataFormatException, ResponseException {
        if (params.length == 2){
            UserData user = new UserData(params[0], params[1], null);
            server.login(user);
            state = State.SIGNEDIN;
            return String.format("You are signed in as %s", user.username());

        }
        throw new DataFormatException("Formatting error");
    }

    public String createGame(String... params) throws DataFormatException{
        System.out.println("creating game");
        return null;
    }

    public String listGames(String... params) throws DataFormatException{
      System.out.println("listing games");
      return null;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                          register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                          login <USERNAME> <PASSWORD> - to play chess
                          quit - playing chess
                          help - with possible commands
                          """;
        }
        return """
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE][BLACK] - a game
                    observed <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """;
    }
}
