package model.dao;

import model.dto.CastingDirectorDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for managing Casting Director entities.
 * <p>
 * This class provides the persistence layer for the "Casting_Director" table.
 * Since the Casting Director entity in the database primarily links a {@link model.dto.UserDTO}
 * to specific permissions and productions, this DAO focuses on managing these role associations.
 * </p>
 */
public class CastingDirectorDAO implements GenericDAO<CastingDirectorDTO, Integer> {

    private static final List<String> ALLOWED_ORDER_COLUMNS = Arrays.asList(
            "CdID", "UserID"
    );

    private static final String DEFAULT_ORDER_COLUMN = "CdID";

    private final DataSource dataSource;

    /**
     * Constructs a new {@code CastingDirectorDAO} with the specified DataSource.
     *
     * @param dataSource the DataSource used to obtain database connections.
     * @throws NullPointerException if the provided dataSource is null.
     */
    public CastingDirectorDAO(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource cannot be null");
    }

    /**
     * Persists a CastingDirector association to the database.
     * <p>
     * If {@code cdID} is 0, a new record is created (INSERT).
     * </p>
     *
     * @param cd the DTO object to save.
     * @throws SQLException if a database access error occurs.
     * @throws IllegalArgumentException if the DTO is null or UserID is invalid.
     */
    @Override
    public void save(CastingDirectorDTO cd) throws SQLException {
        if (cd == null) {
            throw new IllegalArgumentException("CastingDirectorDTO cannot be null");
        }
        if (cd.getUserID() <= 0) {
            throw new IllegalArgumentException("A valid UserID is required to create a Casting Director profile.");
        }

        String sql;
        if (cd.getCdID() == 0) {
            // INSERT
            sql = "INSERT INTO Casting_Director (UserID) VALUES (?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, cd.getUserID());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating Casting Director profile failed, no rows affected.");
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cd.setCdID(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating Casting Director profile failed, no ID obtained.");
                    }
                }
            }
        }
    }

    /**
     * Deletes a Casting Director profile by their ID.
     * <p>
     * <b>Note:</b> This removes the "Casting Director" role status.
     * The underlying {@code User} account is <b>not</b> deleted.
     * </p>
     *
     * @param cdID the unique identifier of the Casting Director profile.
     * @return {@code true} if deleted successfully, {@code false} otherwise.
     * @throws SQLException if a database error occurs.
     */
    @Override
    public boolean delete(Integer cdID) throws SQLException {
        if (cdID == null || cdID <= 0) {
            throw new IllegalArgumentException("Invalid CdID");
        }

        String sql = "DELETE FROM Casting_Director WHERE CdID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cdID);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves a Casting Director profile by its unique ID.
     *
     * @param cdID the unique identifier.
     * @return the CastingDirectorDTO or {@code null} if not found.
     * @throws SQLException if a database error occurs.
     */
    @Override
    public CastingDirectorDTO getByID(Integer cdID) throws SQLException {
        if (cdID == null || cdID <= 0) return null;

        String sql = "SELECT * FROM Casting_Director WHERE CdID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cdID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCdFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the Casting Director profile associated with a specific User ID.
     * <p>
     * Useful during login to check if the user has the Casting Director role
     * and to retrieve their specific CD-ID.
     * </p>
     *
     * @param userID the ID of the generic User.
     * @return the CastingDirectorDTO or {@code null} if not found.
     * @throws SQLException if a database error occurs.
     */
    public CastingDirectorDTO getByUserID(Integer userID) throws SQLException {
        if (userID == null || userID <= 0) return null;

        String sql = "SELECT * FROM Casting_Director WHERE UserID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCdFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all Casting Director profiles.
     *
     * @param order the column to sort by.
     * @return a collection of all Casting Directors.
     * @throws SQLException if a database error occurs.
     */
    @Override
    public Collection<CastingDirectorDTO> getAll(String order) throws SQLException {
        String actualOrder = (order != null && ALLOWED_ORDER_COLUMNS.contains(order)) ? order : DEFAULT_ORDER_COLUMN;
        String sql = "SELECT * FROM Casting_Director ORDER BY " + actualOrder;

        Collection<CastingDirectorDTO> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractCdFromResultSet(rs));
            }
        }
        return list;
    }

    // --- Helper Methods ---

    private CastingDirectorDTO extractCdFromResultSet(ResultSet rs) throws SQLException {
        CastingDirectorDTO cd = new CastingDirectorDTO();
        cd.setCdID(rs.getInt("CdID"));
        cd.setUserID(rs.getInt("UserID"));
        return cd;
    }
}