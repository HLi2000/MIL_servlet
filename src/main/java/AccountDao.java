import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDao {

    public String Login(User user){
        //initiate message
        String msg;

        //set up SQL
        int username = user.getH_username();
        int password = user.getH_password();
        String SQL = "select * from account where username = ?";
        PreparedStatement pstmt;
        Connection conn=null;

        //execute SQL query for the account
        try{
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1,username);
            ResultSet rset = pstmt.executeQuery();

            msg="Unknown Username"; //if rset is empty
            while(rset.next()){
                if(rset.getInt("password")==password){
                    msg="Login Successful"; //if password matched
                }
                else msg="Wrong Password"; //if password unmatched
            }

            pstmt.close();
        } catch(Exception e) {
            return e.getMessage();
        } finally {
            DBConn.closeConnection(conn);
        }

        return msg;
    }

    public String Register(User user){
        //initiate message
        String msg;

        //set up SQL
        int username = user.getH_username();
        int password = user.getH_password();
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection conn = null;

        //decide insert or not by searching exist username
        String SQL1 = "select * from account where username = ?";
        boolean exist=false;
        try{
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(SQL1);
            pstmt.setInt(1,username);
            ResultSet rset1 = pstmt.executeQuery();

            if(rset1.next()){ //if rset is nonempty
                exist=true;
            }
            pstmt.close();
        } catch(Exception e) {
            return e.getMessage();
        } finally {
            DBConn.closeConnection(conn);
        }

        if(exist){
            msg="Username Already Exists";
        }
        else{
            //insert
            String SQL2 = "insert into account (username,password) values(?,?)";
            try {
                conn = DBConn.getConnection();//connect to the database
                pstmt = conn.prepareStatement(SQL2);
                pstmt.setInt(1, username);//replace ? in SQL by username and password
                pstmt.setInt(2, password);
                pstmt.executeQuery();
                pstmt.close();
            } catch (Exception e) {
                return e.getMessage();
            } finally {
                DBConn.closeConnection(conn);
            }

            //verify
            String SQL3 = "select * from account where username = ? and password = ?";
            try{
                conn = DBConn.getConnection();//connect to the database
                pstmt = conn.prepareStatement(SQL3);
                pstmt.setInt(1,username);//replace ? in SQL by username and password
                pstmt.setInt(2,password);
                ResultSet rset2 = pstmt.executeQuery();//execute the command

                msg = "Registration Failed";
                if(rset2.next()){
                    msg = "Registered Successfully";
                }
                pstmt.close();
            } catch(Exception e) {
                return e.getMessage();
            } finally {
                DBConn.closeConnection(conn);
            }
        }

        return msg;
    }
}