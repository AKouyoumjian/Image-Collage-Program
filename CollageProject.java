package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for a Project consisting of a layers.
 * Layers at the front of the arrayList are on the bottom. Index 0 is the bottom-most layer.
 * This class represents the Collager's model, consisting of a list of layers which each are
 * a 2D list of IPixels. This is the model is used in the MVC.
 */
public class CollageProject implements IProject {
  private List<ILayer> layers;
  private String name;
  private int height;
  private int width;
  private int maxPixel;
  private boolean started;

  /**
   * Main constructor for Collage Project.
   *
   * @param name   string name
   * @param height int height
   * @param width  int width
   */
  public CollageProject(String name, int height, int width) {
    this.layers = new ArrayList<>(Arrays.asList(this.makeBackgroundLayer(height, width)));
    this.name = name;
    this.height = height;
    this.width = width;
    this.maxPixel = 255; // max ppm pixel amount
    if (this.name == null || this.height < 0 || this.width < 0) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.started = false;
  }

  /**
   * Alternative constructor for Collage Project for giving it a max alpha (not assuming it is 255).
   *
   * @param name      string name
   * @param height    int height
   * @param width     int width
   * @param maxPixels int maxPixels
   */
  public CollageProject(String name, int height, int width, int maxPixels) {
    this.layers = new ArrayList<>(Arrays.asList(this.makeBackgroundLayer(height, width)));
    this.name = name;
    this.height = height;
    this.width = width;
    this.maxPixel = maxPixels; // max ppm pixel amount
    if (this.name == null || this.maxPixel < 0 || this.height < 0 || this.width < 0) {
      throw new IllegalArgumentException("Name cannot be null.");
    }
    this.started = false;
  }

  /**
   * Alternate constructor for Collage Project.
   * This is for when you want to make a collage with layers already made.
   *
   * @param name   string name
   * @param height height
   * @param width  width
   * @param layers layers
   */
  public CollageProject(String name, List<ILayer> layers, int height, int width) {
    this.layers = layers;
    this.name = name;
    this.height = height;
    this.width = width;
    this.maxPixel = 255; // max ppm pixel amount
    this.started = false;
  }


  /**
   * Returns all the layers of a project so it can be saved.
   *
   * @return the collection of layers as a list
   */
  @Override
  public List<ILayer> returnAllLayers() {

    ArrayList<ILayer> list = new ArrayList<ILayer>();
    for (int i = 0; i < this.layers.size(); i++) {

      String name = this.layers.get(i).getName();
      IFilterOption filter = this.layers.get(i).getFilter();
      List<List<IPixel>> pixels = this.layers.get(i).getPixelArrayCopy();

      ILayer current = new CollageLayer(name, pixels, filter,
              this.layers.get(i).getHeight(), this.layers.get(i).getWidth());


      list.add(current);

    }
    return list;
  }

  @Override
  public String getName() {
    return this.name;
  }


  /**
   * Formats the project into Collager format.
   *
   * @return the string formatted.
   */
  public String formatProject() {
    String format = this.name + "\n" + this.width + " " + this.height + "\n"
            + this.maxPixel + "\n";

    for (ILayer layer : this.layers) {

      format = format + layer.toString();

    }

    return format;

  }

  @Override
  public void addLayer(String name) throws IllegalArgumentException {
    for (ILayer layer : this.layers) {
      if (layer.getName().equals(name)) {
        throw new IllegalArgumentException("A layer with the given name already exists.");
      }
    }
    List<List<IPixel>> newLayerPix = new ArrayList<>();
    for (int row = 0; row < this.height; row++) {
      List<IPixel> newRow = new ArrayList<>();
      for (int col = 0; col < this.width; col++) {
        newRow.add(new RGBPixel(255, 255, 255, 0));
      }
      newLayerPix.add(newRow);
    }
    this.layers.add(new CollageLayer(name, newLayerPix, this.height, this.width));
  }


