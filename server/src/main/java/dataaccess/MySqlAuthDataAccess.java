package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import spark.Spark;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDataAccess implements AuthDAO{
    public MySqlAuthDataAccess() {
        DatabaseManager.configureDatabase();
    }

    public boolean authExists(String u) throws DataAccessException {
        var query = "SELECT COUNT(*) FROM auth WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setString(1, u);
            try (var rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException y) {
            throw new DataAccessException("Error checking if game exists: " + y.getMessage());
        }
    }

    @Override
    public AuthData createAuth(String u) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        String authToken = UUID.randomUUID().toString();
        executeUpdate(statement, authToken, u);
        return new AuthData(authToken, u);
    }

    @Override
    public AuthData getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String auth = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthData(auth, username);
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            return null;
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

        String statement = "DELETE FROM auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                int affectedRows = ps.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("No auth found with the provided authToken.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error while deleting auth: " + e.getMessage());
        }


    }

    @Override
    public void deleteAllAuth() throws DataAccessException {
        var statement = "DELETE FROM auth";
        executeUpdate(statement);
    }

    public void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) {ps.setString(i + 1, p);}
                    else if (param instanceof AuthData p) {ps.setString(i + 1, p.toString());}
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
