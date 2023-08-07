package model;

import java.util.List;

/**
 * Represents an interface for a collage project which uses layers.
 * This interface represents the Collager's model, consisting of a list of layers which each are
 * a 2D list of IPixels. This is the model is used in the MVC.
 */
public interface IProject {

  /**
   * This method gets the layers from the project as a copy not a reference.
   */
  public List<ILayer> returnAllLayers();

  /**
   * This method puts the project into the desired format to be saved.
   *
   * @return the string format desired.
   */
  public String formatProject();

  /**
   * Adds a fully-transparent, white layer to the top of this project.
   *
   * @param name what the new layer will be named
   * @throws IllegalArgumentException if a layer with the given name already exists in this project
   */
  public void addLayer(String name) throws IllegalArgumentException;

  /**
   * Adds an image to the layer with the given name, with the provided coordinate being the
   * location within the layer of the image's top-left corner.
   *
   * @param layerName the name of the layer which the image is being added to
   * @param img the actual image to be added to the layer
   * @param x the x-coordinate within the layer, of the image's top-left corner location
   * @param y the y-coordinate within the layer, of the image's top-left corner location
   * @throws IllegalArgumentException if layerName is null, if img is null,
   *                                  if layerName is not the name of a layer which exists,
   *                                  if the provided x or y coordinates are negative,
   *                                  if the provided coordinate is not on the layer,
   *                                  or if part of the image would be off of the layer, given its
   *                                  size and desired location
   */
  public void addLayerImg(String layerName, List<List<IPixel>> img, int x, int y) throws
          IllegalArgumentException;


  /**
   * Compresses the project's layers to one, making an image.
   * @param name name you want the image to be.
   * @return the ILayer for the image.
   */
  public ILayer compressToImage(String name);

  /**
   * Gets the name of this IProject.
   * @return the String representing the name of the Project.
   */
  public String getName();

  /**
   * Applies the given FilterOption to the given file name.
   *
   * @param s the name of the layer to have the filter applied
   * @param f the filter that will be applied to the layer
   */
  public void applyFilterToCertainLayer(IFilterOption f, String s) throws IllegalArgumentException;

  /**
   * Sets the given filter to the given layer.
   * @param f filterOption that is given
   * @param s layerName that is given
   * @throws IllegalArgumentException exception
   */
  public void setFilterToCertainLayers(IFilterOption f, String s) throws IllegalArgumentException;

  /**
   * Starts this project with the given attributes for name, height, and width.
   *
   * @param name the name to be given to this collage project
   * @param height the height to be given to this collage project
   * @param width the width to be given to this collage project
   * @throws IllegalStateException if this collage project has already been started
   * @throws IllegalArgumentException if a null String is given for the name
   */
  public void startProject(String name, int height, int width)
          throws IllegalStateException, IllegalArgumentException;

  /**
   * Starts this project with the given attributes for name, list of layers, height, and width.
   * This method is for when you want to start a project and give it a list of layers, whereas
   * the other method for startProject starts with just the background layer.
   *
   * @param name the name to be given to this collage project
   * @param layers the layers to be given to this collage project
   * @param height the height to be given to this collage project
   * @param width the width to be given to this collage project
   * @throws IllegalStateException if this collage project has already been started
   * @throws IllegalArgumentException if a null String is given for the name,
   *                                  or if an empty list of layers is given.
   */
  public void startProject(String name, List<ILayer> layers, int height, int width)
          throws IllegalStateException, IllegalArgumentException;
}
