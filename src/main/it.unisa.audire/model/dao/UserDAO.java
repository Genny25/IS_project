package model.dao;

import model.dto.UserDTO;
import com.password4j.Argon2Function;
import com.password4j.Password;
import com.password4j.types.Argon2;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Data Access Object (DAO) for managing User entities.
 * <p>
 * This class handles all database operations regarding users, including
 * Create, Read, Update, and Delete (CRUD) operations, as well as password hashing.
 * </p>
 */
public class UserDAO implements GenericDAO<UserDTO, Integer> {

    // Configuration for Argon2 password hashing
    private static final Argon2Function ARGON_2_ID = Argon2Function.getInstance(19, 2, 1, 32, Argon2.ID);

    // Whitelist for sorting columns to prevent SQL Injection
    private static final List<String> ALLOWED_ORDER_COLUMNS = Arrays.asList(
            "UserID", "FirstName", "LastName", "PasswordHash", "PhoneNumber", "Role", "Email", "RegistrationDate"
    );

    private static final String DEFAULT_ORDER_COLUMN = "UserID";

    private final DataSource dataSource;

    /**
     * Constructs a new UserDAO with the specified DataSource.
     *
     * @param dataSource the DataSource to be used for database connections. Cannot be null.
     */
    public UserDAO(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource cannot be null");
    }

    /**
     * Persists a User object to the database.
     * <p>
     * If the User ID is 0, a new record is inserted.
     * If the User ID is greater than 0, the existing record is updated.
     * </p>
     *
     * @param user the UserDTO object to save or update.
     * @throws SQLException if a database access error occurs.
     * @throws IllegalArgumentException if the user object or required fields are invalid.
     */
    public void save(UserDTO user) throws SQLException {
        if (user == null || user.getFirstName() == null || user.getFirstName().trim().isEmpty() ||
                user.getLastName() == null || user.getLastName().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty() ||
                user.getRole() == null || user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("User, FirstName, LastName, PasswordHash, PhoneNumber, Role or Email cannot be null or empty.");
        }

        String sql;
        if (user.getUserID() == 0) {
            // INSERT Operation
            sql = "INSERT INTO User(FirstName, LastName, PasswordHash, PhoneNumber, Role, Email, RegistrationDate) VALUES(?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getPasswordHash());
                ps.setString(4, user.getPhoneNumber());
                ps.setString(5, user.getRole().name());
                ps.setString(6, user.getEmail());
                ps.setTimestamp(7, (user.getRegistrationDate() != null) ? Timestamp.valueOf(user.getRegistrationDate()) : Timestamp.valueOf(LocalDateTime.now()));

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserID(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }
        } else {
            // UPDATE Operation
            sql = "UPDATE User SET FirstName = ?, LastName = ?, PasswordHash = ?, PhoneNumber = ?, Role = ?, Email = ? WHERE UserID = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getPasswordHash());
                ps.setString(4, user.getPhoneNumber());
                ps.setString(5, user.getRole().name());
                ps.setString(6, user.getEmail());
                ps.setInt(7, user.getUserID());

                ps.executeUpdate();
            }
        }
    }

    /**
     * Deletes a user from the database by their unique ID.
     *
     * @param userID the unique identifier of the user to delete.
     * @return {@code true} if the user was successfully deleted, {@code false} otherwise.
     * @throws SQLException if a database access error occurs.
     * @throws IllegalArgumentException if the userID is null or zero.
     */
    public boolean delete(Integer userID) throws SQLException {
        if (userID == null || userID == 0) {
            throw new IllegalArgumentException("UserID cannot be null or empty.");
        }

        String sql = "DELETE FROM User WHERE UserID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Retrieves a user by their unique database ID.
     *
     * @param userID the unique identifier of the user.
     * @return the UserDTO object if found, or {@code null} if not found.
     * @throws SQLException if a database access error occurs.
     * @throws IllegalArgumentException if the userID is null or non-positive.
     */
    public UserDTO getByID(Integer userID) throws SQLException {
        if (userID == null || userID <= 0) {
            throw new IllegalArgumentException("UserID cannot be null or negative.");
        }

        String sql = "SELECT * FROM User WHERE UserID = ?";
        UserDTO userDTO = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userDTO = extractUserFromResultSet(rs);
                }
            }
        }
        return userDTO;
    }

    /**
     * Retrieves a user by their email address.
     * <p>This method is typically used during the login process.</p>
     *
     * @param email the email address to search for.
     * @return the UserDTO object if found, or {@code null} if not found.
     * @throws SQLException if a database access error occurs.
     */
    public UserDTO getByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        String sql = "SELECT * FROM User WHERE Email = ?";
        UserDTO userDTO = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userDTO = extractUserFromResultSet(rs);
                }
            }
        }
        return userDTO;
    }

    /**
     * Retrieves all users from the database, sorted by the specified column.
     *
     * @param order the column name to sort by. If invalid or null, defaults to "UserID".
     * @return a Collection of UserDTO objects.
     * @throws SQLException if a database access error occurs.
     */
    public Collection<UserDTO> getAll(String order) throws SQLException {
        Collection<UserDTO> users = new ArrayList<>();

        // Sanitize sort order to prevent SQL Injection
        String actualOrder = (order != null && ALLOWED_ORDER_COLUMNS.contains(order)) ? order : DEFAULT_ORDER_COLUMN;

        String sql = "SELECT * FROM User ORDER BY " + actualOrder;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        }
        return users;
    }

    /**
     * Helper method to map a ResultSet row to a UserDTO object.
     *
     * @param rs the ResultSet positioned at the current row.
     * @return a populated UserDTO.
     * @throws SQLException if a column is missing or a type mismatch occurs.
     */
    private UserDTO extractUserFromResultSet(ResultSet rs) throws SQLException {
        UserDTO user = new UserDTO();
        user.setUserID(rs.getInt("UserID"));
        user.setFirstName(rs.getString("FirstName"));
        user.setLastName(rs.getString("LastName"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setPhoneNumber(rs.getString("PhoneNumber"));
        user.setEmail(rs.getString("Email"));

        // Handle Enum Conversion safely
        String roleStr = rs.getString("Role");
        if (roleStr != null) {
            try {
                user.setRole(UserDTO.Role.valueOf(roleStr));
            } catch (IllegalArgumentException e) {
                user.setRole(null); // Or handle default role
            }
        }

        // Handle Timestamp conversion
        Timestamp ts = rs.getTimestamp("RegistrationDate");
        if (ts != null) {
            user.setRegistrationDate(ts.toLocalDateTime());
        }

        return user;
    }

    /**
     * Hashes a plain text password using the Argon2 algorithm.
     *
     * @param plainPassword the password to hash.
     * @return the hashed password string.
     */
    public String hashPassword(String plainPassword) {
        return Password.hash(plainPassword).addRandomSalt().with(ARGON_2_ID).getResult();
    }

    /**
     * Verifies a plain text password against a stored hash.
     *
     * @param plainPassword the input password.
     * @param storedHash the hashed password from the database.
     * @return {@code true} if matches, {@code false} otherwise.
     */
    public boolean verifyPassword(String plainPassword, String storedHash) {
        return Password.check(plainPassword, storedHash).with(ARGON_2_ID);
    }
}