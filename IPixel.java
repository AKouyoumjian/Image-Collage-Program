package model;

/**
 * Represents a pixel for various visual projects.
 */
public interface IPixel {

  /**
   * Creates a String representation of this IPixel's color and alpha values.
   */
  String toString();

  /**
   * Applies a given filter to this pixel.
   *
   * @param f the filter to apply to this pixel
   */
  void apply(IFilterOption f);

  /**
   * Creates a copy of this pixel.
   *
   * @return separate pixel object identical to this pixel.
   */
  IPixel copy();

  /**
   * Using the formula provided, merges two pixels into one.
   * @param pix other pixel being combined.
   * @return the new pixel which is a combo of the two.
   */
  IPixel merge(IPixel pix);

  /**
   * Gets the brighten/darken value for this pixel.
   * @return the integer for the value.
   */
  double getValue();

  /**
   * Gets the brighten/darken Luma for this pixel.
   * @return the integer for the Luma.
   */
  double getLuma();

  /**
   * Gets the brighten/darken intensity for this pixel.
   * @return the integer for the intensity.
   */
  double getIntensity();

  /**
   * Gets the max value of the pixel.
   * @return the int for the max value.
   */
  int getMaxValue();

  /**
   * Determines if the IPixel type RGB.
   * @return the boolean for this answer.
   */
  boolean isRGB();

  /**
   * Determines if the IPixel type HSL.
   * @return the boolean for this answer.
   */
  boolean isHSL();

  /**
   * Gets the IPixel below this IPixel.
   * @return the IPixel below.
   */
  IPixel getPixelBelow();

  /**
   * Sets this IPixel's pixelBelow field to be the given IPixel.
   * @param below the IPixel below this one.
   */
  void setPixelBelow(IPixel below);

}
