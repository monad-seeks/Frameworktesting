package com.automation.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DBUtil {

    private DBUtil() {
    }

    public static Connection getConnection(String url, String username, String password) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Log.error("MySQL JDBC Driver registration failed", e);
            throw new SQLException("Driver class not located: com.mysql.cj.jdbc.Driver", e);
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static List<Map<String, Object>> executeQuery(String url, String username, String password, String sql, Object... params) {
        List<Map<String, Object>> records = new ArrayList<>();

        try (Connection conn = getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnLabel(i), rs.getObject(i));
                    }
                    records.add(row);
                }
            }
            Log.info("DB Query executed successfully: " + sql + " | Total rows fetched: " + records.size());
        } catch (SQLException e) {
            Log.error("SQL query execution encountered an error: " + sql, e);
            throw new RuntimeException("Database operation failed: " + e.getMessage(), e);
        }

        return records;
    }

    public static int executeUpdate(String url, String username, String password, String sql, Object... params) {
        try (Connection conn = getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            int rowsAffected = stmt.executeUpdate();
            Log.info("DB Update executed. Rows affected: " + rowsAffected);
            return rowsAffected;
        } catch (SQLException e) {
            Log.error("SQL update statement failed: " + sql, e);
            throw new RuntimeException("Database update failed: " + e.getMessage(), e);
        }
    }
}
