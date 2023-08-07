import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.CollageLayer;
import model.CollageProject;
import model.FilterOption;
import model.ILayer;
import model.IPixel;
import model.IProject;
import model.RGBPixel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for CollageProject class constructor and its methods.
 */
public class CollageProjectTest {
  IPixel topLeft;
  IPixel topRight;
  IPixel midLeft;
  IPixel midRight;
  IPixel botLeft;
  IPixel botRight;
  List<List<IPixel>> cLayerPix;
  ILayer cLayer;

  List<List<IPixel>> cLayerPix2;


  IProject project;

  @Before
  public void init() {
    topLeft = new RGBPixel(250, 250, 250, 100);
    topRight = new RGBPixel(250, 250, 250, 50);
    midLeft = new RGBPixel(30, 100, 170, 100);
    midRight = new RGBPixel(170, 100, 30, 100);
    botLeft = new RGBPixel(250, 250, 0, 0);
    botRight = new RGBPixel(250, 0, 250, 100);
    cLayerPix = new ArrayList<>();
    cLayerPix2 = new ArrayList<>();

    ArrayList<IPixel> topRow = new ArrayList<IPixel>();
    topRow.add(topLeft);
    topRow.add(topRight);
    ArrayList<IPixel> midRow = new ArrayList<IPixel>();
    midRow.add(midLeft);
    midRow.add(midRight);
    ArrayList<IPixel> botRow = new ArrayList<IPixel>();
    botRow.add(botLeft);
    botRow.add(botRight);
    cLayerPix.add(topRow);
    cLayerPix.add(midRow);
    cLayerPix.add(botRow);

    cLayerPix2.add(midRow);
    cLayerPix2.add(botRow);
    cLayerPix2.add(topRow);


    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    project = new CollageProject("project1", 4, 3);
  }


  /**
   * Test for invalid constructor in CollageProject.
   */
  @Test
  public void testInvalidConstructor() {
    try {
      IProject project = new CollageProject(null, 1, 1);
      fail("Should have thrown IllegalArg.");
    }
    catch (IllegalArgumentException e) {
      // do nothing.
    }
    try {
      IProject project = new CollageProject("name", -1, 1);
      fail("Should have thrown IllegalArg.");
    }
    catch (IllegalArgumentException e) {
      // do nothing.
    }
    try {
      IProject project = new CollageProject("name", 1, -1, 1);
      fail("Should have thrown IllegalArg.");
    }
    catch (IllegalArgumentException e) {
      // do nothing.
    }
    try {
      IProject project = new CollageProject("name", 1, 1, -22);
      fail("Should have thrown IllegalArg.");
    }
    catch (IllegalArgumentException e) {
      // do nothing.
    }
  }

  /**
   * Test for returnAllLayers method in CollageProject.
   */
  @Test
  public void testReturnAllLayers() {
    assertEquals("[background normal\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "]", project.returnAllLayers().toString());


    // test for 3 non-white layers
    ArrayList<ILayer> layers = new ArrayList<>(Arrays.asList(cLayer, cLayer, cLayer));
    CollageProject project2 = new CollageProject("project 2", layers, 4, 3);
    // test for 3 non-white layers
    assertEquals("[first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "30 100 170 100\n" +
            "170 100 30 100\n" +
            "250 250 0 0\n" +
            "250 0 250 100\n" +
            ", first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "30 100 170 100\n" +
            "170 100 30 100\n" +
            "250 250 0 0\n" +
            "250 0 250 100\n" +
            ", first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "30 100 170 100\n" +
            "170 100 30 100\n" +
            "250 250 0 0\n" +
            "250 0 250 100\n" +
            "]", project2.returnAllLayers().toString());
  }

  /**
   * Test for formatProject method in CollageProject.
   */
  @Test
  public void testFormatProject() {

    assertEquals("project1\n" +
            "3 4\n" +
            "255\n" +
            "background normal\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n", project.formatProject());


    // test for 3 non-white layers
    ArrayList<ILayer> layers = new ArrayList<>(Arrays.asList(cLayer, cLayer, cLayer));
    CollageProject project2 = new CollageProject("project 2", layers, 4, 3);
    // test for 3 non-white layers
    assertEquals("project 2\n" +
            "3 4\n" +
            "255\n" +
            "first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "30 100 170 100\n" +
            "170 100 30 100\n" +
            "250 250 0 0\n" +
            "250 0 250 100\n" +
            "first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "30 100 170 100\n" +
            "170 100 30 100\n" +
            "250 250 0 0\n" +
            "250 0 250 100\n" +
            "first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "30 100 170 100\n" +
            "170 100 30 100\n" +
            "250 250 0 0\n" +
            "250 0 250 100\n", project2.formatProject());
  }

  /**
   * Test for addLayer method in CollageProject.
   */
  @Test
  public void testAddLayer() {
    // check before adding layer
    assertEquals("[background normal\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "]", project.returnAllLayers().toString());


    // now test to see difference after layer has been added
    project.addLayer("layer1");
    assertEquals("[background normal\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            ", layer1 normal\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "]", project.returnAllLayers().toString());

    // now test for add layer w same name as already exists
    try {
      project.addLayer("layer1");
      fail("Add layer of same name should have thrown an exception, if it gets here it didnt.");
    } catch (IllegalArgumentException e) {
      // do nothing, since we want to get here if method works properly
    }

    // show here that adding layer of different name does not throw exception
    project.addLayer("layerDIFFERENTNAME");

  }


