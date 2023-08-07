package controller.utilities;

import model.HSLPixel;
import model.IPixel;
import model.RGBPixel;

/**
 * This class contains utility methods to convert an RGB representation
 * to HSL and back and print those results.
 * Feel free to change these methods as required.
 */
public class RepresentationConverter {

  /**
   * Converts an RGB representation in the range 0-1 into an HSL
   * representation where.
   * <ul>
   * <li> 0 &lt;= H &lt; 360</li>
   * <li> 0 &lt;= S &lt;= 1</li>
   * <li> 0 &lt;= L &lt;= 1</li>
   * </ul>
   *
   * @param r red value of the RGB between 0 and 1
   * @param g green value of the RGB between 0 and 1
   * @param b blue value of the RGB between 0 and 1
   */
  public static HSLPixel convertRGBtoHSL(double r, double g, double b, int a, IPixel below) {

    double rMath = r / 256;
    double gMath = g / 256;
    double bMath = b / 256;


    double componentMax = Math.max(rMath, Math.max(gMath, bMath));
    double componentMin = Math.min(rMath, Math.min(gMath, bMath));
    double delta = componentMax - componentMin;

    double lightness = (componentMax + componentMin) / 2;
    double hue;
    double saturation;
    if (delta == 0) {
      hue = 0;
      saturation = 0;
    } else {
      saturation = delta / (1 - Math.abs(2 * lightness - 1));
      hue = 0;
      if (componentMax == rMath) {
        hue = (gMath - bMath) / delta;
        while (hue < 0) {
          hue += 6; //hue must be positive to find the appropriate modulus
        }
        hue = hue % 6;
      } else if (componentMax == gMath) {
        hue = (bMath - rMath) / delta;
        hue += 2;
      } else if (componentMax == bMath) {
        hue = (rMath - gMath) / delta;
        hue += 4;
      }

      hue = hue * 60;
    }

    Double hueUse = hue;
    return new HSLPixel(hueUse.intValue(), saturation, lightness, a, 360, below);

  }


  /**
   * Convers an HSL representation where.
   * <ul>
   * <li> 0 &lt;= H &lt; 360</li>
   * <li> 0 &lt;= S &lt;= 1</li>
   * <li> 0 &lt;= L &lt;= 1</li>
   * </ul>
   * into an RGB representation where each component is in the range 0-1
   *
   * @param hue        hue of the HSL representation
   * @param saturation saturation of the HSL representation
   * @param lightness  lightness of the HSL representation
   */

  public static RGBPixel convertHSLtoRGB(
          int hue, double saturation, double lightness, int a, IPixel below) {
    double r = convertFn(hue, saturation, lightness, 0) * 255;
    double g = convertFn(hue, saturation, lightness, 8) * 255;
    double b = convertFn(hue, saturation, lightness, 4) * 255;

    // r, g, b are doubles by this method, but images use integers, so we round the double to its
    // closest integer value and return a new RGBPixel with those values.
    int rInt = new Double(r).intValue();
    int gInt = new Double(g).intValue();
    int bInt = new Double(b).intValue();

    return new RGBPixel(rInt, gInt, bInt, 255, 255, below);
  }

  /*
   * Helper method that performs the translation from the HSL polygonal
   * model to the more familiar RGB model
   */
  private static double convertFn(double hue, double saturation, double lightness, int n) {
    double k = (n + (hue / 30)) % 12;
    double a = saturation * Math.min(lightness, 1 - lightness);

    return lightness - a * Math.max(-1, Math.min(k - 3, Math.min(9 - k, 1)));
  }

}
