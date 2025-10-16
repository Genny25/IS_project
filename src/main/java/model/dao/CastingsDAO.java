package model.dao;

import model.dto.CastingsDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author Gennaro Carmine Tozza
 */
public class CastingsDAO implements GenericDAO<CastingsDTO, Integer> {

    /**
     * List of database columns that are allowed for ORDER BY clauses.
     * This prevents SQL injection through ordering parameters.
     */
    private static final List<String> ALLOWED_ORDER_COLUMNS = Arrays.asList(
            "CastingID", "Location", "Category", "Description", "Date", "DeadLine", "Title", "CdID", "ProductionID"
    );

    /**
     * Default column name used for ordering query results when no order is specified.
     */
    private static final String DEFAULT_ORDER_COLUMN = "CastingID";

    /**
     * DataSource for obtaining database connections.
     */
    private final DataSource dataSource;

    /**
     * Constructs a new CastingsDAO with the specified DataSource.
     *
     * @param dataSource the DataSource to use for database connections, must not be null
     * @throws NullPointerException if dataSource is null
     */
    public CastingsDAO(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource cannot be null");
    }

    /**
     * Saves a casting to the database. If the casting has a CastingID of 0, a new casting is inserted.
     * Otherwise, the existing casting is updated.
     *
     * <p>For INSERT operations, the generated CastingID is automatically set in the CastingsDTO object.</p>
     *
     * @param castingsDTO the casting data transfer object to save, must not be null
     * @throws IllegalArgumentException if castingsDTO is null or if any required field
     *         (Location, Category, Description, Title, CdID, ProductionID) is null or empty/zero
     * @throws SQLException if a database access error occurs or the insert/update operation fails
     */
    @Override
    public void save(CastingsDTO castingsDTO) throws SQLException {
        if (castingsDTO == null || castingsDTO.getLocation() == null || castingsDTO.getLocation().trim().isEmpty() ||
                castingsDTO.getCategory() == null || castingsDTO.getCategory().trim().isEmpty() ||
                castingsDTO.getDescription() == null || castingsDTO.getDescription().trim().isEmpty() ||
                castingsDTO.getTitle() == null || castingsDTO.getTitle().trim().isEmpty() ||
                castingsDTO.getCdID() == 0 || castingsDTO.getProductionID() == 0) {
            throw new IllegalArgumentException("Location, Category, Description, Title, CdID, ProductionID cannot be null or empty");
        }

        String sql;
        if (castingsDTO.getCastingID() == 0) { // New casting (INSERT)
            sql = "INSERT INTO Castings (Location, Category, Description, Date, DeadLine, Title, CdID, ProductionID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, castingsDTO.getLocation());
                ps.setString(2, castingsDTO.getCategory());
                ps.setString(3, castingsDTO.getDescription());
                ps.setTimestamp(4, (castingsDTO.getDate() != null) ? Timestamp.valueOf(castingsDTO.getDate()) : null);
                ps.setTimestamp(5, (castingsDTO.getDeadLine() != null) ? Timestamp.valueOf(castingsDTO.getDeadLine()) : null);
                ps.setString(6, castingsDTO.getTitle());
                ps.setInt(7, castingsDTO.getCdID());
                ps.setInt(8, castingsDTO.getProductionID());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating casting failed, no rows affected.");
                }

                // Retrieve the generated CastingID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        castingsDTO.setCastingID(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating casting failed, no ID obtained.");
                    }
                }
            }
        } else { // Existing casting (UPDATE)
            sql = "UPDATE Castings SET Location = ?, Category = ?, Description = ?, Date = ?, DeadLine = ?, Title = ?, CdID = ?, ProductionID = ? WHERE CastingID = ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, castingsDTO.getLocation());
                ps.setString(2, castingsDTO.getCategory());
                ps.setString(3, castingsDTO.getDescription());
                ps.setTimestamp(4, (castingsDTO.getDate() != null) ? Timestamp.valueOf(castingsDTO.getDate()) : null);
                ps.setTimestamp(5, (castingsDTO.getDeadLine() != null) ? Timestamp.valueOf(castingsDTO.getDeadLine()) : null);
                ps.setString(6, castingsDTO.getTitle());
                ps.setInt(7, castingsDTO.getCdID());
                ps.setInt(8, castingsDTO.getProductionID());
                ps.setInt(9, castingsDTO.getCastingID());

                ps.executeUpdate();
            }
        }
    }

    /**
     * Deletes a casting from the database by their CastingID.
     *
     * @param id the CastingID of the casting to delete, must not be null or zero
     * @return true if the casting was successfully deleted, false if no casting was found with the given ID
     * @throws IllegalArgumentException if id is null or zero
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean delete(Integer id) throws SQLException {
        if (id == null || id == 0) {
            throw new IllegalArgumentException("CastingID cannot be null or zero for deletion.");
        }

        String sql = "DELETE FROM Castings WHERE CastingID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Retrieves a casting by their CastingID.
     *
     * @param castingID the CastingID to search for, must be a positive integer
     * @return the CastingsDTO object if found, null otherwise
     * @throws IllegalArgumentException if castingID is null or less than or equal to zero
     * @throws SQLException if a database access error occurs
     */
    @Override
    public CastingsDTO getById(Integer castingID) throws SQLException {
        if (castingID == null || castingID <= 0) {
            throw new IllegalArgumentException("CastingID must be a positive integer.");
        }

        String sql = "SELECT CastingID, Location, Category, Description, Date, DeadLine, Title, CdID, ProductionID FROM Castings WHERE CastingID = ?";
        CastingsDTO castingsDTO = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, castingID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    castingsDTO = extractCastingFromResultSet(rs);
                }
            }
        }
        return castingsDTO;
    }

    /**
     * Retrieves all castings from the database, ordered by the specified column.
     *
     * @param order the column name to order by (must be one of the allowed columns),
     *              or null/empty for default ordering by CastingID
     * @return a Collection of all castings in the database
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Collection<CastingsDTO> getAll(String order) throws SQLException {
        return getFilteredCastings(order, false, null, null, null, null, null, null, null);
    }

    /**
     * Retrieves castings from the database with optional filtering and ordering.
     * Filters can be applied for location, category, title, dates, CdID, and ProductionID.
     * Text filters use case-sensitive LIKE matching with wildcards.
     *
     * @param order the column name to order by (must be one of the allowed columns),
     *              or null/empty for default ordering by CastingID
     * @param desc true for descending order, false for ascending
     * @param location filter castings by location (partial match), or null for no filtering
     * @param category filter castings by category (partial match), or null for no filtering
     * @param title filter castings by title (partial match), or null for no filtering
     * @param dateFrom filter castings with date >= dateFrom, or null for no filtering
     * @param dateTo filter castings with date <= dateTo, or null for no filtering
     * @param cdID filter castings by exact CdID match, or null for no filtering
     * @param productionID filter castings by exact ProductionID match, or null for no filtering
     * @return a Collection of castings matching the filter criteria
     * @throws SQLException if a database access error occurs
     */
    public Collection<CastingsDTO> getFilteredCastings(String order, boolean desc, String location, String category,
                                                       String title, LocalDateTime dateFrom, LocalDateTime dateTo,
                                                       Integer cdID, Integer productionID) throws SQLException {
        Collection<CastingsDTO> castingsDTOS = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT CastingID, Location, Category, Description, Date, DeadLine, Title, CdID, ProductionID FROM Castings WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (location != null && !location.isEmpty()) {
            sql.append(" AND Location LIKE ?");
            params.add("%" + location + "%");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND Category LIKE ?");
            params.add("%" + category + "%");
        }
        if (title != null && !title.isEmpty()) {
            sql.append(" AND Title LIKE ?");
            params.add("%" + title + "%");
        }
        if (dateFrom != null) {
            sql.append(" AND Date >= ?");
            params.add(Timestamp.valueOf(dateFrom));
        }
        if (dateTo != null) {
            sql.append(" AND Date <= ?");
            params.add(Timestamp.valueOf(dateTo));
        }
        if (cdID != null) {
            sql.append(" AND CdID = ?");
            params.add(cdID);
        }
        if (productionID != null) {
            sql.append(" AND ProductionID = ?");
            params.add(productionID);
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
                    castingsDTOS.add(extractCastingFromResultSet(rs));
                }
            }
        }
        return castingsDTOS;
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
     * Retrieves all castings associated with a specific Casting Director (CD).
     *
     * @param cdID the Casting Director ID to search for, must be a positive integer
     * @return a Collection of CastingsDTO objects for the specified CD
     * @throws IllegalArgumentException if cdID is null or less than or equal to zero
     * @throws SQLException if a database access error occurs
     */
    public Collection<CastingsDTO> getCastingsByCdID(Integer cdID) throws SQLException {
        if (cdID == null || cdID <= 0) {
            throw new IllegalArgumentException("CdID must be a positive integer.");
        }

        return getFilteredCastings(null, false, null, null, null, null, null, cdID, null);
    }

    /**
     * Retrieves all castings associated with a specific Production.
     *
     * @param productionID the Production ID to search for, must be a positive integer
     * @return a Collection of CastingsDTO objects for the specified Production
     * @throws IllegalArgumentException if productionID is null or less than or equal to zero
     * @throws SQLException if a database access error occurs
     */
    public Collection<CastingsDTO> getCastingsByProductionID(Integer productionID) throws SQLException {
        if (productionID == null || productionID <= 0) {
            throw new IllegalArgumentException("ProductionID must be a positive integer.");
        }

        return getFilteredCastings(null, false, null, null, null, null, null, null, productionID);
    }

    /**
     * Retrieves all castings with a deadline after the current date and time.
     *
     * @return a Collection of active CastingsDTO objects
     * @throws SQLException if a database access error occurs
     */
    public Collection<CastingsDTO> getActiveCastings() throws SQLException {
        Collection<CastingsDTO> castingsDTOS = new ArrayList<>();
        String sql = "SELECT CastingID, Location, Category, Description, Date, DeadLine, Title, CdID, ProductionID FROM Castings WHERE DeadLine > ? ORDER BY DeadLine ASC";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    castingsDTOS.add(extractCastingFromResultSet(rs));
                }
            }
        }
        return castingsDTOS;
    }

    /**
     * Counts the total number of castings in the database.
     *
     * @return the total count of castings
     * @throws SQLException if a database access error occurs
     */
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Castings";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Extracts casting data from a ResultSet and creates a CastingsDTO object.
     * This is a private helper method used by query methods to convert database
     * rows into CastingsDTO objects.
     *
     * @param rs the ResultSet positioned at a valid row containing casting data
     * @return a CastingsDTO object populated with data from the current ResultSet row
     * @throws SQLException if a database access error occurs or if the ResultSet
     *         does not contain the expected columns
     */
    private CastingsDTO extractCastingFromResultSet(ResultSet rs) throws SQLException {
        CastingsDTO castingsDTO = new CastingsDTO();
        castingsDTO.setCastingID(rs.getInt("CastingID"));
        castingsDTO.setLocation(rs.getString("Location"));
        castingsDTO.setCategory(rs.getString("Category"));
        castingsDTO.setDescription(rs.getString("Description"));

        Timestamp dateTimestamp = rs.getTimestamp("Date");
        if (dateTimestamp != null) {
            castingsDTO.setDate(dateTimestamp.toLocalDateTime());
        }

        Timestamp deadLineTimestamp = rs.getTimestamp("DeadLine");
        if (deadLineTimestamp != null) {
            castingsDTO.setDeadLine(deadLineTimestamp.toLocalDateTime());
        }

        castingsDTO.setTitle(rs.getString("Title"));
        castingsDTO.setCdID(rs.getInt("CdID"));
        castingsDTO.setProductionID(rs.getInt("ProductionID"));

        return castingsDTO;
    }
}