  /**
   * Test for addLayerImg method in CollageProject.
   */
  @Test
  public void testAddLayerImg() {

    project.addLayerImg("background", cLayerPix, 0, 0);
    assertEquals("[background normal\n" +
            "248 248 248 100\n" +
            "246 246 246 50\n" +
            "255 255 255 1\n" +
            "30 100 169 100\n" +
            "169 100 30 100\n" +
            "255 255 255 1\n" +
            "1 1 1 1\n" +
            "248 1 248 100\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "]", project.returnAllLayers().toString());

    // now test for invalid calls of the method
    // test for if no layer of that name is in project
    try {
      project.addLayerImg("wrongName", cLayerPix, 0, 0);
      fail("should have thown exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test for if layer given is null
    try {
      project.addLayerImg(null, cLayerPix, 0, 0);
      fail("should have thown exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test for if image given is null
    try {
      project.addLayerImg("background", null, 0, 0);
      fail("should have thown exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test for if x input is negative
    try {
      project.addLayerImg("background", cLayerPix, -2, 0);
      fail("should have thown exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test for if y input is negative
    try {
      project.addLayerImg("background", cLayerPix, 2, -1);
      fail("should have thown exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test for if x input is larger than width
    try {
      project.addLayerImg("background", cLayerPix, 4, 0);
      fail("should have thown exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test for if y input is larger than height
    try {
      project.addLayerImg("background", cLayerPix, 0, 10);
      fail("should have thown exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
  }


  /**
   * Test for compressToImage method in CollageProject.
   */
  @Test
  public void testCompressToImage() {

    assertEquals("background normal\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n", this.project.compressToImage("name").toString());


    project.addLayer("layerNext");
    project.addLayerImg("layerNext", cLayerPix2, 0, 0);

    // test for compressing multiple layers
    assertEquals("final image normal\n" +
            "29 100 170 100\n" +
            "170 100 29 100\n" +
            "255 255 255 0\n" +
            "0 0 0 0\n" +
            "249 0 249 100\n" +
            "255 255 255 0\n" +
            "249 249 249 100\n" +
            "249 249 249 50\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n" +
            "255 255 255 0\n", project.compressToImage("name").toString());

  }

  /**
   * Test for applyFilterToCertainLayer method in CollageProject.
   */
  @Test
  public void testApplyFilterToCertainLayer() {

    // test to show filter is normal
    assertEquals("[background normal\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "]", project.returnAllLayers().toString());


    // now change filter, apply brighten-value
    project.applyFilterToCertainLayer(FilterOption.BRIGHTV, "background");

    // test to show filter has changed to darken-luma
    assertEquals("[background brighten-value\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "]", project.returnAllLayers().toString());


    // now change filter, apply blue-component
    project.applyFilterToCertainLayer(FilterOption.BLUE, "background");

    // test to show filter has changed to darken-luma
    assertEquals("[background blue-component\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "]", project.returnAllLayers().toString());


    // now finally, apply darken-luma filter
    project.applyFilterToCertainLayer(FilterOption.DARKL, "background");

    // test to show filter has changed to darken-luma
    assertEquals("[background darken-luma\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "255 255 255 1\n" +
            "]", project.returnAllLayers().toString());


    // tests for illegalArg exception if no layer w the name exists
    try {
      project.applyFilterToCertainLayer(FilterOption.RED, "wrongName");
      fail("Should have thrown an IllegalArg since no layer w given name exists.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
  }


  /**
   * Tests the setFilterToLayers method in project class.
   */
  @Test
  public void testSetFilterToCertainLayers() {
    ILayer cLayer2 =
            new CollageLayer("second", cLayerPix, FilterOption.BLUE, 3, 2);
    ArrayList<ILayer> list = new ArrayList<>(Arrays.asList(this.cLayer, cLayer2));
    IProject testProject = new CollageProject("test project", list, 10, 10);

    // first layer is normal
    assertEquals("normal", testProject.returnAllLayers().get(0).getFilter().toString());

    testProject.setFilterToCertainLayers(FilterOption.RED, "first");
    // now after setting it to only "first" layer, first's is red, second is still blue
    assertEquals("red-component",
            testProject.returnAllLayers().get(0).getFilter().toString());

    assertEquals("blue-component",
            testProject.returnAllLayers().get(1).getFilter().toString());

    // now set "second" to multiply
    testProject.setFilterToCertainLayers(FilterOption.MULTIPLY, "second");

    // now test that it has changed to multiply
    assertEquals("multiply",
            testProject.returnAllLayers().get(1).getFilter().toString());
  }

  /**
   * Test for getName method in Project class that gets the name of the project.
   */
  @Test
  public void testGetName() {
    assertEquals("project1", project.getName());

    // create a new project with new name
    IProject projectTest = new CollageProject("Hello grader, how's your day been?", 10, 10);
    assertEquals("Hello grader, how's your day been?" ,projectTest.getName());

    // third test for get name.
    IProject test3 = new CollageProject("Another name", 10, 10);
    assertEquals("Another name" ,test3.getName());
  }
}
