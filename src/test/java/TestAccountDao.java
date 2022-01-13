import DB.AccountDao;
import Entities.User;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the DB.AccountDao with different input cases
 */

public class TestAccountDao {
    @Test
    public void testDelete() {
        //set up a user
        User user = new User();
        user.setUsername("ABC");
        user.setPassword("123");
        user.hashcode();

        //test
        AccountDao accountDao=new AccountDao();
        String res=accountDao.delete(user);

        //output should be
        String res_should_be="Deleted Successfully";

        Assert.assertEquals(res_should_be,res);
    }
    @Test
    public void testRegister1() {
        //set up a user
        User user = new User();
        user.setUsername("ABC");
        user.setPassword("123");
        user.hashcode();

        //test
        AccountDao accountDao=new AccountDao();
        accountDao.delete(user);
        String res=accountDao.register(user);

        //output should be
        String res_should_be="Registered Successfully";

        Assert.assertEquals(res_should_be,res);
    }
    @Test
    public void testRegister2() {
        //set up a user
        User user = new User();
        user.setUsername("ABC");
        user.setPassword("123");
        user.hashcode();

        //test
        AccountDao accountDao=new AccountDao();
        accountDao.register(user);
        String res=accountDao.register(user);

        //output should be
        String res_should_be="Username Already Exists";

        Assert.assertEquals(res_should_be,res);
    }
    @Test
    public void testLogin1() {
        //set up a user
        User user = new User();
        user.setUsername("ABC");
        user.setPassword("123");
        user.hashcode();

        //test
        AccountDao accountDao=new AccountDao();
        accountDao.register(user);
        String res=accountDao.login(user);

        //output should be
        String res_should_be="Login Successful";

        Assert.assertEquals(res_should_be,res);
    }
    @Test
    public void testLogin2() {
        //set up a user
        User user1 = new User();
        user1.setUsername("ABC");
        user1.setPassword("12");
        user1.hashcode();

        User user2 = new User();
        user2.setUsername("ABC");
        user2.setPassword("123");
        user2.hashcode();

        //test
        AccountDao accountDao=new AccountDao();
        accountDao.register(user2);
        String res=accountDao.login(user1);

        //output should be
        String res_should_be="Wrong Password";

        Assert.assertEquals(res_should_be,res);
    }
    @Test
    public void testLogin3() {
        //set up a user
        User user = new User();
        user.setUsername("TestOnly");
        user.setPassword("123");
        user.hashcode();

        //test
        AccountDao accountDao=new AccountDao();
        String res=accountDao.login(user);

        //output should be
        String res_should_be="Unknown Username";

        Assert.assertEquals(res_should_be,res);
    }
}