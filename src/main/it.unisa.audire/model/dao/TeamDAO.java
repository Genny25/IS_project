package model.dao;

import model.dto.TeamDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) for managing TeamDTO entities.
 * <p>
 * This class handles the Many-to-Many relationship between Productions and Casting Directors.
 * It allows adding members to a production team, removing them, and retrieving team compositions.
 * </p>
 * <p>
 * <b>Note:</b> Since the "Team" table uses a composite primary key (ProductionID + CdID),
 * there is no single auto-generated ID to retrieve upon insertion.
 * </p>
 *
 */
public class TeamDAO {

    private final DataSource dataSource;

    /**
     * Constructs a new {@code TeamDAO} with the specified DataSource.
     *
     * @param dataSource the {@link DataSource} used to obtain database connections.
     */
    public TeamDAO(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "DataSource cannot be null");
    }

    /**
     * Adds a Casting Director to a Production Team.
     * <p>
     * Corresponds to an INSERT operation in the join table.
     * If the association already exists, this method might throw a specific SQLException
     * (Duplicate Entry) depending on the driver, or simply do nothing if using INSERT IGNORE.
     * </p>
     *
     * @param team the TeamDTO containing the ProductionID and CdID to link.
     * @throws SQLException if a database access error occurs.
     * @throws IllegalArgumentException if IDs are invalid.
     */
    public void save(TeamDTO team) throws SQLException {
        if (team == null) {
            throw new IllegalArgumentException("TeamDTO cannot be null");
        }
        if (team.getProductionID() <= 0 || team.getCdID() <= 0) {
            throw new IllegalArgumentException("Both ProductionID and CdID must be valid positive integers.");
        }

        // Check if exists to avoid Duplicate Key Exception logic, or handle it via try-catch
        if (exists(team.getProductionID(), team.getCdID())) {
            return; // Association already exists, nothing to do.
        }

        String sql = "INSERT INTO Team (ProductionID, CdID) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, team.getProductionID());
            ps.setInt(2, team.getCdID());

            ps.executeUpdate();
        }
    }

    /**
     * Removes a Casting Director from a specific Production Team.
     *
     * @param productionID the ID of the production.
     * @param cdID the ID of the Casting Director to remove.
     * @return {@code true} if the removal was successful, {@code false} if the link didn't exist.
     * @throws SQLException if a database error occurs.
     */
    public boolean delete(int productionID, int cdID) throws SQLException {
        if (productionID <= 0 || cdID <= 0) {
            throw new IllegalArgumentException("Invalid IDs provided for deletion.");
        }

        String sql = "DELETE FROM Team WHERE ProductionID = ? AND CdID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, productionID);
            ps.setInt(2, cdID);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves all Casting Directors associated with a specific Production.
     *
     * @param productionID the ID of the production.
     * @return a List of TeamDTO objects representing the team members.
     * @throws SQLException if a database error occurs.
     */
    public List<TeamDTO> getByProductionID(int productionID) throws SQLException {
        if (productionID <= 0) return new ArrayList<>();

        String sql = "SELECT * FROM Team WHERE ProductionID = ?";
        List<TeamDTO> teamMembers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, productionID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    teamMembers.add(extractTeamFromResultSet(rs));
                }
            }
        }
        return teamMembers;
    }

    /**
     * Retrieves all Productions a specific Casting Director is working on.
     *
     * @param cdID the ID of the Casting Director.
     * @return a List of TeamDTO objects representing the productions.
     * @throws SQLException if a database error occurs.
     */
    public List<TeamDTO> getByCdID(int cdID) throws SQLException {
        if (cdID <= 0) return new ArrayList<>();

        String sql = "SELECT * FROM Team WHERE CdID = ?";
        List<TeamDTO> assignments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cdID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    assignments.add(extractTeamFromResultSet(rs));
                }
            }
        }
        return assignments;
    }

    /**
     * Checks if a specific association already exists.
     *
     * @param productionID the production ID.
     * @param cdID the casting director ID.
     * @return true if exists, false otherwise.
     * @throws SQLException if database error occurs.
     */
    public boolean exists(int productionID, int cdID) throws SQLException {
        String sql = "SELECT 1 FROM Team WHERE ProductionID = ? AND CdID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productionID);
            ps.setInt(2, cdID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // --- Helper Methods ---

    private TeamDTO extractTeamFromResultSet(ResultSet rs) throws SQLException {
        TeamDTO team = new TeamDTO();
        team.setProductionID(rs.getInt("ProductionID"));
        team.setCdID(rs.getInt("CdID"));
        return team;
    }
}