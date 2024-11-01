package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import spark.Spark;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlGameDataAccess implements GameDAO{
    public MySqlGameDataAccess() {
        DatabaseManager.configureDatabase();
    }

    public boolean gameExists(String g) throws DataAccessException {
        var query = "SELECT COUNT(*) FROM game WHERE gameName = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setString(1, g);
            try (var rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException y) {
            throw new DataAccessException("Error checking if game exists: " + y.getMessage());
        }
    }
    public boolean gameIdExists(int g) throws DataAccessException {
        var query = "SELECT COUNT(*) FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setInt(1, g);
            try (var rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException y) {
            throw new DataAccessException("Error checking if gameID exists: " + y.getMessage());
        }
    }

    @Override
    public GameData createGame(String g) throws DataAccessException {
        if(gameExists(g)){
            throw new AlreadyTakenException();
        }
        var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?, ?)";
        ChessGame game = new ChessGame();
        var json = new Gson().toJson(game);
        int gameID = MemoryGameDAO.generateRandomNumber(1, 1000);
        executeUpdate(statement, gameID, null, null, g, json);
        return getGame(gameID);
    }

    @Override
    public GameData getGame(Integer g) throws DataAccessException {
        if(!gameIdExists(g)){
            throw new BadRequestException();
        }
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, g);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String json = rs.getString("json");
                        ChessGame game = new Gson().fromJson(json, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String json = rs.getString("json");
                        ChessGame game = new Gson().fromJson(json, ChessGame.class);
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }
        return games;

    }

    @Override
    public void updateGame(Integer g, String u, String p) throws DataAccessException {
        UserDAO uDao = new MySqlUserDataAccess();
        if(uDao.getUser(u) == null) {
            throw new DataAccessException("user not found");
        }
        GameData currentGame = getGame(g);
        String statement;
        if (Objects.equals(p, "WHITE")){
            if (currentGame.whiteUsername() == null){
                statement = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
            }
            else{
                throw new AlreadyTakenException();
            }
        }
        else if (Objects.equals(p, "BLACK")) {
            if (currentGame.blackUsername() == null) {
                statement = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
            }
            else{
                throw new AlreadyTakenException();
            }
        }
        else {
            throw new DataAccessException("Not a black or white user");
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, u);
                ps.setInt(2, g);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving game: " + e.getMessage());
        }



    }

    @Override
    public void deleteAllGames() throws DataAccessException {
        var statement = "DELETE FROM game";
        executeUpdate(statement);
    }

    public void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p);}
                    else if (param instanceof Integer p) {ps.setInt(i + 1, p);}
                    else if (param instanceof GameData p) {ps.setString(i + 1, p.toString());}
                    else if (param == null) {ps.setNull(i + 1, NULL);}
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    rs.getInt(1);
                }

            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to execute update.");
        }
    }
}
