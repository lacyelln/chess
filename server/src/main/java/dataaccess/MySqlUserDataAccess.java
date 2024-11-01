package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import spark.Spark;

import java.sql.SQLException;
import static java.sql.Types.NULL;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlUserDataAccess implements UserDAO {
    public MySqlUserDataAccess() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
    public boolean userExists(String username) throws DataAccessException {
        var query = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException y) {
            throw new DataAccessException("Error checking if user exists: " + y.getMessage());
        }
    }



    @Override
    public void createUser(UserData u) throws DataAccessException {
        if (userExists(u.username())) {
            throw new DataAccessException("User with username '" + u.username() + "' already exists.");
        }
        if (u.password() == null || u.password().isEmpty() || u.username() == null || u.username().isEmpty()) {
            throw new DataAccessException("Username/password cannot be empty or null.");
        }
        String hashedPassword = BCrypt.hashpw(u.password(), BCrypt.gensalt());
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, u.username(), hashedPassword, u.email());
    }

    public UserData getUser(UserData u) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, u.username());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString("username");
                        String password = rs.getString("password");
                        String email = rs.getString("email");
                        return new UserData(username, password, email); // Create UserData with retrieved fields
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAllUsers() throws DataAccessException {
        var statement = "DELETE FROM user";
        executeUpdate(statement);

    }

    public void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof UserData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
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
