import java.sql.*;

public class DB {

    static Connection con;

    public static Connection getConnection() {
        try {
            if (con == null) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorydb",
                    "root",
                    ""
                );
            }
        }
        catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found!");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
        return con;
    }
}

