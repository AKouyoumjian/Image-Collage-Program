package model;



import controller.utilities.RepresentationConverter;

/**
 * Class for HSLPixel, an IPixel represented with HSL values and an alpha a.
 * This alpha is not used for hsl pixels, but is needed when converting from rgb -> hsl -> rgb
 * where we need to preserve the orginal alpha value.
 * It also keeps track of the pixel below. A 2D list of pixels will make up a layer,
 * and a list of layers makes up a project (the Collager model).
 */
public class HSLPixel implements IPixel {
  private int h;
  private double s;
  private double l;
  private int a;
  private final int maxValue;
  private IPixel pixelBelow; // represents the IPixel below this pixel in a layer.
  // we use a reference here intentionally as we want the below pixel to update if needed.
  // it is a private field and only accessible in the model.


  /**
   * Constructor for a HSL pixel that takes in the integer values for HSL and a.
   * This constructor includes alpha even though hsl does not have alpha,
   * storing it will be helpful for future when we need rgb->hsl->(pixel with alpha)
   * to keep track of a.
   *
   * @param h        integer
   * @param s        double
   * @param l        double
   * @param a        integer
   * @param maxValue integer
   */
  public HSLPixel(int h, double s, double l, int a, int maxValue, IPixel pixelBelow) {
    this.h = h;
    this.s = s;
    this.l = l;
    this.a = a;
    this.maxValue = maxValue;
    this.pixelBelow = pixelBelow;
    if (this.h < 0 || this.h > 360 || this.s < 0 || this.s > 1 || this.l < 0 || this.l > 1) {
      throw new IllegalArgumentException("Invalid h, s, or l field for HSLPixel class.");
    }

  }

  /**
   * Alternative constructor that does not include alpha, sets it to 255 by default.
   * This is used as a convenience constructor, especially for testing.
   *
   * @param h integer
   * @param s double
   * @param l double
   */
  public HSLPixel(int h, double s, double l) {
    this.h = h;
    this.s = s;
    this.l = l;
    this.a = 255;
    this.maxValue = 360;
    this.pixelBelow = null; // starts at null, in constructor of ILayer it will be set.
    if (this.h < 0 || this.h > 360 || this.s < 0 || this.s > 1 || this.l < 0 || this.l > 1) {
      throw new IllegalArgumentException("Invalid h, s, or l field for HSLPixel class.");
    }
  }


  @Override
  public String toString() {
    return this.h + " " + this.s + " " + this.l + "\n";
  }


  @Override
  public void apply(IFilterOption f) {
    // For multiply and screen filters, the below pixel needs to be of type HSL,
    // so we convert if necessary
    double lPrime = -1;

    if (this.pixelBelow != null) {

      if (this.pixelBelow.isRGB()) {
        String[] str = this.pixelBelow.toString().split(" ", -1);
        int r = Integer.parseInt(str[0]);
        int g = Integer.parseInt(str[1]);
        int b = Integer.parseInt(str[2]);
        this.pixelBelow = RepresentationConverter.convertRGBtoHSL(r, g, b, 255,
                this.pixelBelow.getPixelBelow());
      }

      // getting the l value of the pixel below this pixel, used in multiply and screen filters.
      String[] stringForL = this.pixelBelow.toString().split(" ", -1);
      lPrime = Double.parseDouble(stringForL[2]);

    }
    switch (f.toString()) {
      case "normal":
        break;
      case "red-component":

      case "blue-component":

      case "green-component":

      case "brighten-value":

      case "darken-value":

      case "brighten-luma":

      case "darken-luma":

      case "brighten-intensity":

      case "darken-intensity":

      case "difference":

        // for these cases, we need to use apply method in HSL class,
        // so we convert, apply, convert back, and then mutate with the converted back values.

        // convert pixel to rgb to do filter
        IPixel filteredPixel = RepresentationConverter.convertHSLtoRGB(
                this.h, this.s, this.l, this.a, this.pixelBelow);
        filteredPixel.apply(f);

        // get r, g, b values for filtered pixel so we can convert back
        String[] str = filteredPixel.toString().split(" ", -1);
        int filteredR = Integer.parseInt(str[0]);
        int filteredG = Integer.parseInt(str[1]);
        int filteredB = Integer.parseInt(str[2]);


        // now convert back
        IPixel convertedBack =
                RepresentationConverter.convertRGBtoHSL(filteredR, filteredG, filteredB,
                        this.a, this.pixelBelow);


        // get h, s, l values for convertedBack pixel so we can mutate this pixel to its values.
        String[] str2 = convertedBack.toString().split(" ", -1);
        int convertedH = Integer.parseInt(str2[0]);
        double convertedS = Double.parseDouble(str2[1]);
        double convertedL = Double.parseDouble(str2[2]);

        this.h = convertedH;
        this.s = convertedS;
        this.l = convertedL;

        break;
      case "multiply":
        if (this.pixelBelow != null) {
          // if there is a pixel below, do the filter.
          this.l = this.l * lPrime;
        }
        break;
      case "screen":
        if (this.pixelBelow != null) {
          this.l = 1 - ((1 - this.l) * (1 - lPrime));
        }
        break;
      default: // if it gets down here, it was an invalid filter name.
        throw new IllegalArgumentException("No filter with that name exists.");
    }
  }


  @Override
  public IPixel copy() {
    return new HSLPixel(this.h, this.s, this.l, this.a, this.maxValue, this.pixelBelow);
  }

  @Override
  public IPixel merge(IPixel bgPix) {

    // for merge, we will convert both this and given to RGB
    // and then use it's merge for code reuse purposes.

    // converting given to RGB if it is HSL
    if (bgPix.isHSL()) {
      String[] bgPixStr = bgPix.toString().split(" ", -1);
      int dH = Integer.parseInt(bgPixStr[0]);
      double dS = Double.parseDouble(bgPixStr[1]);
      double dL = Double.parseDouble(bgPixStr[2]);
      bgPix = RepresentationConverter.convertHSLtoRGB(dH, dS, dL, this.a, this.pixelBelow);
    }

    return RepresentationConverter.convertHSLtoRGB(
            this.h, this.s, this.l, this.a, this.pixelBelow).merge(bgPix);


  }

  @Override
  public double getValue() {
    double maxOfTwo = Math.max(this.h, this.s);
    return Math.max(maxOfTwo, this.l);
  }

  @Override
  public double getLuma() {
    return 0.2126 * this.h + 0.7152 * this.s + 0.0722 * this.l;
  }

  @Override
  public double getIntensity() {
    return (this.h + this.s + this.l) / 3;
  }


  @Override
  public int getMaxValue() {
    return this.maxValue;
  }

  @Override
  public boolean isRGB() {
    return false;
  }

  @Override
  public boolean isHSL() {
    return true;
  }

  @Override
  public IPixel getPixelBelow() {
    return pixelBelow;
  }

  @Override
  public void setPixelBelow(IPixel below) {
    this.pixelBelow = below;
  }


  /**
   * This helper method returns the current int, if it is over the max it sets it back to the max.
   *
   * @param current current int
   * @param max     max int
   * @return the integer clamped.
   */
  private int clampMax(int current, int max) {
    return Math.min(current, max);
  }
}
