import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/search","/thumbnail","/img"},loadOnStartup = 1)
public class Selvet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if (Objects.equals(path, "/search")){ //cannot compare use '=='
            resp.setContentType("text/html");
            resp.getWriter().write("<h1>Hello, world!<h1>");
        }
        else resp.getWriter().write(path);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/search": {
                String reqBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                Gson gson = new Gson();
                SearchInfo searchInfo = gson.fromJson(reqBody, SearchInfo.class);

                SearchDao searchDao = new SearchDao();
                Img[] img_a = searchDao.search(searchInfo);

                resp.setContentType("application/json");
                Gson gson2 = new Gson();
                String jsonString = gson2.toJson(img_a);
                resp.getWriter().write(jsonString);
                break;
            }

            case "/thumbnail": {
                String fileName = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                SearchDao searchDao = new SearchDao();
                InputStream is = searchDao.create_thumbnail(fileName);

                setRespOS(resp, is);
                break;
            }

            case "/img": {
                String fileName = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

                ImgDao imgDao = new ImgDao();
                InputStream is = imgDao.open_img(fileName);

                setRespOS(resp, is);
                break;
            }
        }
    }

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
