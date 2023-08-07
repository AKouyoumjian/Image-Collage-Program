package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for a CollageLayer that represents an image with a position
 * relative to other layers.
 * The list of pixels are in lists of rows.
 */
public class CollageLayer implements ILayer {
  private final String name;
  private final List<List<IPixel>> originalPix; // original pixels before any filters applied
  // we keep track of original pixels so when filters are applied to an already filtered layer,
  // the filter is applied correctly to the original.
  private List<List<IPixel>> currentPix;
  private final int height;
  private final int width;
  private IFilterOption filter;
  private final List<List<List<IPixel>>> originalImgs;
  private final List<int[]> coordImgs;

  /**
   * Constructor for a CollageLayer.
   *
   * @param pixels the image of this layer, represented via pixels. used to initialize the
   *               originalPix parameter, which a copy is made of to initialize the
   *               currentPix parameter
   * @param width  integer for this layer's width
   */
  public CollageLayer(String name, List<List<IPixel>> pixels, int height, int width) {
    if (name == null || pixels == null) {
      throw new IllegalArgumentException("Layer cannot have null value for its name or rows of" +
              " pixels.");
    }
    if (width <= 0 || height <= 0) {
      // checking if the provided width or height is invalid
      throw new IllegalArgumentException("Layer width and height must be greater than 0.");
    }
    // checking that pixels is not an empty list, or has empty rows
    if (pixels.size() == 0 || pixels.get(0).size() == 0) {
      throw new IllegalArgumentException("Cannot have a layer with no rows, or without an pixels"
              + " in one of its rows.");
    }
    // checking that all rows have the right amount of pixels for the specified width
    // ex: making sure a row doesn't only have 4 pixels, if the layer has a width of ten.
    for (int i = 0; i < pixels.size(); i++) {
      List<IPixel> row = pixels.get(i);
      if (row.size() != width) {
        throw new IllegalArgumentException("Row " + (i + 1) + " does not have an amount pixels"
                + " equal to the given width.");
      }
    }
    // making sure that provided width and height match pixel dimensions
    if (pixels.size() != height || pixels.get(0).size() != width) {
      throw new IllegalArgumentException("Provided height and width do not match the dimensions"
              + " of the given pixels.");
    }
    this.name = name;
    this.originalPix = pixels;
    this.height = height;
    this.width = width;
    this.filter = FilterOption.NORM;
    this.originalImgs = new ArrayList<>();
    this.coordImgs = new ArrayList<>();
    // the current pixels are initialized to a copy of the pixels provided
    this.currentPix = new ArrayList<>();
    for (int row = 0; row < this.height; row++) {
      List<IPixel> newRow = new ArrayList<>();
      for (int col = 0; col < this.width; col++) {
        IPixel pixCopy = this.getOriginalPixel(row, col).copy();
        newRow.add(pixCopy);
      }
      this.currentPix.add(newRow);
    }

    this.setAllBelowPixels(); // called in constructor so that all pixels knowing their below pix

  }

