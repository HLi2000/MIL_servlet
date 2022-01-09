import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(urlPatterns={"/login","/register"},loadOnStartup = 1)
public class Servlet extends HttpServlet {
    private boolean loginResult;
    private boolean registerResult;
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String path = req.getServletPath();
        switch (path) {
            case "/login":{
                String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Gson gson = new Gson();
                User loginUser = gson.fromJson(reqBody,User.class);//user received from client for login
                DBDao d1 = new DBDao();
                loginResult = d1.Login(loginUser);//the search result
                //response to the client
                resp.setContentType("text/html");
                if(loginResult = true) {

                    resp.getWriter().write("correct username and password");
                }
                else{
                    resp.getWriter().write("wrong username or password");
                }

                break;
            }
            case "/register":{
                String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Gson gson = new Gson();
                User registerUser = gson.fromJson(reqBody,User.class);//user received from client for registration
                DBDao d2 = new DBDao();
                registerResult = d2.Register(registerUser);
                //response to client
                resp.setContentType("text/html");
                if(registerResult = false) {
                    resp.getWriter().write("registration failed");

                }
                else{
                    resp.getWriter().write("you are registered");
                }

                break;
            }
        }





    }


}
