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
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class TestServlet {
    @Mock
    HttpServletRequest req;
    @Mock
    HttpServletResponse resp;
    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testSearch() throws IOException, ServletException {
        String[] modality_a={"CT"};
        String[] region_a={"Arm"};
        String patient_name="Peter";
        SearchInfo searchInfo=new SearchInfo(modality_a,region_a,patient_name);

        Gson gson = new Gson();
        String jsonString = gson.toJson(searchInfo);

        BufferedReader stringReader=new BufferedReader(new StringReader(jsonString));
        when(req.getReader()).thenReturn(stringReader);
        when(req.getServletPath()).thenReturn("/search");

        StringWriter stringWriter=new StringWriter();
        PrintWriter printWriter=new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn(printWriter);

        Servlet servlet=new Servlet();
        servlet.doGet(req,resp);

        String output=stringWriter.getBuffer().toString();
        Gson gson2 = new Gson();
        Img[] img_a=gson2.fromJson(output,Img[].class);

        int id=1;
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
        Assert.assertArrayEquals(img_a,img_a_should_be);
    }
}
