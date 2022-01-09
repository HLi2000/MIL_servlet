public class Main {
    public static void main(String[] args) throws Exception{
        User u = new User();
        u.getUsername("abc");
        u.getPassword("123");
        u.hashcode();
        System.out.println(u.username);
        System.out.println(u.password);

        Client c = new Client();


        c.login(u);


    }
}
