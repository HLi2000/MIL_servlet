import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;
import java.sql.ResultSet;

public class DBDao {

    private String SQL = "";
    private int username;
    private int password;


    public boolean Login(User user){
        username = user.username = 0;//initial  value
        password = user.password = 0;//initial value
        SQL = "select * from account where username = ? and password = ?";//select matched user
        PreparedStatement pstmt = null;//used to execute sql statement with parameters
        Connection connection = null;
        User dbuser = new User();


        try{
            connection = DBConn.getConnection();//connect to the database
            pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1,username);//replace ? in SQL by username and password
            pstmt.setInt(2,password);
            ResultSet rset = pstmt.executeQuery(SQL);//execute the command
            if(rset.next()){
                dbuser.getUsername(rset.getString("username"));
                dbuser.getPassword(rset.getString("password"));
            }

            pstmt.close();
            connection.close();
        }
        catch(SQLException e) {
            System.out.println(e);
            return false;
        }

        //check if there is a matched user and  return the result
        if (dbuser.username == 0 && dbuser.password == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean Register(User user){
        username = user.username;
        password = user.password;
        SQL = "insert into account (username,password) values(?,?)";//insert new user into the table
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection connection = null;

        try {
            connection = DBConn.getConnection();//connect to the database
            pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, username);//replace ? in SQL by username and password
            pstmt.setInt(2, password);
            pstmt.executeQuery(SQL);
            pstmt.close();

        }
        catch (SQLException e){
            System.out.println("dao error");
            return false;
        }

        finally {
            DBConn.closeConnection();
        }

        return true;//successfully registered
    }
}
