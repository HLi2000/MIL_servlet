import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Collectors;

/**
 * The Servlet is used to communicate with the client and DB
 *
 * @author  Hao Li
 * @since   2021-12-05
 */


@WebServlet(urlPatterns = {"/login","/register","/delete","/search","/thumbnail","/img"},loadOnStartup = 1)
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    /**
     * doPost method execute 'search', downloading 'thumbnail', or downloading raw 'img' depending on path
     *
     * @param req request from client
     * @param resp response to client
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/login":{
                String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Gson gson = new Gson();
                User loginUser = gson.fromJson(reqBody,User.class);//user received from client for login
                AccountDao accountDao = new AccountDao();
                String loginResult = accountDao.Login(loginUser);//the search result

                //response to the client
                resp.setContentType("text/html");
                resp.getWriter().write(loginResult);

                break;
            }
            case "/register":{
                String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Gson gson = new Gson();
                User registerUser = gson.fromJson(reqBody,User.class);//user received from client for registration
                AccountDao accountDao = new AccountDao();
                String registerResult = accountDao.Register(registerUser);

                //response to client
                resp.setContentType("text/html");
                resp.getWriter().write(registerResult);

                break;
            }
            case "/delete":{
                String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Gson gson = new Gson();
                User registerUser = gson.fromJson(reqBody,User.class);//user received from client for registration
                AccountDao accountDao = new AccountDao();
                String registerResult = accountDao.Delete(registerUser);

                //response to client
                resp.setContentType("text/html");
                resp.getWriter().write(registerResult);

                break;
            }
            case "/search": {
                // Read the body of the request into a SearchInfo
                String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Gson gson = new Gson();
                SearchInfo searchInfo = gson.fromJson(reqBody, SearchInfo.class);

                // Search using the searchInfo
                SearchDao searchDao = new SearchDao();
                Img[] img_a = searchDao.search(searchInfo);

                // Set up and write the body of the response
                resp.setContentType("application/json");
                Gson gson2 = new Gson();
                String jsonString = gson2.toJson(img_a);
                resp.getWriter().write(jsonString);
                break;
            }

            case "/thumbnail": {
                // Read the body of the request
                String fileName = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                // Create the thumbnail
                SearchDao searchDao = new SearchDao();
                InputStream is = searchDao.create_thumbnail(fileName);

                // Write into the body of the response
                setRespOS(resp, is);
                break;
            }

            case "/img": {
                // Read the body of the request
                String fileName = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                // Open the raw image
                ImgDao imgDao = new ImgDao();
                InputStream is = imgDao.open_img(fileName);

                // Write into the body of the response
                setRespOS(resp, is);
                break;
            }
        }
    }

    /**
     * Set up the response as an output file stream
     *
     * @param resp response
     * @param is InputStream of thumbnail or raw img
     */
    private void setRespOS(HttpServletResponse resp, InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        int len;

        try (OutputStream os = resp.getOutputStream()) {
            while ((len = is.read(bytes)) > 0) {
                os.write(bytes, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}

