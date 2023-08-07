package controller.utilities;


import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import model.IPixel;
import model.RGBPixel;

/**
 * Utility class for the ImageIO library which reads and translates jpeg, png, and other images.
 */
public class JpegAndPngUtil {

  /**
   * This method reads in a Jpeg or Png image and returns a 2D list of pixels representing
   * the image so that it can be given to the model.
   *
   * @param path File path as a string
   * @return the list, of list of IPixel representing the image.
   * @throws IllegalArgumentException if the path leads to a not recognized file or error occurs
   *                                  during reading, like if the path leads to a ppm image.
   */
  public static List<List<IPixel>> readImage(String path) throws IllegalArgumentException {
    BufferedImage bufferedImage;

    try {
      bufferedImage = ImageIO.read(new FileImageInputStream(new File(path)));

    } catch (IOException e) {
      throw new IllegalArgumentException("File name not recognized.");
    }

    // now get the list<list<IPixel>> from the buffered image
    List<List<IPixel>> outer = new ArrayList<>();


    for (int i = 0; i < bufferedImage.getHeight(); i++) {
      // each list is a row of pixels
      List<IPixel> inner = new ArrayList<>();

      for (int j = 0; j < bufferedImage.getWidth(); j++) {
        // now make pixels from .getRGB and add to
        int rgb = bufferedImage.getRGB(i, j);
        // get the individual r, g, b values
        int r = (rgb >> 16) & 0x000000FF;
        int g = (rgb >> 8) & 0x000000FF;
        int b = (rgb) & 0x000000FF;


        // get the alpha from the rgb pixel int
        int a = new IndexColorModel(1, 1, null, null, null).getAlpha(rgb);


        // make new rgb pixel
        IPixel current = new RGBPixel(r, g, b, a);

        // now add pixel created to the inner list
        inner.add(current);
      }

      outer.add(inner);

    }

    return outer;
  }

  /**
   * This method writes the given BufferedImage to the given path (saved in JPeg/Png format).
   * @param path String for file path
   * @param b BufferedImage representing the image to be saved.
   * @throws IllegalArgumentException If error occurs while writing to path.
   */
  public static void saveImage(String path, BufferedImage b) throws IllegalArgumentException {

    // To get the file type (png vs jpeg vs ppm), first reverse the path
    String[] reversedPath = new StringBuilder(path).reverse().toString().split("\\.");

    // then take characters before period and reverse it back to just get the extension
    String extension = new StringBuilder(reversedPath[0]).reverse().toString();

    try {
      ImageIO.write(b, extension, new File(path));

    }
    catch (IOException e) {
      throw new IllegalStateException("Error occurred while writing.");
    }

  }




}
