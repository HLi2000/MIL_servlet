package DB;

import Entities.Img;
import Entities.SearchInfo;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import ij.process.StackProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DB.SearchDao is used to handle data from img table in DB
 */

public class SearchDao {

    /**
     * search method does SQL query in DB using keywords in searchInfo and return all results
     *
     * @param searchInfo contains search keywords
     * @return Entities.Img[], an array of result Entities.Img objects which contains all image info
     */
    public Img[] search(SearchInfo searchInfo){
        // set up params
        Connection conn=null;
        PreparedStatement psmt;
        String sql;
        ResultSet rs;
        List<Img> img_l = new ArrayList<>();

        try {
            //connect
            conn = DBConn.getConnection();

            //set up keywords
            String[] modality_a= searchInfo.getModality_a();
            String[] region_a= searchInfo.getRegion_a();
            String patient_name= searchInfo.getPatient_name();

            //in case of no selection, query uses all possible keywords
            String[] modality_all={"MRI","CT","Ultrasound","X Ray"};
            String[] region_all={"Arm","Head","Heart","Leg","Body"};
            Array modality_aa;
            Array region_aa;

            //process keywords for SQL query
            if (modality_a.length==0){
                modality_aa=conn.createArrayOf("varchar", modality_all);
            }
            else {
                modality_aa = conn.createArrayOf("varchar", modality_a);
            }

            if (region_a.length==0){
                region_aa=conn.createArrayOf("varchar", region_all);
            }
            else {
                region_aa=conn.createArrayOf("varchar", region_a);
            }
            String patient_name_like="%"+patient_name+"%";

            //set up SQL
            sql = "SELECT * FROM imgs WHERE Modality = ANY (?) AND Region = ANY (?) AND Patient_name LIKE ?";
            psmt = conn.prepareStatement(sql);
            psmt.setArray(1, modality_aa);
            psmt.setArray(2, region_aa);
            psmt.setString(3, patient_name_like);

            //query
            rs = psmt.executeQuery();

            //read each query result into an Entities.Img object
            while (rs.next()) {
                Img img=new Img();
                img.setId(rs.getInt("Id"));
                img.setModality(rs.getString("Modality"));
                img.setRegion(rs.getString("Region"));
                img.setPatient_name(rs.getString("Patient_name"));
                img.setFile_name(rs.getString("File_name"));
                img_l.add(img);
            }

            rs.close();
            psmt.close();
            conn.close();

        } catch (SQLException e) {
            // return e info as a File_name in Entities.Img
            Img img2=new Img();
            img2.setFile_name(e.toString());
            img_l.add(img2);

        }finally{
            DBConn.closeConnection(conn);
        }

        return img_l.toArray(new Img[0]);
    }

    /**
     * create a thumbnail for a given file name using ImagePlus library
     *
     * @param filename the given file name
     * @return InputStream, the created thumbnail
     */
    public InputStream create_thumbnail(String filename) {
        //set up file path
        String fileAbsolutePath="./imgs/"+filename;

        try {
            //open the img file
            ImagePlus imp = IJ.openImage(fileAbsolutePath);
            ImageProcessor ip = imp.getProcessor();
            StackProcessor sp = new StackProcessor(imp.getStack(), ip);

            //crop the img
            int width = imp.getWidth();
            int height = imp.getHeight();
            int cropWidth;
            int cropHeight;

            if(width > height) {
                cropWidth = height;
                cropHeight = height;
            } else {
                cropWidth = width;
                cropHeight = width;
            }

            int x = -1;
            int y = -1;

            if(width == height) {
                x = 0;
                y = 0;
            } else if(width > height) {
                x = (width - height) / 2;
                y=0;
            } else if (width < height) {
                x = 0;
                y = (height - width) / 2;
            }

            ImageStack croppedStack = sp.crop(x, y, cropWidth, cropHeight);

            //generate the thumbnail img
            imp.setStack(null, croppedStack);
            sp = new StackProcessor(imp.getStack(), imp.getProcessor());
            ImageStack resizedStack = sp.resize(100, 100, true);
            imp.setStack(null, resizedStack);

            BufferedImage buffImage = imp.getBufferedImage();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(buffImage, "jpg", baos);

            return new ByteArrayInputStream(baos.toByteArray());

        } catch (Exception e) {
            return null;
        }
    }
}
