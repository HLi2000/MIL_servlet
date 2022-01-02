import org.junit.Assert;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.*;

/**
 * Test the SearchDao
 *
 * downloading functions are not fully tested because they require image files
 */

public class TestSearchDao {
    @Test
    public void testSearch() {
        //set up a searchInfo
        String[] modality_a={"CT"};
        String[] region_a={"Arm"};
        String patient_name="Peter";
        SearchInfo searchInfo=new SearchInfo(modality_a,region_a,patient_name);

        //test
        SearchDao searchDao = new SearchDao();
        Img[] img_a = searchDao.search(searchInfo);

        //output should be
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