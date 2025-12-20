package model.dao;

import model.dto.ProductionManagerDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for managing Production Manager entities.
 * <p>
 * This class provides the persistence layer for the "Production_Manager" table.
 * It manages the association between a generic {@link model.dto.UserDTO} and the
 * Production Manager role, allowing the user to manage Productions and Teams.
 * </p>
 *
 * @see model.dto.ProductionManagerDTO
 * @see javax.sql.DataSource
 */
public class ProductionManagerDAO implements GenericDAO<ProductionManagerDTO, Integer> {

    private static final List<String> ALLOWED_ORDER_COLUMNS = Arrays.asList(
            "PmID", "UserID"
    );

    private static final String DEFAULT_ORDER_COLUMN = "PmID";

    private final DataSource dataSource;

    /**
     * Constructs a new {@code ProductionManagerDAO} with the specified DataSource.
     *
     * @param dataSource the DataSource used to obtain database connections.
     * @throws NullPointerException if the provided dataSource is null.
     */
    public ProductionManagerDAO(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource cannot be null");
    }

    /**
     * Persists a Production Manager role association to the database.
     * <p>
     * If {@code PmID} is 0, a new record is created (INSERT).
     * </p>
     *
     * @param pm the DTO object to save.
     * @throws SQLException if a database access error occurs.
     * @throws IllegalArgumentException if the DTO is null or UserID is invalid.
     */
    @Override
    public void save(ProductionManagerDTO pm) throws SQLException {
        if (pm == null) {
            throw new IllegalArgumentException("ProductionManagerDTO cannot be null");
        }
        if (pm.getUserID() <= 0) {
            throw new IllegalArgumentException("A valid UserID is required to create a Production Manager profile.");
        }

        String sql;
        if (pm.getPmID() == 0) {
            // INSERT
            sql = "INSERT INTO Production_Manager (UserID) VALUES (?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, pm.getUserID());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating Production Manager profile failed, no rows affected.");
                }

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pm.setPmID(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating Production Manager profile failed, no ID obtained.");
                    }
                }
            }
        }
    }

    /**
     * Deletes a Production Manager profile by their ID.
     * <p>
     * <b>Warning:</b> This removes the "Production Manager" role.
     * The underlying {@code User} account is <b>not</b> deleted.
     * </p>
     *
     * @param pmID the unique identifier of the Production Manager profile.
     * @return {@code true} if deleted successfully, {@code false} otherwise.
     * @throws SQLException if a database error occurs.
     */
    @Override
    public boolean delete(Integer pmID) throws SQLException {
        if (pmID == null || pmID <= 0) {
            throw new IllegalArgumentException("Invalid PmID");
        }

        String sql = "DELETE FROM Production_Manager WHERE PmID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pmID);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves a Production Manager profile by its unique ID.
     *
     * @param pmID the unique identifier.
     * @return the ProductionManagerDTO or {@code null} if not found.
     * @throws SQLException if a database error occurs.
     */
    @Override
    public ProductionManagerDTO getByID(Integer pmID) throws SQLException {
        if (pmID == null || pmID <= 0) return null;

        String sql = "SELECT * FROM Production_Manager WHERE PmID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, pmID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractPmFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the Production Manager profile associated with a specific User ID.
     *
     * @param userID the ID of the generic User.
     * @return the ProductionManagerDTO or {@code null} if not found.
     * @throws SQLException if a database error occurs.
     */
    public ProductionManagerDTO getByUserID(Integer userID) throws SQLException {
        if (userID == null || userID <= 0) return null;

        String sql = "SELECT * FROM Production_Manager WHERE UserID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractPmFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all Production Manager profiles.
     *
     * @param order the column to sort by.
     * @return a collection of all Production Managers.
     * @throws SQLException if a database error occurs.
     */
    @Override
    public Collection<ProductionManagerDTO> getAll(String order) throws SQLException {
        String actualOrder = (order != null && ALLOWED_ORDER_COLUMNS.contains(order)) ? order : DEFAULT_ORDER_COLUMN;
        String sql = "SELECT * FROM Production_Manager ORDER BY " + actualOrder;

        Collection<ProductionManagerDTO> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractPmFromResultSet(rs));
            }
        }
        return list;
    }

    // --- Helper Methods ---

    private ProductionManagerDTO extractPmFromResultSet(ResultSet rs) throws SQLException {
        ProductionManagerDTO pm = new ProductionManagerDTO();
        pm.setPmID(rs.getInt("PmID"));
        pm.setUserID(rs.getInt("UserID"));
        return pm;
    }
}