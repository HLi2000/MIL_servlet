import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.mockito.Mockito.when;

/**
 * Test the Servlet
 *
 * register functions are not fully tested because their tests depend on other req (function)
 * downloading functions are not fully tested because they require image files
 */

public class TestServlet {
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testDelete() throws IOException {
        //set up req
        User user = new User();
        user.setUsername("ABC");
        user.setPassword("123");
        user.hashcode();

        Gson gson = new Gson();
        String jsonString = gson.toJson(user);

        BufferedReader stringReader=new BufferedReader(new StringReader(jsonString));
        when(req.getReader()).thenReturn(stringReader);
        when(req.getServletPath()).thenReturn("/delete");

        //set up resp
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn(printWriter);

        //test
        Servlet servlet=new Servlet();
        servlet.doPost(req,resp);

        //get output
        String output=stringWriter.getBuffer().toString();

        //output should be
        String output_should_be="Deleted Successfully";

        Assert.assertEquals(output_should_be,output);
    }
    @Test
    public void testLogin() throws IOException {
        //set up req
        User user = new User();
        user.setUsername("TestOnly");
        user.setPassword("123");
        user.hashcode();

        Gson gson = new Gson();
        String jsonString = gson.toJson(user);

        BufferedReader stringReader=new BufferedReader(new StringReader(jsonString));
        when(req.getReader()).thenReturn(stringReader);
        when(req.getServletPath()).thenReturn("/login");

        //set up resp
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn(printWriter);

        //test
        Servlet servlet=new Servlet();
        servlet.doPost(req,resp);

        //get output
        String output=stringWriter.getBuffer().toString();

        //output should be
        String output_should_be="Unknown Username";

        Assert.assertEquals(output_should_be,output);
    }
    @Test
    public void testSearch() throws IOException {
        //set up req
        String[] modality_a={"CT"};
        String[] region_a={"Arm"};
        String patient_name="Peter";
        SearchInfo searchInfo=new SearchInfo(modality_a,region_a,patient_name);

        Gson gson = new Gson();
        String jsonString = gson.toJson(searchInfo);

        BufferedReader stringReader=new BufferedReader(new StringReader(jsonString));
        when(req.getReader()).thenReturn(stringReader);
        when(req.getServletPath()).thenReturn("/search");

        //set up resp
        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn(printWriter);

        //test
        Servlet servlet=new Servlet();
        servlet.doPost(req,resp);

        //get output
        String output=stringWriter.getBuffer().toString();
        Gson gson2 = new Gson();
        Img[] img_a=gson2.fromJson(output,Img[].class);

        //output should be
        int id=2;
        String modality="CT";
        String region="Arm";
        patient_name="Peter MacLeish";
        String file_name="ct arm 2.png";
        Img img=new Img();
        img.setId(id);
        img.setModality(modality);
        img.setRegion(region);
        img.setPatient_name(patient_name);
        img.setFile_name(file_name);
        Img[] img_a_should_be={img};

        Assert.assertEquals(img_a[0].getId(),img_a_should_be[0].getId());
        Assert.assertEquals(img_a[0].getRegion(),img_a_should_be[0].getRegion());
        Assert.assertEquals(img_a[0].getPatient_name(),img_a_should_be[0].getPatient_name());
        Assert.assertEquals(img_a[0].getFile_name(),img_a_should_be[0].getFile_name());
    }
}
