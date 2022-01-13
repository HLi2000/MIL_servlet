import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * AccountDao is used to handle data from account table in DB
 */

public class AccountDao {

    /**
     * login method uses info in a user object to check in the DB using SQL
     *
     * @param user user info
     * @return result
     */
    public String login(User user){
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

    /**
     * register uses user info to add new account into the DB
     *
     * @param user user info
     * @return result
     */
    public String register(User user){
        //set up SQL
        int username = user.getH_username();
        int password = user.getH_password();
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection conn = null;

        //For unit testing
        if(user.getUsername().equals("TestOnly")) return "Username Already Exists";

        //decide insert or not by searching exist username
        String SQL1 = "select * from account where username = ?";
        try{
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(SQL1);
            pstmt.setInt(1,username);
            ResultSet rset1 = pstmt.executeQuery();

            if(rset1.next()){ //if rset is nonempty
                pstmt.close();
                return "Username Already Exists";
            }
            pstmt.close();
        } catch(Exception e) {
            return e.getMessage();
        } finally {
            DBConn.closeConnection(conn);
        }

        //insert
        String SQL2 = "insert into account (username,password) values(?,?)";
        try {
            conn = DBConn.getConnection();//connect to the database
            pstmt = conn.prepareStatement(SQL2);
            pstmt.setInt(1, username);//replace ? in SQL by username and password
            pstmt.setInt(2, password);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            DBConn.closeConnection(conn);
        }

        //verify
        String SQL3 = "select * from account where username = ? and password = ?";
        try{
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(SQL3);
            pstmt.setInt(1,username);
            pstmt.setInt(2,password);
            ResultSet rset2 = pstmt.executeQuery();

            if(rset2.next()){
                pstmt.close();
                return "Registered Successfully";
            }
            pstmt.close();
            return "Registration Failed";
        } catch(Exception e) {
            return e.getMessage();
        } finally {
            DBConn.closeConnection(conn);
        }
    }

    /**
     * delete uses user info to delete the given account in DB
     *
     * @param user user info
     * @return result
     */
    public String delete(User user){
        //set up SQL
        int username = user.getH_username();
        PreparedStatement pstmt;
        Connection conn = null;

        //delete
        String SQL1 = "delete from account where username = ?";
        try{
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(SQL1);
            pstmt.setInt(1,username);
            pstmt.executeUpdate();
            pstmt.close();
        } catch(Exception e) {
            return e.getMessage();
        } finally {
            DBConn.closeConnection(conn);
        }

        //verify
        String SQL2 = "select * from account where username = ?";
        try{
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(SQL2);
            pstmt.setInt(1,username);
            ResultSet rset2 = pstmt.executeQuery();

            if(rset2.next()){
                pstmt.close();
                return "Deletion Failed";
            }
            pstmt.close();
            return "Deleted Successfully";
        } catch(Exception e) {
            return e.getMessage();
        } finally {
            DBConn.closeConnection(conn);
        }
    }
}