import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDao {

    public boolean Login(User user){

        int username = user.getH_username();//initial value
        int password = user.getH_password();//initial value
        String SQL = "select * from account where username = ? and password = ?";//select matched user
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection conn;
        User dbuser = new User();

        try{
            conn = DBConn.getConnection();//connect to the database
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1,username);//replace ? in SQL by username and password
            pstmt.setInt(2,password);
            ResultSet rset = pstmt.executeQuery();//execute the command
            if(rset.next()){
                dbuser.setH_username(rset.getInt("username"));
                dbuser.setH_password(rset.getInt("password"));
            }
            pstmt.close();
        }
        catch(Exception e) {
            System.out.println(e);
            return false;
        }

        //check if there is a matched user and  return the result
        return dbuser.getH_username() != 0 && dbuser.getH_password() != 0;
    }

    public boolean Register(User user){
        int username = user.getH_username();
        int password = user.getH_password();
        String SQL = "insert into account (username,password) values(?,?)";//insert new user into the table
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection conn = null;

        try {
            conn = DBConn.getConnection();//connect to the database
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, username);//replace ? in SQL by username and password
            pstmt.setInt(2, password);
            pstmt.executeQuery();
            pstmt.close();
        }
        catch (Exception e){
            System.out.println("dao error");
            return true;
        }
        finally {
            DBConn.closeConnection(conn);
        }

        return true;//successfully registered
    }
}