  /**
   * Constructor for CollageLayer with filterOption a passed, so you can instantiate
   * a layer with a filter already applied.
   * @param name   String for the name.
   * @param pixels the image of this layer, represented via pixels. used to initialize the
   *               *                    originalPix parameter, which a copy is made of to
   *                                    initialize the currentPix parameter
   * @param height int for layers height
   * @param width  integer for this layer's width
   */
  public CollageLayer(
          String name, List<List<IPixel>> pixels, IFilterOption filter, int height, int width) {
    if (name == null || pixels == null) {
      throw new IllegalArgumentException("Layer cannot have null value for its name or rows of" +
              " pixels.");
    }
    if (width <= 0 || height <= 0) {
      // checking if the provided width or height is invalid
      throw new IllegalArgumentException("Layer width and height must be greater than 0.");
    }
    // checking that pixels is not an empty list, or has empty rows
    if (pixels.size() == 0 || pixels.get(0).size() == 0) {
      throw new IllegalArgumentException("Cannot have a layer with no rows, or without an pixels"
              + " in one of its rows.");
    }
    // checking that all rows have the right amount of pixels for the specified width
    // ex: making sure a row doesn't only have 4 pixels, if the layer has a width of ten.
    for (int i = 0; i < pixels.size(); i++) {
      List<IPixel> row = pixels.get(i);
      if (row.size() != width) {
        throw new IllegalArgumentException("Row " + (i + 1) + " does not have an amount pixels"
                + " equal to the given width.");
      }
    }
    // making sure that provided width and height match pixel dimensions
    if (pixels.size() != height || pixels.get(0).size() != width) {
      throw new IllegalArgumentException("Provided height and width do not match the dimensions"
              + " of the given pixels.");
    }
    this.name = name;
    this.originalPix = pixels;
    this.height = height;
    this.width = width;
    this.filter = filter;
    this.originalImgs = new ArrayList<>();
    this.coordImgs = new ArrayList<>();
    // the current pixels are initialized to a copy of the pixels provided
    this.currentPix = new ArrayList<>();
    for (int row = 0; row < this.height; row++) {
      List<IPixel> newRow = new ArrayList<>();
      for (int col = 0; col < this.width; col++) {
        IPixel pixCopy = this.getOriginalPixel(row, col).copy();
        newRow.add(pixCopy);
      }
      this.currentPix.add(newRow);
    }
    this.setAllBelowPixels(); // called in constructor so that all pixels knowing their below pix
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getPPM() throws IOException {
    Appendable textPPM = new StringBuilder();
    try {
      textPPM.append("P3\n# ").append(this.name).append(".ppm\n");
      textPPM.append(String.valueOf(this.width)).append(" ").append(String.valueOf(this.height));
      textPPM.append("\n").append(String.valueOf(this.getPixel(0, 0).getMaxValue()));
      textPPM.append("\n");
      for (int row = 0; row < this.height; row++) {
        for (int col = 0; col < this.width; col++) {
          IPixel current = this.getPixel(row, col);
          String[] pixSplit = current.toString().split(" ");
          textPPM.append(pixSplit[0]).append(" ");
          textPPM.append(pixSplit[1]).append(" ");
          textPPM.append(pixSplit[2]);
          if (col == this.width - 1) {
            // the case where the pixel is the last in the row
            textPPM.append("\n");
          } else {
            // the case where the pixel is not the last in the row
            textPPM.append(" ");
          }
        }
      }
    } catch (IOException e) {
      throw new IOException("Could not append to the string builder.");
    }
    return textPPM.toString();
  }

  @Override
  public IPixel getOriginalPixel(int row, int col) throws IllegalArgumentException {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Cannot have a pixel with a negative row or column");
    }
    if (row > this.height) {
      throw new IllegalArgumentException("Row value is out-of-bounds for this layer.");
    }
    if (col > this.width) {
      throw new IllegalArgumentException("Column value is out-of-bounds for this layer.");
    }
    return this.originalPix.get(row).get(col);
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public void applyFilter(IFilterOption f) {
    this.filter = f;
    if (f.toString().equals("normal")) {
      // if the user wants to see the unaltered state of the original image, initialize the current
      // display of pixels to be a copy of the original pixels
      this.currentPix = new ArrayList<>();
      for (int row = 0; row < height; row++) {
        List<IPixel> newRow = new ArrayList<>();
        for (int col = 0; col < width; col++) {
          IPixel pixCopy = this.getOriginalPixel(row, col).copy();
          newRow.add(pixCopy);
        }
        this.currentPix.add(newRow);
      }
    }
    // the case where the user wants to see some altered version of the original pixels
    // assigning the currently displayed pixels to a new ArrayList
    this.currentPix = new ArrayList<>();
    for (int row = 0; row < this.height; row++) {
      // creating the new row to be added to the currently displayed pixels
      List<IPixel> newRow = new ArrayList<>();
      //the row of the original pixels which corresponds to the row index
      List<IPixel> origRow = this.originalPix.get(row);
      for (int col = 0; col < this.width; col++) {
        // creating a copy of the original pixel at the column index, then applying the
        // given filter to it
        IPixel newPix = origRow.get(col).copy();
        newPix.apply(f);
        // adding the filtered copy of the original pixel to the new row of currently
        // displayed pixels
        newRow.add(newPix);
      }
      // adding the row of newly filtered original pixels to the 2d list of currently
      // displayed pixels
      currentPix.add(newRow);
    }
  }

  @Override
  public IPixel getPixel(int row, int col) throws IllegalArgumentException {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("Cannot have a pixel with a negative row or column");
    }
    if (row > this.height) {
      throw new IllegalArgumentException("Row value is out-of-bounds for this layer.");
    }
    if (col > this.width) {
      throw new IllegalArgumentException("Column value is out-of-bounds for this layer.");
    }
    return this.currentPix.get(row).get(col);
  }

