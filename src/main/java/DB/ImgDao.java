package DB;

import ij.IJ;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * DB.ImgDao is used to handle image file data from Server
 */

public class ImgDao {

    /**
     * open and return the raw image fiel for a given file name using ImagePlus library
     *
     * @param filename the given file name
     * @return InputStream, the opened file
     */
    public InputStream open_img(String filename) {
        //set up file path
        String fileAbsolutePath="./imgs/"+filename;

        try {
            //open the file
            ImagePlus imp = IJ.openImage(fileAbsolutePath);

            //read into a universal format of jpg
            BufferedImage buffImage = imp.getBufferedImage();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(buffImage, "jpg", baos);

            return new ByteArrayInputStream(baos.toByteArray());

        } catch (Exception e) {
            return null;
        }
    }
}
