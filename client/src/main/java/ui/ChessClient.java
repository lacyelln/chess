package ui;


import model.AuthData;
import model.GameData;
import model.UserData;
import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.DataFormatException;

public class ChessClient {
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private ServerFacade server;
    private AuthData auth;
    private HashMap<Integer, Integer> numberToId;

    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.auth = null;
        this.numberToId = new HashMap<>();
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
                return String.format("You have created game '%s'.", params[0]);
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
          if (gameList.isEmpty()){
              return "You have not created any games.";
          }
          System.out.println("Here are the games you have created:");
          for (int i = 0; i < games.length; i++){
              numberToId.put(i+1, gameList.get(i).gameID());
              String white = gameList.get(i).whiteUsername();
              if (white == null){
                  white = "None";
              }
              String black = gameList.get(i).blackUsername();
              if (black == null){
                  black = "None";
              }

              System.out.print(String.format("%d) %s with players: white(%s) and black(%s).\n", i+1,
                      gameList.get(i).gameName(), white , black));
          }
          return "What would you like to do with your games?";
      } catch (ResponseException e){
          throw new ResponseException("Error grabbing games");
      }
    }

    public String joinGame(String ... params) throws NumberFormatException, ResponseException{
        System.out.println("joining game...");
        int gameNumber = 0;
        try {
            if (params.length == 2) {
                try {
                    gameNumber = Integer.parseInt(params[0]);
                } catch (NumberFormatException e) {
                    return "Please select a number from the list of games.";
                }
                if (numberToId.containsKey(gameNumber)) {
                    int gameID = numberToId.get(gameNumber);
                    server.joinGame(this.auth, gameID, params[1]);
                    ChessBoard.main(params);
                    return String.format("You have successfully joined game %d as a %s player.", gameID, params[1]);
                }
                return "Please select a valid game number";
            }
            return String.format("Incorrect number of parameters, please provide a gameID and a team color.");
        } catch (ResponseException e){
            if (e.getMessage().contains("bad request")){
                 return "Please try again with a valid game number and a valid team color (white/black).";
            } else if (e.getMessage().contains("already taken")) {
                return "The player color you selected is already taken.";
            } else {
                return "You are not authorized to join this game.";
            }

        }

    }

    public String observeGame(String ... params) throws ResponseException{
        int gameNumber;
        if(params.length == 1){
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Please select a number from the list of games.";
            }
            if (numberToId.containsKey(gameNumber)) {
                ChessBoard.main(params);
                return String.format("You are observing the chess game of %s", params[0]);
            }
            return "Please provide a valid existing game from your game list.";
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
                    join <list number> [WHITE]/[BLACK] - a game
                    observed <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """;
    }
}
