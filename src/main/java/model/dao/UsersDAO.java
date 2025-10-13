package model.dao;

import com.password4j.Argon2Function;
import com.password4j.Password;
import com.password4j.types.Argon2;
import model.dto.UsersDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for managing User entities in the database.
 * This class provides CRUD operations and additional user-related functionality
 * including password hashing and verification using Argon2 algorithm.
 *
 *
 * @author Gennaro Carmine Tozza
 */
public class UsersDAO implements GenericDAO<UsersDTO, Integer> {

    private static final Argon2Function ARGON_2_ID = Argon2Function.getInstance(19, 2, 1, 32, Argon2.ID);

    /**
     * List of database columns that are allowed for ORDER BY clauses.
     * This prevents SQL injection through ordering parameters.
     */
    private static final List<String> ALLOWED_ORDER_COLUMNS = Arrays.asList(
            "UserID", "FirstName", "LastName", "Email", "Role"
    );

    /**
     * Default column name used for ordering query results when no order is specified.
     */
    private static final String DEFAULT_ORDER_COLUMN = "UserID";

    /**
     * DataSource for obtaining database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new UsersDAO with the specified DataSource.
     *
     * @param dataSource the DataSource to use for database connections, must not be null
     * @throws NullPointerException if dataSource is null
     */
    public UsersDAO(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource cannot be null");
    }

