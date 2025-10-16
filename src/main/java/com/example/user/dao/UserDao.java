package com.example.user.dao;

            import com.example.common.utils.DBUtil;
            import com.example.common.utils.HashUtil;
            import com.example.user.model.User;

            import java.sql.*;
            import java.util.logging.Level;
            import java.util.logging.Logger;

            public class UserDao {

                private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

                public void saveUser(User user) throws SQLException {
                    String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
                    try (Connection conn = DBUtil.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, user.getUsername());
                        stmt.setString(2, user.getEmail());
                        stmt.setString(3, HashUtil.hash(user.getPassword()));
                        stmt.setString(4, user.getRole());
                        stmt.executeUpdate();
                    }
                }

                public User findUserByEmail(String email) throws SQLException {
                    String sql = "SELECT * FROM users WHERE email = ?";
                    try (Connection conn = DBUtil.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, email);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                return mapResultSetToUser(rs);
                            }
                        }
                    }
                    return null;
                }

                public boolean validateUser(String email, String password) throws SQLException {
                    User user = findUserByEmail(email);
                    if (user != null) {
                        return HashUtil.verify(password, user.getPassword());
                    }
                    return false;
                }

                public void updateUserProfile(User user) throws SQLException {
                    String sql = "UPDATE users SET username = ?, phone = ?, address_line = ?, city = ?, postal_code = ? WHERE id = ?";
                    try (Connection conn = DBUtil.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, user.getUsername());
                        stmt.setString(2, user.getPhone());
                        stmt.setString(3, user.getAddressLine());
                        stmt.setString(4, user.getCity());
                        stmt.setString(5, user.getPostalCode());
                        stmt.setInt(6, user.getId());
                        stmt.executeUpdate();
                    }
                }

                public int deleteUserByEmail(String email) throws SQLException {
                    String sql = "DELETE FROM users WHERE email = ?";
                    try (Connection conn = DBUtil.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, email);
                        int deleted = stmt.executeUpdate();
                        if (deleted == 0) {
                            LOGGER.warning("deleteUserByEmail affected 0 rows for email=" + email);
                        }
                        return deleted;
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "deleteUserByEmail failed for email=" + email, e);
                        throw e;
                    }
                }

                // Helper method to map ResultSet to User object
                private User mapResultSetToUser(ResultSet rs) throws SQLException {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddressLine(rs.getString("address_line"));
                    user.setCity(rs.getString("city"));
                    user.setPostalCode(rs.getString("postal_code"));
                    return user;
                }

                public void createUser(User user) throws SQLException {
                    String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
                    try (Connection conn = DBUtil.getConnection();
                         PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, user.getUsername());
                        ps.setString(2, user.getEmail());
                        ps.setString(3, user.getPassword());
                        ps.setString(4, user.getRole());
                        ps.executeUpdate();
                    }
                }
                // *** ADD THIS ENTIRE METHOD AT THE END OF UserDAO.java ***
                public int createUserWithDriver(User user, String licenseNumber, String vehicleType) throws SQLException {
                    Connection conn = null;
                    try {
                        conn = DBUtil.getConnection();
                        conn.setAutoCommit(false); // Start transaction

                        // 1. Insert user
                        String userSql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
                        int userId;
                        try (PreparedStatement stmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                            stmt.setString(1, user.getUsername());
                            stmt.setString(2, user.getEmail());
                            stmt.setString(3, user.getPassword());
                            stmt.setString(4, user.getRole());
                            stmt.executeUpdate();

                            try (ResultSet rs = stmt.getGeneratedKeys()) {
                                if (rs.next()) {
                                    userId = rs.getInt(1);
                                } else {
                                    throw new SQLException("Failed to get user ID");
                                }
                            }
                        }

                        // 2. If driver, insert driver record
                        if ("driver".equals(user.getRole()) && licenseNumber != null && vehicleType != null) {
                            String driverSql = "INSERT INTO drivers (user_id, license_number, vehicle_type, status) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement stmt = conn.prepareStatement(driverSql)) {
                                stmt.setInt(1, userId);
                                stmt.setString(2, licenseNumber);
                                stmt.setString(3, vehicleType);
                                stmt.setString(4, "available");
                                stmt.executeUpdate();
                            }
                        }

                        conn.commit(); // Commit both inserts
                        return userId;

                    } catch (SQLException e) {
                        if (conn != null) {
                            conn.rollback(); // Rollback on error
                        }
                        throw e;
                    } finally {
                        if (conn != null) {
                            conn.setAutoCommit(true);
                            conn.close();
                        }
                    }
                }
            }
