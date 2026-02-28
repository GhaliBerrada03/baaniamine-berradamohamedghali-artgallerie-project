package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// starting the singleton pattern
public class Db_connection {
    private static Db_connection instance;
    private Connection conn;
    private static final String url = "jdbc:mysql://localhost:3306/artgallerie";
    private static final String username = "root";
    private static final String pass = "";
    private Db_connection() throws SQLException {
        conn = DriverManager.getConnection(url, username, pass);
        conn.setAutoCommit(true);
    }
    // start creating the object but the conncetion to my sql is still private .
    public static synchronized Db_connection getInstance() throws SQLException{
        if (instance == null || instance.conn.isClosed()) {
            instance = new Db_connection();
        }
        return instance;
    }
    // that's why this is important it's gonna let us do the Db_connection().getinstance().getconnection() to open the connection.
    public Connection getConnection(){
        return conn;
    }

}

