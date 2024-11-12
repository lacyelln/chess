package ui;


import java.util.Arrays;
import java.util.zip.DataFormatException;

public class ChessClient {
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        //server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
        } catch (DataFormatException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws DataFormatException {
        System.out.print("Registering");
        return null;
    }

    public String login(String ... params) throws DataFormatException {
        System.out.print("logging in");
        return null;
    }

    public String createGame(String... params) throws DataFormatException{
        System.out.print("creating game");
        return null;
    }

    public String listGames(String... params) throws DataFormatException{
      System.out.print("listing games");
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
