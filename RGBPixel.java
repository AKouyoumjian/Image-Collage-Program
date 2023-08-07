package model;

import java.util.ArrayList;

import controller.utilities.RepresentationConverter;

/**
 * This class represents a pixel that has RGB and A fields as integers.
 * This pixel also has a MaxValue (auto set to 255 if not put in constructor).
 * It also keeps track of the pixel below. A 2D list of pixels will make up a layer,
 * and a list of layers makes up a project (the Collager model).
 */
public class RGBPixel implements IPixel {
  private int r;
  private int g;
  private int b;
  private int a;
  private final int maxValue;
  private IPixel pixelBelow; // represents the IPixel below this pixel in a layer.
  // we use a reference here intentionally as we want the below pixel to update if needed.
  // it is a private field and only accessible in the model.

  /**
   * Constructor for an RGB pixel that takes in the integer values for rgba.
   * And all fields of an RGBPixel
   * @param r integer
   * @param g integer
   * @param b integer
   * @param a integer
   * @param maxValue integer
   */
  public RGBPixel(int r, int g, int b, int a, int maxValue, IPixel pixelBelow) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
    if (!(this.isValidField(this.r)
            && this.isValidField(this.b)
            && this.isValidField(this.g)
            && this.isValidField(this.a))) {
      throw new IllegalArgumentException("Invalid RGB or A input. Must be from 0-255");
    }
    this.maxValue = maxValue;
    this.pixelBelow = pixelBelow;
  }

  /**
   * Convenience Constructor FOR PPM PIXELS since they all have maxValue with 255.
   *
   * @param r integer
   * @param g integer
   * @param b integer
   * @param a integer
   */
  public RGBPixel(int r, int g, int b, int a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
    if (!(this.isValidField(this.r)
            && this.isValidField(this.b)
            && this.isValidField(this.g)
            && this.isValidField(this.a))) {
      throw new IllegalArgumentException("Invalid RGB or A input. Must be from 0-255");
    }
    this.maxValue = 255; // max value for all PPMs are 255
    this.pixelBelow = null; // starts at null, in constructor of ILayer it will be set.

  }

  @Override
  public String toString() {
    return this.r + " " + this.g + " " + this.b + " " + this.a + "\n";
  }

  @Override
  public void apply(IFilterOption f) {
    double value = this.getValue();
    double luma = this.getLuma();
    double intensity = this.getIntensity();


    switch (f.toString()) {
      case "multiply":

      case "screen":

        // apply screen filter if pixel below is not null
        if (this.pixelBelow != null) {

          // convert pixel to hsl to do filter
          IPixel filteredPixel = RepresentationConverter.convertRGBtoHSL(
                  this.r, this.g, this.b, this.a, this.pixelBelow);

          // apply the filter
          filteredPixel.apply(f);

          // get h,s,l values for filtered pixel, so we can convert back
          String[] filteredPixelList = filteredPixel.toString().split(" ", -1);
          int filteredH = Integer.parseInt(filteredPixelList[0]);
          double filteredS = Double.parseDouble(filteredPixelList[1]);
          double filteredL = Double.parseDouble(filteredPixelList[2]);


          // now convert back
          IPixel convertedBack =
                  RepresentationConverter.convertHSLtoRGB(filteredH, filteredS, filteredL,
                          this.a, this.pixelBelow);


          // get r, g, b values for convertedBack pixel so we can mutate this pixel to its values.
          String[] convertedList = convertedBack.toString().split(" ", -1);
          int convertedR = Integer.parseInt(convertedList[0]);
          int convertedG = Integer.parseInt(convertedList[1]);
          int convertedB = Integer.parseInt(convertedList[2]);

          // finally, mutate this pixel's values to now be filtered.
          this.r = convertedR;
          this.g = convertedG;
          this.b = convertedB;
        }

        break;


      case "normal":
        break;
      case "red-component":
        this.g = 0;
        this.b = 0;
        break;
      case "blue-component":
        this.g = 0;
        this.r = 0;
        break;
      case "green-component":
        this.r = 0;
        this.b = 0;
        break;

      case "brighten-value":

        this.r += value;
        this.r = this.clampMax(this.r, this.maxValue);

        this.g += value;
        this.g = this.clampMax(this.g, this.maxValue);

        this.b += value;
        this.b = this.clampMax(this.b, this.maxValue);

        break;
      case "darken-value":

        this.r -= value;
        this.r = this.clampMax(this.r, this.maxValue);

        this.g -= value;
        this.g = this.clampMax(this.g, this.maxValue);

        this.b -= value;
        this.b = this.clampMax(this.b, this.maxValue);

        break;

      case "brighten-luma":

        this.r += luma;
        this.r = this.clampMax(this.r, this.maxValue);

        this.g += luma;
        this.g = this.clampMax(this.g, this.maxValue);

        this.b += luma;
        this.b = this.clampMax(this.b, this.maxValue);

        break;
      case "darken-luma":

        this.r -= luma;
        this.r = this.clampMax(this.r, this.maxValue);

        this.g -= luma;
        this.g = this.clampMax(this.g, this.maxValue);

        this.b -= luma;
        this.b = this.clampMax(this.b, this.maxValue);

        break;

      case "brighten-intensity":

        this.r += intensity;
        this.r = this.clampMax(this.r, this.maxValue);

        this.g += intensity;
        this.g = this.clampMax(this.g, this.maxValue);

        this.b += intensity;
        this.b = this.clampMax(this.b, this.maxValue);

        break;
      case "darken-intensity":

        this.r -= intensity;
        this.r = this.clampMax(this.r, this.maxValue);

        this.g -= intensity;
        this.g = this.clampMax(this.g, this.maxValue);

        this.b -= intensity;
        this.b = this.clampMax(this.b, this.maxValue);

        break;
      case "difference":
        // wrap entire case in if not null since if below pixel is null, no filter is applied.
        if (this.pixelBelow != null) {


          // if the below pixel is HSL, need to convert to RGB.
          if (this.pixelBelow.isHSL()) {
            String[] str = this.pixelBelow.toString().split(" ", -1);
            int h = Integer.parseInt(str[0]);
            double s = Double.parseDouble(str[1]);
            double l = Double.parseDouble(str[2]);
            this.pixelBelow = RepresentationConverter.convertHSLtoRGB(h, s, l, this.a,
                    this.pixelBelow.getPixelBelow());
          }

          // now we get r g and b values of below pixels.
          String[] str2 = this.pixelBelow.toString().split(" ", -1);
          int rPrime = Integer.parseInt(str2[0]);
          int gPrime = Integer.parseInt(str2[1]);
          int bPrime = Integer.parseInt(str2[2]);

          // now mutate, "applying" the filter.
          this.r = Math.abs(this.r - rPrime);
          this.g = Math.abs(this.g - gPrime);
          this.b = Math.abs(this.b - bPrime);
          break;
        }
        break;

      default: // if it gets down here, it was an invalid filter name.
        throw new IllegalArgumentException("No filter with that name exists.");
    }
  }

  @Override
  public IPixel copy() {
    return new RGBPixel(this.r, this.g, this.b, this.a, this.maxValue, this.pixelBelow);
  }

  @Override
  public IPixel merge(IPixel bgPix) {
    String[] bgPixStr = bgPix.toString().split(" ", -1);
    int dR = Integer.parseInt(bgPixStr[0]);
    int dG = Integer.parseInt(bgPixStr[1]);
    int dB = Integer.parseInt(bgPixStr[2]);
    double dA = Integer.parseInt(bgPixStr[3].replace("\n", ""));

    // merge with other pixel being the pixel below so dA is below a.
    // so this pixel is on top being merged with other below.

    double aDouble = a * 1.0;
    double rDouble = r * 1.0;
    double gDouble = g * 1.0;
    double bDouble = b * 1.0;


    double aPercent =
            ((dA / this.maxValue) + ((aDouble / this.maxValue) * (1 - (dA / this.maxValue))));

    int newA = (int) (aPercent * 255);


    int newR = (int) ((dA / this.maxValue) * dR
            + rDouble * (aDouble / this.maxValue) * (1 - (dA / this.maxValue)) * (1 / aPercent));





    int newG = (int) ((dA / this.maxValue) * dG
            + gDouble * (aDouble / this.maxValue) * (1 - (dA / this.maxValue)) * (1 / aPercent));


    int newB = (int) ((dA / this.maxValue) * dB
            + bDouble * (aDouble / this.maxValue) * (1 - (dA / this.maxValue)) * (1 / aPercent));

    return new RGBPixel(newR, newG, newB, newA, this.maxValue, this.pixelBelow);

  }

  @Override
  public double getValue() {
    int maxOfTwo = Math.max(this.r, this.g);
    return Math.max(maxOfTwo, this.b);
  }

  @Override
  public double getLuma() {
    return 0.2126 * this.r + 0.7152 * this.g + 0.0722 * this.b;
  }

  @Override
  public double getIntensity() {
    return (this.r + this.g + this.b) / 3;
  }


  @Override
  public int getMaxValue() {
    return this.maxValue;
  }

  @Override
  public boolean isRGB() {
    return true;
  }

  @Override
  public boolean isHSL() {
    return false;
  }

  @Override
  public IPixel getPixelBelow() {
    return this.pixelBelow;
  }

  @Override
  public void setPixelBelow(IPixel below) {
    this.pixelBelow = below;
  }

  /**
   * This helper method returns the current int, if it is over the max it sets it back to the max.
   * @param current current int
   * @param max max int
   * @return the integer clamped.
   */
  private int clampMax(int current, int max) {
    return Math.min(current, max);
  }


  /**
   * Private helper for constructor making sure a value is valid (within 0-255).
   */
  private boolean isValidField(int n) {
    return n >= 0 && n < 256;
  }


}