  @Override
  public IFilterOption getFilter() {
    return this.filter;
  }

  @Override
  public String toString() {
    String str = this.name + " " + this.filter.toString() + "\n";

    for (List<IPixel> pix : currentPix) {


      for (IPixel pix2 : pix) {
        str = str + pix2.toString();
      }

    }
    return str;
  }

  @Override
  public List<List<IPixel>> getPixelArrayCopy() {
    List<List<IPixel>> list = new ArrayList<>();
    for (int i = 0; i < this.height; i++) {
      ArrayList<IPixel> embeddedList = new ArrayList<IPixel>();
      for (int k = 0; k < this.width; k++) {
        embeddedList.add(this.getOriginalPixel(i, k).copy());
      }
      list.add(embeddedList);
    }
    return list;
  }


  @Override
  public void addImg(List<List<IPixel>> img, int x, int y) throws IllegalArgumentException {

    if (img == null) {
      throw new IllegalArgumentException("Cannot use null as an image.");
    }
    // throwing an exception if the x/y are out-of-bounds
    if (x < 0 || y < 0 || x > this.width || y > this.height) {
      throw new IllegalArgumentException("Coordinate out-of-bounds. X/Y must be positive"
              + " and within the layer.");
    }

    try {
      for (int i = 0; i < img.size(); i++) {

        for (int k = 0; k < img.get(0).size(); k++) {

          IPixel merged = img.get(i).get(k).merge(this.currentPix.get(i + x).get(k + y)).copy();
          this.originalPix.get(i + x).set(k + y, merged);

        }
      }
    }
    catch (IndexOutOfBoundsException e) {
      // if index out of bounds occurs, the image is too big,
      // so we throw an illegal argument exception.
      throw new IllegalArgumentException("Image too large to be placed at given coordinates.");
    }
  }

  @Override
  public void setFilter(IFilterOption f) {
    this.filter = f;
  }

  @Override
  public void setAllBelowPixels() {
    // only for pixels not on lowest layer, so i < size() - 1;
    for (int i = 0; i < this.currentPix.size() - 1; i ++) {

      for (int j = 0; j < this.currentPix.get(i).size(); j++) {
        IPixel below = currentPix.get(i + 1).get(j);
        this.currentPix.get(i).get(j).setPixelBelow(below);
      }
    }
  }


  @Override
  public ILayer mergeLayers(ILayer other) {
    List<List<IPixel>> otherPix = other.getPixelArrayCopy();

    // set mergedPixels to current array copy to keep array structure we want (size, height, width)
    // it will be mutated with merged values below.
    List<List<IPixel>> mergedPixels = this.getPixelArrayCopy();

    // for each pixel in currentPix, merge with corresponding pixel in other layer.
    for (int i = 0; i < this.height; i++ ) {

      for (int j = 0; j < this.width; j++) {
        IPixel current = this.currentPix.get(i).get(j).merge(otherPix.get(i).get(j));
        mergedPixels.get(i).set(j, current);
      }
    }
    return new CollageLayer(other.getName(), mergedPixels, this.height, this.width);
  }
}