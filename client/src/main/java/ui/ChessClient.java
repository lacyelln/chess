package ui;


import model.AuthData;
import model.GameData;
import model.UserData;
import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

public class ChessClient {
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private ServerFacade server;
    private AuthData auth;

    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.auth = null;
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
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3 && state == State.SIGNEDOUT) {
            UserData user = new UserData(params[0], params[1], params[2]);
            try {
                this.auth = server.register(user);
            } catch (ResponseException e){
                throw new ResponseException("Invalid registration, user already registered.");
            }
            System.out.println("Registering ...");
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", user.username());
        }
        throw new ResponseException("Error register with username, password, and email while signed out.");
    }

    public String login(String ... params) throws ResponseException {
        if (params.length == 2){
            if (state == State.SIGNEDIN){
                return String.format("You are already signed in.");
            }
            System.out.println("Logging in ...");
            UserData user = new UserData(params[0], params[1], null);
            this.auth = server.login(user);
            state = State.SIGNEDIN;
            return String.format("You are signed in as %s", user.username());
        }
        throw new ResponseException("Please login with a registered username and password.");
    }

    public String createGame(String... params) throws ResponseException {
        try {
            if (params.length == 1) {
                int gameID = server.createGame(this.auth, params[0]);
                return String.format("You have created game '%s' with gameID %d", params[0], gameID);
            }
            return String.format("Please name your game one word.");
        } catch (ResponseException e){
            throw new ResponseException("Error creating game, create a game with an unused game name that is one word.");
        }
    }

    public String listGames() throws ResponseException {
      try{
          GameData[] games = server.listGames(this.auth);
          List<GameData> gameList = Arrays.stream(games).toList();
          System.out.println("Here are the games you have created:");
          for (int i = 0; i < games.length; i++){
              System.out.print(String.format("- %s, %d\n", gameList.get(i).gameName(), gameList.get(i).gameID()));
          }
          return "Would you like to create a new game?";
      } catch (ResponseException e){
          throw new ResponseException("Error grabbing games");
      }
    }

    public String joinGame(String ... params) throws ResponseException{
        System.out.println("joining game...");
        try {
            if (params.length == 2) {
                int gameID = Integer.parseInt(params[0]);
                server.joinGame(this.auth, gameID, params[1]);
                ChessBoard.main(params);
                return String.format("You have successfully joined game %d as a %s player.", gameID, params[1]);
            }
            return String.format("Incorrect number of parameters, please provide a gameID and a team color.");
        } catch (ResponseException e){
            if (e.getMessage().contains("bad request")){
                 return "Try a different game.";
            } else if (e.getMessage().contains("already taken")) {
                return "The player color you selected is already taken.";
            } else if (e.getMessage().contains("unauthorized")); {
                return "You are not authorized to join this game.";
            }

        }

    }

    public String observeGame(String ... params) throws ResponseException{
        if(params.length == 1){
            ChessBoard.main(params);
            return String.format("You are observing the chess game of %s", params[0]);
        }
        return "Error";
    }

    public String logout() throws ResponseException {
        try {
            server.logout(this.auth);
            state = State.SIGNEDOUT;
            return String.format("You have successfully logged out.");
        }
        catch (ResponseException e){
            throw new ResponseException("Unable to logout.");
        }
    }



    public String help() {
        System.out.println(SET_TEXT_COLOR_BLUE);
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
