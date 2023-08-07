import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.IFilterOption;
import model.ILayer;
import model.IPixel;
import model.IProject;

/**
 * Mock class for testing controller's interaction with the model.
 */
public class CollageProjectMock implements IProject {
  private String name;
  private Appendable log;

  /**
   * Constructor for the mock.
   *
   * @param name   the name of the mock collage
   * @param log    the log of inputs from the controller
   */
  public CollageProjectMock(String name, Appendable log) {
    this.log = log;
    this.name = name;
  }


  /**
   * Returns the log of text entered by the user.
   */
  @Override
  public String toString() {
    return this.log.toString();
  }

  /**
   * Mock for this method has no arguments so just return null.
   *
   * @return all layers of this project
   */
  @Override
  public ArrayList<ILayer> returnAllLayers() {
    return null;
  }

  /**
   * Mock for this method has no arguments so just return null.
   *
   * @return all layers of this project
   */
  public String formatProject() {
    return null;
  }

  /**
   * Mock for addLayer.
   * @param name what the new layer will be named
   * @throws IllegalArgumentException throws exception
   */
  @Override
  public void addLayer(String name) throws IllegalArgumentException {
    try {
      this.log.append("name: ").append(name).append("\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  /**
   * Mock method for addLayerImg.
   * @param layerName the name of the layer which the image is being added to
   * @param img the actual image to be added to the layer
   * @param x the x-coordinate within the layer, of the image's top-left corner location
   * @param y the y-coordinate within the layer, of the image's top-left corner location
   * @throws IllegalArgumentException throws exception
   */
  @Override
  public void addLayerImg(String layerName, List<List<IPixel>> img, int x, int y)
          throws IllegalArgumentException {
    try {
      this.log.append("layerName: " + layerName + " img: " + img.toString() + " x: " + x
              + " y: " + y + "\n");
    } catch (IOException e) {
      // do nothing
    }
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
    try {
      this.log.append("height: " + height + " width: " + width + "\n");
    } catch (IOException e) {
      // do nothing
    }
    return null;
  }

  /**
   * Mock method for compress to image.
   * @param name name you want the image to be.
   * @return
   */
  @Override
  public ILayer compressToImage(String name) {
    try {
      this.log.append("name: " + name + "\n");
    } catch (IOException e) {
      // do nothing
    }
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  /**
   * Mock method for apply filter to a certain layer.
   * @param f FilterOption
   * @param s string
   * @throws IllegalArgumentException throws exception
   */
  @Override
  public void applyFilterToCertainLayer(IFilterOption f, String s) throws IllegalArgumentException {
    try {
      this.log.append("FilterOption: " + name + " LayerName: " + s + "\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void setFilterToCertainLayers(IFilterOption f, String s) throws IllegalArgumentException {
    try {
      this.log.append("FilterOption: " + name + " LayerName: " + s + "\n");
    } catch (IOException e) {
      // do nothing
    }
  }

  @Override
  public void startProject(String name, int height, int width)
          throws IllegalStateException, IllegalArgumentException {
    try {
      this.log.append("name: " + name + " height, width: " + height + "," + width + "\n");
    } catch (IOException e) {
      // do nothing
    }

  }

  @Override
  public void startProject(String name, List<ILayer> layers, int height, int width)
          throws IllegalStateException, IllegalArgumentException {
    try {
      this.log.append("name: " + name + "layers: " + layers
              + " height, width: " + height + "," + width + "\n");
    } catch (IOException e) {
      // do nothing
    }
  }
}

