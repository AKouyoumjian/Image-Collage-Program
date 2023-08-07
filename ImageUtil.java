package controller.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import model.IPixel;
import model.RGBPixel;


/**
 * This class contains utility methods to read a PPM image from file and simply print its contents.
 * Feel free to change this method as required.
 */
public class ImageUtil {

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   */
  public static List<List<IPixel>> readPPM(String filename) throws IllegalArgumentException {
    Scanner sc = null;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      System.out.println("File " + filename + " not found!");
      throw new IllegalArgumentException("File name invalid");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    System.out.println("Width of image: " + width);
    int height = sc.nextInt();
    System.out.println("Height of image: " + height);
    int maxValue = sc.nextInt();
    System.out.println("Maximum value of a color in this file (usually 255): " + maxValue);


    // makes array grid, list of rows
    IPixel[][] arr = new RGBPixel[height][width];


    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();

        /*
        make a new pixel here with these values and add to array
         */
        IPixel pCurrent = new RGBPixel(r, g, b, 1);


        arr[i][j] = pCurrent;

        //System.out.println("Color of pixel (" + j + "," + i + "): " + r + "," + g + "," + b);
      }
    }



    List<List<IPixel>> list = new ArrayList<>();

    // loop to turn arr[][] into a list of list of IPixel.
    for (IPixel[] row : arr) {
      ArrayList<IPixel> rowList = new ArrayList<IPixel>();

      for (IPixel col : row) {
        rowList.add(col);
      }

      list.add(rowList);
    }

    return list;
  }

  /**
   * Main method used to run the ImageUtil.
   * @param args array of string.
   */
  public static void main(String[] args) {
    String filename;

    if (args.length > 0) {
      filename = args[0];
    } else {
      filename = "sample.ppm";
    }

    ImageUtil.readPPM(filename);

  }


}

