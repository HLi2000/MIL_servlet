import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDao {

    public String Login(User user){

        String msg;

        //set up SQL
        int username = user.getH_username();
        int password = user.getH_password();
        //String SQL = "select * from account where username = ? and password = ?";
        String SQL = "select * from account where username = ?";
        PreparedStatement pstmt;
        Connection conn;
        //User dbuser = new User(); //store the SQL result from DB

        //execute SQL query for the account
        try{
            conn = DBConn.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1,username);
            //pstmt.setInt(2,password);
            ResultSet rset = pstmt.executeQuery();//execute the command

            msg="Unknown Username"; //If rset is empty
            while(rset.next()){
                if(rset.getInt("password")==password){
                    msg="Login Successful";
                }
                else msg="Wrong Password";
            }

            pstmt.close();
        }
        catch(Exception e) {
            return e.getMessage();
        }

        return msg;
    }

    public boolean Register(User user){
        int username = user.getH_username();
        int password = user.getH_password();
        boolean searchResult;
        PreparedStatement pstmt;//used to execute sql statement with parameters
        Connection conn = null;
        //decide insert or not by searching
        String SQL = "select * from account where username = ? and password = ?";
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

        }
        searchResult = dbuser.getH_username() != 0 && dbuser.getH_password() != 0;

        //insert
        while(searchResult = false) {
            String SQL1 = "insert into account (username,password) values(?,?)";//insert new user into the table
            try {
                conn = DBConn.getConnection();//connect to the database
                pstmt = conn.prepareStatement(SQL1);
                pstmt.setInt(1, username);//replace ? in SQL by username and password
                pstmt.setInt(2, password);
                pstmt.executeQuery();
                pstmt.close();
            } catch (Exception e) {
                System.out.println("dao error");
            } finally {
                DBConn.closeConnection(conn);
            }
        }

        //verify
        String SQL2 = "select * from account where username = ? and password = ?";
        User dbuser1 = new User();
        try{
            conn = DBConn.getConnection();//connect to the database
            pstmt = conn.prepareStatement(SQL2);
            pstmt.setInt(1,username);//replace ? in SQL by username and password
            pstmt.setInt(2,password);
            ResultSet rset = pstmt.executeQuery();//execute the command
            if(rset.next()){
                dbuser1.setH_username(rset.getInt("username"));
                dbuser1.setH_password(rset.getInt("password"));
            }
            pstmt.close();
        }
        catch(Exception e) {
            System.out.println(e);
            return false;
        }
        finally {
            DBConn.closeConnection(conn);
        }

        return dbuser1.getH_username() != 0 && dbuser1.getH_password() != 0;//successfully registered
    }
}