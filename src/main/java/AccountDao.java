import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDao {

    private String SQL;
    private int username;
    private int password;

    public boolean Login(User user){
        username = user.username = 0;//initial  value
        password = user.password = 0;//initial value
        SQL = "select * from account where username = ? and password = ?";//select matched user
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection conn;
        User dbuser = new User();


        try{
            conn = DBConn.getConnection();//connect to the database
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1,username);//replace ? in SQL by username and password
            pstmt.setInt(2,password);
            ResultSet rset = pstmt.executeQuery(SQL);//execute the command
            if(rset.next()){
                dbuser.getUsername(rset.getString("username"));
                dbuser.getPassword(rset.getString("password"));
            }

            pstmt.close();
            conn.close();
        }
        catch(Exception e) {
            System.out.println(e);
            return false;
        }

        //check if there is a matched user and  return the result
        return dbuser.username != 0 || dbuser.password != 0;
    }

    public boolean Register(User user){
        username = user.username;
        password = user.password;
        SQL = "insert into account (username,password) values(?,?)";//insert new user into the table
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection conn = null;

        try {
            conn = DBConn.getConnection();//connect to the database
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, username);//replace ? in SQL by username and password
            pstmt.setInt(2, password);
            pstmt.executeQuery(SQL);
            pstmt.close();

        }
        catch (Exception e){
            System.out.println("dao error");
            return false;
        }

        finally {
            DBConn.closeConnection(conn);
        }

        return true;//successfully registered
    }
}