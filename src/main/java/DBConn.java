import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConn is used to connect or disconnect to the DB
 */

public class DBConn {
    //set up URL and DB driver
    private static final String dbUrl = System.getenv("JDBC_DATABASE_URL");
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static Connection conn = null;

    /**
     * Connect to the DB
     *
     * @return Connection
     */
    public static Connection getConnection(){

        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(dbUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * Disconnect to the DB
     *
     * @param conn the given connection
     */
    public static void closeConnection(Connection conn){

        if(conn != null){
            try {
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}