    /**
     * Saves a user to the database. If the user has a UserID of 0, a new user is inserted.
     * Otherwise, the existing user is updated.
     *
     * <p>For INSERT operations, the generated UserID is automatically set in the UsersDTO object.</p>
     *
     * @param usersDTO the user data transfer object to save, must not be null
     * @throws IllegalArgumentException if usersDTO is null or if any required field
     *         (FirstName, LastName, Email, PasswordHash, Role) is null or empty
     * @throws SQLException if a database access error occurs or the insert/update operation fails
     */
    @Override
    public void save(UsersDTO usersDTO) throws SQLException {
        // Input validation: Ensure critical fields are not null or empty
        if (usersDTO == null || usersDTO.getFirstName() == null || usersDTO.getFirstName().trim().isEmpty() ||
                usersDTO.getLastName() == null || usersDTO.getLastName().trim().isEmpty() ||
                usersDTO.getEmail() == null || usersDTO.getEmail().trim().isEmpty() ||
                usersDTO.getPasswordHash() == null || usersDTO.getPasswordHash().trim().isEmpty() ||
                usersDTO.getRole() == null) {
            throw new IllegalArgumentException("User, FirstName, LastName, Email, PasswordHash, or Role cannot be null or empty.");
        }

        String sql;
        if (usersDTO.getUserID() == 0) { // New user (INSERT)
            sql = "INSERT INTO User (FirstName, LastName, PasswordHash, Role, Email ) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // Get generated ID
                ps.setString(1, usersDTO.getFirstName());
                ps.setString(2, usersDTO.getLastName());
                ps.setString(3, usersDTO.getPasswordHash());
                ps.setString(4, usersDTO.getRole().name()); // Store enum as string
                ps.setString(5, usersDTO.getEmail());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                // Retrieve the generated UserID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usersDTO.setUserID(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }
        } else { // Existing user (UPDATE)
            sql = "UPDATE User SET FirstName = ?, LastName = ?, PasswordHash = ?, Role = ?, Email = ? WHERE UserID = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, usersDTO.getFirstName());
                ps.setString(2, usersDTO.getLastName());
                ps.setString(3, usersDTO.getPasswordHash());
                ps.setString(4, usersDTO.getRole().name());
                ps.setString(5, usersDTO.getEmail());
                ps.setInt(6, usersDTO.getUserID());

                ps.executeUpdate();
            }
        }
    }

    /**
     * Deletes a user from the database by their UserID.
     *
     * @param id the UserID of the user to delete, must not be null or zero
     * @return true if the user was successfully deleted, false if no user was found with the given ID
     * @throws IllegalArgumentException if id is null or zero
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id == 0) {
            throw new IllegalArgumentException("UserID cannot be null or zero for deletion.");
        }

        String sql = "DELETE FROM Users WHERE UserID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Retrieves a user by their UserID.
     *
     * @param userID the UserID to search for, must be a positive integer
     * @return the UsersDTO object if found, null otherwise
     * @throws IllegalArgumentException if userID is null or less than or equal to zero
     * @throws SQLException if a database access error occurs
     */
    @Override
    public UsersDTO getById(Integer userID) throws SQLException {
        if (userID == null || userID <= 0) {
            throw new IllegalArgumentException("UserID must be a positive integer.");
        }

        String sql = "SELECT UserID, FirstName, LastName, PasswordHash, Role, Email FROM User WHERE UserID = ?";
        UsersDTO usersDTO = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usersDTO = extractUserFromResultSet(rs);
                }
            }
        }
        return usersDTO;
    }

    /**
     * Retrieves all users from the database, ordered by the specified column.
     *
     * @param order the column name to order by (must be one of the allowed columns),
     *              or null/empty for default ordering by UserID
     * @return a Collection of all users in the database
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Collection<UsersDTO> getAll(String order) throws SQLException {
        return getFilteredUsers(order, false, null, null, null, null);
    }

    /**
     * Retrieves users from the database with optional filtering and ordering.
     * Filters can be applied for firstName, lastName, email, and role. All text filters
     * use case-sensitive LIKE matching with wildcards.
     *
     * @param order the column name to order by (must be one of the allowed columns),
     *              or null/empty for default ordering by UserID
     * @param desc true for descending order, false for ascending
     * @param firstName filter users by first name (partial match), or null for no filtering
     * @param lastName filter users by last name (partial match), or null for no filtering
     * @param email filter users by email (partial match), or null for no filtering
     * @param role filter users by exact role match, or null for no filtering
     * @return a Collection of users matching the filter criteria
     * @throws SQLException if a database access error occurs
     */
    public Collection<UsersDTO> getFilteredUsers(String order, boolean desc, String firstName, String lastName, String email, UsersDTO.Roles role) throws SQLException {
        Collection<UsersDTO> userDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT UserID, FirstName, LastName, PasswordHash, Role, Email FROM User WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (firstName != null && !firstName.isEmpty()) {
            sql.append(" AND FirstName LIKE ?");
            params.add("%" + firstName + "%");
        }
        if (lastName != null && !lastName.isEmpty()) {
            sql.append(" AND LastName LIKE ?");
            params.add("%" + lastName + "%");
        }
        if (email != null && !email.isEmpty()) {
            sql.append(" AND Email LIKE ?");
            params.add("%" + email + "%");
        }
        if (role != null) {
            sql.append(" AND Role = ?");
            params.add(role.name());
        }

        String actualOrderColumn = DEFAULT_ORDER_COLUMN;
        if (order != null && !order.trim().isEmpty()) {
            String trimmedOrder = order.trim();
            if (ALLOWED_ORDER_COLUMNS.contains(trimmedOrder)) {
                actualOrderColumn = trimmedOrder;
            } else {
                System.err.println("Warning: Attempted to order by invalid column: '" + order + "'. Falling back to default order.");
            }
        }
        sql.append(" ORDER BY ").append(actualOrderColumn);
        if (desc) {
            sql.append(" DESC");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userDTOS.add(extractUserFromResultSet(rs));
                }
            }
        }
        return userDTOS;
    }

    /**
     * Verifies a plain text password against a stored Argon2 hash.
     *
     * @param plainTextPassword the plain text password to verify
     * @param storedHashPassword the stored Argon2 hash to check against
     * @return true if the password matches the hash, false otherwise
     */
    public boolean verifyPassword(String plainTextPassword, String storedHashPassword) {
        return Password.check(plainTextPassword, storedHashPassword).with(ARGON_2_ID);
    }

    /**
     * Returns the list of column names that are allowed for ordering query results.
     * This is used to prevent SQL injection through ORDER BY clauses.
     *
     * @return a new List containing the allowed column names
     */
    @Override
    public List<String> getAllowedOrderColumns() {
        return new ArrayList<>(ALLOWED_ORDER_COLUMNS);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address to search for, must not be null or empty
     * @return the UsersDTO object if found, null otherwise
     * @throws IllegalArgumentException if email is null or empty
     * @throws SQLException if a database access error occurs
     */
    public UsersDTO getUserByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }

        String sql = "SELECT UserID, FirstName, LastName, PasswordHash, Role, Email FROM Users WHERE Email = ?";
        UsersDTO usersDTO = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usersDTO = extractUserFromResultSet(rs);
                }
            }
        }
        return usersDTO;
    }

    /**
     * Extracts user data from a ResultSet and creates a UsersDTO object.
     * This is a private helper method used by query methods to convert database
     * rows into UsersDTO objects.
     *
     * @param rs the ResultSet positioned at a valid row containing user data
     * @return a UsersDTO object populated with data from the current ResultSet row
     * @throws SQLException if a database access error occurs or if the ResultSet
     *         does not contain the expected columns
     */
    private UsersDTO extractUserFromResultSet(ResultSet rs) throws SQLException {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUserID(rs.getInt("UserID"));
        usersDTO.setFirstName(rs.getString("FirstName"));
        usersDTO.setLastName(rs.getString("LastName"));
        usersDTO.setEmail(rs.getString("Email"));
        usersDTO.setPasswordHash(rs.getString("PasswordHash"));

        // Assuming your Role enum has a valueOf method that matches the DB string
        usersDTO.setRole(UsersDTO.Roles.valueOf(rs.getString("Role")));
        return usersDTO;
    }

    /**
     * Hashes a plain text password using Argon2id algorithm with a random salt.
     *
     * @param password the plain text password to hash
     * @return the Argon2 hash string that can be stored in the database
     */
    public String hashPassword(String password) {
        return Password.hash(password).addRandomSalt().with(ARGON_2_ID).getResult();
    }

    /**
     * Counts the total number of users in the database.
     *
     * @return the total count of users
     * @throws SQLException if a database access error occurs
     */
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM `User`";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}