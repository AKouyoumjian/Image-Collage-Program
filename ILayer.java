package model;

import java.io.IOException;
import java.util.List;

/**
 * Interface for a layer in a visual project.
 * Represents an image.
 */
public interface ILayer {

  /**
   * Gets the name of this layer.
   *
   * @return the name of this layer
   */
  String getName();

  /**
   * Gets the pixel FROM ORIGINAL LAYER which exists at a specific row and column coordinate.
   * The top left pixel has
   * the coordinate value (0, 0), with row/column values increasing down/right, respectively.
   *
   * @param row the number of the pixel's row
   * @param col the position of the pixel within its row
   * @return the pixel at the specified row and column
   * @throws IllegalArgumentException if the specified row or column are invalid, or if they are
   *                                  out of bounds for this layer
   */
  IPixel getOriginalPixel(int row, int col) throws IllegalArgumentException;


  /**
   * Gets the pixel FROM CURRENT LAYER which exists at a specific row and column coordinate.
   * The top left pixel has
   * the coordinate value (0, 0), with row/column values increasing down/right, respectively.
   *
   * @param row the number of the pixel's row
   * @param col the position of the pixel within its row
   * @return the pixel at the specified row and column
   * @throws IllegalArgumentException if the specified row or column are invalid, or if they are
   *                                  out of bounds for this layer
   */
  IPixel getPixel(int row, int col) throws IllegalArgumentException;

  /**
   * Gets the height of this layer.
   *
   * @return the height of this layer
   */
  int getHeight();

  /**
   * Gets the width of this layer.
   *
   * @return the width of this layer
   */
  int getWidth();

  /**
   * Applies a given filter to this layer, altering the values of pixels according to the filter.
   *
   * @param f the filter which is going to be applied to this image.
   */
  void applyFilter(IFilterOption f);

  /**
   * Gets the filter being applied to the layer.
   * @return the string for this filter.
   */
  IFilterOption getFilter();

  /**
   * Gets the layer-name, filter-name, and then, on a seperate line, the LAYER-CONTENT-FORMAT.
   * @return the string format of ILayer.
   */
  String toString();


  /**
   * Make a copy of the list of pixels.
   * @return the 2d copy list of pixels.
   */
  List<List<IPixel>> getPixelArrayCopy();

  /**
   * Adds an image to this layer, with the desired x/y coordinate representing the location of
   * the image's top-left corner within the layer.
   *
   * @param img the image to be added to the layer
   * @param x the x coordinate of the location of the image's top-left corner
   * @param y the y coordinate of the location of the image's top-left corner
   * @throws IllegalArgumentException throws exception
   */
  void addImg(List<List<IPixel>> img, int x, int y) throws IllegalArgumentException;

  /**
   * Sets the layer's filter to the given filterOption.
   * @param f filterOption
   */
  void setFilter(IFilterOption f);

  /**
   * Sets all IPixels in the layer to know their below IPixel.
   */
  void setAllBelowPixels();

  /**
   * Converts this ILayer into a ppm file format.
   *
   * @return this layer as a String ppm format
   */
  String getPPM() throws IOException;

  /**
   * Merges this layer with another layer. The merge will be from bottom up with this layer being
   * the below one. So, for example, if the other layer has no transparency and is completely filled
   * with the color pink, this method will return just that layer.
   * @param other other ILayer
   * @return the merged ILayer
   */
  ILayer mergeLayers(ILayer other);
}
