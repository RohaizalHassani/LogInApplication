package application;

import application.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static Connection connection;
    public static User currentUser;

    public static void createDB() {

        String url = "jdbc:sqlite:login.db";

        try {

            connection = DriverManager.getConnection(url);
            // Create the table if it doesn't exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS user ("
                    + "username TEXT PRIMARY KEY, "
                    + "password TEXT, "
                    + "name TEXT, "
                    + "role TEXT)";

            PreparedStatement pst = connection.prepareStatement(createTableQuery);
            pst.executeUpdate();
            pst.close();
            try {
                String sql = "INSERT INTO user (username, password, name, role) "
                        + "VALUES ('test', 'test', 'David', 'user'),"
                        + "('admin','admin','John','manager');";

                Statement st = connection.createStatement();
                st.executeUpdate(sql);
                st.close();
                System.out.println("Database and table created successfully.");
            } catch (Exception e) {
                System.out.println("Connection Succeed.");
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static User login(String username, String password, String role) {
        currentUser = null;
        try {
            String query = "Select * from user where username = '" + username + "' and password = '" + password + "' and role = '" + role + "'";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                User user = new User(rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getString("role"));
                currentUser = user;
            }
        } catch (Exception e) {
        }
        return currentUser;
    }
}