  @Override
  public void addLayerImg(String layerName, List<List<IPixel>> img, int x, int y)
          throws IllegalArgumentException {
    if (layerName == null || img == null) {
      throw new IllegalArgumentException("Cannot have a null layer name or image.");
    }
    // img height can be tracked
    int imgHeight = img.size();
    // layer of interest is initialized to the bottom-most layer
    ILayer addTo = this.layers.get(0);
    boolean match = false;
    for (ILayer l : this.layers) {
      if (l.getName().equals(layerName)) {
        match = true;
        addTo = l;
      }
    }
    if (!match) {
      throw new IllegalArgumentException("A Layer with that name does not exist in this collage.");
    }
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Cannot have negative x/y coordinates.");
    }
    if (x > addTo.getWidth() || y > addTo.getHeight()) {
      throw new IllegalArgumentException("Provided x/y coordinate is not on the layer.");
    }
    // the case where the image's rows do not all have the same width
    int imgWidth = img.get(0).size();
    for (int i = 1; i < imgHeight; i++) {
      if (img.get(i).size() != imgWidth) {
        throw new IllegalArgumentException("Image rows do not have the same amount of pixels.");
      }
    }
    // the case where the image is bigger than the layer
    if (imgHeight * imgWidth > addTo.getHeight() * addTo.getWidth()) {
      throw new IllegalArgumentException("Layer is not big enough to support this image.");
    }
    // the case where the image is NOT bigger than the layer, but when it is in a position in which
    // it would need to be cropped in order to fit on the layer
    if (addTo.getHeight() - y < imgHeight || addTo.getWidth() - x < imgWidth) {
      throw new IllegalArgumentException("Image must be placed in a different location to fit it "
              + " onto the layer.");
    }
    // add that image to the given layer
    addTo.addImg(img, x, y);
  }


  /**
   * This private method makes an all-white background layer
   * that is used as a default background when making a project.
   *
   * @param height int height of the layer
   * @param width  int width of the layer
   * @return the ILayer background
   */
  private ILayer makeBackgroundLayer(int height, int width) {

    List<List<IPixel>> list = new ArrayList<>();

    RGBPixel whitePix = new RGBPixel(255, 255, 255, 1);

    for (int i = 0; i < height; i++) {

      ArrayList<IPixel> inner = new ArrayList();

      for (int j = 0; j < width; j++) {
        inner.add(whitePix.copy());
      }
      list.add(inner);
    }
    return new CollageLayer("background", list, height, width);
  }

  @Override
  public ILayer compressToImage(String name) {
    // apply each layer's filter to each layer in the Project's list
    for (ILayer layer : this.layers) {
      layer.applyFilter(layer.getFilter());
    }

    if (this.layers.size() == 1) {
      return this.layers.get(0);
    }
    else {

      ILayer finalImg = new CollageLayer("final image",
              this.layers.get(1).getPixelArrayCopy(), this.height, this.width);
      // get at layers at index 1 since we don't want background, so we start at index 1.


      // now we combine the bottom two layers recursively until all layers have been combined.

      // start i at 1 since finalImg is a layer with the 1st layer already
      for (int i = 2; i < this.layers.size(); i++) {
        // mergeLayers will merge from bottom up with the layer it is called on being the bottom one
        finalImg = finalImg.mergeLayers(this.layers.get(i));
      }

      return finalImg;
    }

  }


  @Override
  public void applyFilterToCertainLayer(IFilterOption f, String s) throws IllegalArgumentException {

    // this is the layer we will apply the filter to.
    // if it remains null then we know to throw illegalArg exception.
    ILayer layer = null;

    // find the layer which the instruction asks for
    for (ILayer l : this.layers) {
      if (l.getName().equals(s)) {
        layer = l;
      }
    }
    // if layer remains null, there is no layer w that name in Project so we throw exception.
    if (layer == null) {
      throw new IllegalArgumentException("No layer with given name exists in the Project.");
    }

    // finally, we apply the given filter to the layer.
    layer.applyFilter(f);
  }

  @Override
  public void setFilterToCertainLayers(IFilterOption f, String s) throws IllegalArgumentException {
    // this is the layer we will apply the filter to.
    // if it remains null then we know to throw illegalArg exception.
    ILayer layer = null;

    // find the layer which the instruction asks for
    for (ILayer l : this.layers) {
      if (l.getName().equals(s)) {
        layer = l;
      }
    }
    // if layer remains null, there is no layer w that name in Project so we throw exception.
    if (layer == null) {
      throw new IllegalArgumentException("No layer with given name exists in the Project.");
    }
    // finally, we apply the given filter to the layer.
    layer.setFilter(f);
  }

  @Override
  public void startProject(String name, int height, int width)
          throws IllegalStateException, IllegalArgumentException {
    if (!this.started) {
      throw new IllegalStateException("Cannot start a project which has already been started.");
    }
    if (name == null) {
      throw new IllegalArgumentException("Cannot start a project with a null name.");
    }
    if (height <= 0 || width <= 0) {
      throw new IllegalArgumentException("Height and width must be positive to start a project.");
    }
    this.started = true;
    this.name = name;
    this.height = height;
    this.width = width;
  }

  @Override
  public void startProject(String name, List<ILayer> layers, int height, int width)
          throws IllegalStateException, IllegalArgumentException {
    if (!this.started) {
      throw new IllegalStateException("Cannot start a project which has already been started.");
    }
    if (name == null) {
      throw new IllegalArgumentException("Cannot start a project with a null name.");
    }
    if (height <= 0 || width <= 0) {
      throw new IllegalArgumentException("Height and width must be positive to start a project.");
    }
    this.started = true;
    this.name = name;
    this.layers = layers;
    this.height = height;
    this.width = width;
  }
}


