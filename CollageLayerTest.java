import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.CollageLayer;
import model.FilterOption;
import model.ILayer;
import model.IPixel;
import model.RGBPixel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class for methods in CollageLayer class.
 */
public class CollageLayerTest {

  IPixel topLeft;
  IPixel topRight;
  IPixel midLeft;
  IPixel midRight;
  IPixel botLeft;
  IPixel botRight;
  IPixel imgTopLeft;
  IPixel imgTopRight;
  IPixel imgBotLeft;
  IPixel imgBotRight;
  List<List<IPixel>> cLayerPix;
  List<List<IPixel>> badPix;
  List<List<IPixel>> imgPix;
  ILayer cLayer;
  List<IPixel> botRow;

  @Before
  public void init() {
    topLeft = new RGBPixel(250, 250, 250, 100);
    topRight = new RGBPixel(250, 250, 250, 50);
    midLeft = new RGBPixel(30, 100, 170, 100);
    midRight = new RGBPixel(170, 100, 30, 100);
    botLeft = new RGBPixel(250, 250, 0, 0);
    botRight = new RGBPixel(250, 0, 250, 100);
    imgTopLeft = new RGBPixel(255, 255, 255, 255);
    imgTopRight = new RGBPixel(100, 0, 0, 255);
    imgBotLeft = new RGBPixel(0, 100, 0, 255);
    imgBotRight = new RGBPixel(0, 0, 100, 255);
    cLayerPix = new ArrayList<>();
    badPix = new ArrayList<>();
    imgPix = new ArrayList<>();
    List<IPixel> topRow = new ArrayList<IPixel>();
    topRow.add(topLeft);
    topRow.add(topRight);
    List<IPixel> midRow = new ArrayList<IPixel>();
    midRow.add(midLeft);
    midRow.add(midRight);
    botRow = new ArrayList<IPixel>();
    botRow.add(botLeft);
    botRow.add(botRight);
    cLayerPix.add(topRow);
    cLayerPix.add(midRow);
    cLayerPix.add(botRow);
    List<IPixel> badTopRow = new ArrayList<>();
    badTopRow.add(topLeft);
    badPix.add(badTopRow);
    badPix.add(midRow);
    List<IPixel> imgTopRow = new ArrayList<>();
    imgTopRow.add(imgTopLeft);
    imgTopRow.add(imgTopRight);
    List<IPixel> imgBotRow = new ArrayList<>();
    imgBotRow.add(imgBotLeft);
    imgBotRow.add(imgBotRight);
    imgPix.add(imgTopRow);
    imgPix.add(imgBotRow);
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
  }

  @Test
  public void testConstructor() {
    // testing exception for null name
    try {
      cLayer = new CollageLayer(null, cLayerPix, 3, 2);
      fail("Failed to throw an exception for null input of name.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for null pixels
    try {
      cLayer = new CollageLayer("fail", null, FilterOption.NORM, 3, 2);
      fail("Failed to throw an exception for null input of pixels.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for width = 0
    try {
      cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 0);
      fail("Failed to throw an exception for input of zero for width.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for negative width
    try {
      cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, -3);
      fail("Failed to throw an exception for negative input of width.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for height = 0
    try {
      cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 0, 2);
      fail("Failed to throw an exception for height input = 0.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for negative height
    try {
      cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, -1, 2);
      fail("Failed to throw an exception for negative input of height.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for width being too large
    try {
      cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 1000);
      fail("Failed to throw an exception for width which is too large.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for width being too small
    try {
      cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 1);
      fail("Failed to throw an exception for width which is too small.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // throwing exception for when pixel's rows have no pixels
    List<List<IPixel>> badPix = new ArrayList<>();
    badPix.add(new ArrayList<>());
    badPix.add(new ArrayList<>());
    try {
      cLayer = new CollageLayer("first", badPix,
              FilterOption.NORM, 3, 2);
      fail("Failed to throw an exception for when rows of pixels are empty.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when empty list is given as pixels
    try {
      cLayer = new CollageLayer("first", new ArrayList<>(),
              FilterOption.NORM, 3, 2);
      fail("Failed to throw an exception for empty list as pixels.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when a single row of pixels doesn't have an amount matching the width
    try {
      cLayer = new CollageLayer("first", badPix, FilterOption.NORM, 3, 2);
      fail("Failed to throw an exception for a row of pixels which doesn't match the width");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    assertEquals("first", cLayer.getName());
    assertEquals(3, cLayer.getHeight());
    assertEquals(2, cLayer.getWidth());
  }

  @Test
  public void testGetName() {
    // testing that correct name is returned
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    assertEquals("first", cLayer.getName());
  }

  @Test
  public void testGetOriginalPixel() {
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    // testing exception for when row is negative
    try {
      cLayer.getOriginalPixel(-1, 1);
      fail("Failed to throw exception when row is negative.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when row is larger than the layer's height
    try {
      cLayer.getOriginalPixel(4, 1);
      fail("Failed to throw exception when the row is too high.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when column is negative
    try {
      cLayer.getOriginalPixel(2, -1);
      fail("Failed to throw exception when column is negative.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when column is larger than the layer's width
    try {
      cLayer.getOriginalPixel(2, 4);
      fail("Failed to throw exception when column is too high.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing that correct pixel is retrieved
    assertEquals(topLeft, cLayer.getOriginalPixel(0, 0));
    assertEquals(midLeft, cLayer.getOriginalPixel(1, 0));
    assertEquals(botRight, cLayer.getOriginalPixel(2, 1));
    // testing that the original pixel is being retrieved, and NOT a copy of it
    assertNotEquals(topLeft.copy(), cLayer.getOriginalPixel(0, 0));
  }

  @Test
  public void testGetPixel() {
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    // testing exception for when row is negative
    try {
      cLayer.getPixel(-1, 1);
      fail("Failed to throw exception when row is negative.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when row is larger than the height
    try {
      cLayer.getPixel(4, 1);
      fail("Failed to throw exception when the row is too high.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when column is negative
    try {
      cLayer.getPixel(2, -1);
      fail("Failed to throw exception when column is negative.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing exception for when column is larger than the width
    try {
      cLayer.getPixel(2, 4);
      fail("Failed to throw exception when column is too high.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // testing that correct pixel is retrieved
    assertEquals(topLeft.toString(), cLayer.getPixel(0, 0).toString());
    assertEquals(midLeft.toString(), cLayer.getPixel(1, 0).toString());
    assertEquals(botRight.toString(), cLayer.getPixel(2, 1).toString());
    // testing that the original pixel is being retrieved, and NOT a copy of it
    assertNotEquals(topLeft.copy(), cLayer.getPixel(0, 0));
  }

  @Test
  public void testGetHeight() {
    // testing that correct height is returned
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    assertEquals(3, cLayer.getHeight());
  }

  @Test
  public void testGetWidth() {
    // testing that correct width is returned
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    assertEquals(2, cLayer.getWidth());
  }

  @Test
  public void testApplyFilter() {
    cLayer = new CollageLayer("first", cLayerPix, FilterOption.NORM, 3, 2);
    // testing to see that every one of the pixels are effected by a filter
    // making sure that original pixels are unaltered, all unequal to the filtered copies
    cLayer.applyFilter(FilterOption.RED);
    boolean noMatch = true;
    for (int row = 0; row < cLayer.getHeight(); row++) {
      for (int col = 0; col < cLayer.getWidth(); col++) {
        IPixel filteredPix = cLayer.getPixel(row, col);
        IPixel origPix = cLayer.getOriginalPixel(row, col);
        if (filteredPix.toString().equals(origPix.toString())) {
          noMatch = false;
        }
      }
    }
    assertTrue(noMatch);
    // testing to see that original pixels and currently displayed pixels have same values after
    // the "normal" filter is applied and the red filter is reverted
    cLayer.applyFilter(FilterOption.NORM);
    noMatch = false;
    for (int row = 0; row < cLayer.getHeight(); row++) {
      for (int col = 0; col < cLayer.getWidth(); col++) {
        IPixel currentPix = cLayer.getPixel(row, col);
        IPixel origPix = cLayer.getOriginalPixel(row, col);
        if (!origPix.toString().equals(currentPix.toString())) {
          noMatch = true;
        }
      }
    }
    assertFalse(noMatch);
    // testing that exception is thrown when the filter does not exist
    try {
      cLayer.applyFilter(FilterOption.ERROR);
      fail("Failed to throw an exception for unsupported filter.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
  }

  @Test
  public void testGetFilter() {
    assertEquals(FilterOption.NORM, cLayer.getFilter());
    cLayer.applyFilter(FilterOption.RED);
    assertEquals(FilterOption.RED, cLayer.getFilter());
  }

  /**
   * Test for toString method for CollageLayer.
   */
  @Test
  public void testToString() {

    assertEquals("first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "30 100 170 100\n" +
            "170 100 30 100\n" +
            "250 250 0 0\n" +
            "250 0 250 100\n", cLayer.toString());

    ArrayList<IPixel> row = new ArrayList();
    row.add(topLeft);
    row.add(topRight);

    List<List<IPixel>> pixels2 = new ArrayList<>();

    pixels2.add(row);
    pixels2.add(row);
    pixels2.add(row);

    CollageLayer cLayer2 =
            new CollageLayer("first", pixels2, FilterOption.NORM, 3, 2);

    assertEquals("first normal\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n" +
            "250 250 250 100\n" +
            "250 250 250 50\n", cLayer2.toString());
  }

  /**
   * Test for getPixelArrayCopy method for CollageLayer.
   */
  @Test
  public void testGetPixelArrayCopy() {

    assertEquals("[[250 250 250 100\n" +
            ", 250 250 250 50\n" +
            "], [30 100 170 100\n" +
            ", 170 100 30 100\n" +
            "], [250 250 0 0\n" +
            ", 250 0 250 100\n" +
            "]]", cLayer.getPixelArrayCopy().toString()); // done using toString for convenience

    ArrayList<IPixel> topRow = new ArrayList<IPixel>();
    topRow.add(topLeft);
    topRow.add(topRight);

    // now test on another completely different layer.
    List<List<IPixel>> cLayerPix2 = new ArrayList<>();

    cLayerPix2.add(topRow);
    cLayerPix2.add(topRow);
    cLayerPix2.add(topRow);

    CollageLayer cLayer2 =
            new CollageLayer("first", cLayerPix2, FilterOption.NORM, 3, 2);

    assertEquals("[[250 250 250 100\n" +
            ", 250 250 250 50\n" +
            "], [250 250 250 100\n" +
            ", 250 250 250 50\n" +
            "], [250 250 250 100\n" +
            ", 250 250 250 50\n" +
            "]]", cLayer2.getPixelArrayCopy().toString());
  }

  @Test
  public void testAddImg() {
    // checking for an exception for null image
    try {
      cLayer.addImg(null, 0, 0);
      fail("Failed to throw exception for null image.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // checking for an exception for negative x
    try {
      cLayer.addImg(imgPix, -1, 0);
      fail("Failed to throw exception for negative coord.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // checking for an exception for y out-of-bounds
    try {
      cLayer.addImg(imgPix, 0, 5);
      fail("Failed to throw exception for out-of-bounds coord.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // checking for exception when img has no rows of pixels
    List<List<IPixel>> badImg = new ArrayList<>();

    // checking for exception when img has empty rows of pixels
    badImg.add(new ArrayList<>());
    // checking for exception when img has rows with different amounts of pixels
    badImg.get(0).add(new RGBPixel(100, 100, 100, 100));
    badImg.add(new ArrayList<>());
    badImg.get(1).add(new RGBPixel(100, 100, 100, 100));
    badImg.get(1).add(new RGBPixel(100, 100, 100, 100));
    // checking for exception when image is too big for the layer
    badImg.get(0).add(new RGBPixel(100, 100, 100, 100));
    List<List<IPixel>> onePix = new ArrayList<>();
    onePix.add(new ArrayList<>());
    onePix.get(0).add(new RGBPixel(100, 100, 100, 100));
    ILayer smallLayer = new CollageLayer("one", onePix, 1, 1);
    try {
      smallLayer.addImg(badImg, 0, 0);
      fail("Failed to throw an exception for when the image is too big for the layer.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // checking for exception when image does not fit at the specified coordinate
    try {
      cLayer.addImg(imgPix, 2, 1);
      fail("Failed to throw an exception for when image cannot be placed at a certain coord.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }


    cLayer.addImg(imgPix, 0, 0);

    // checking that non-overlapped pixels are unchanged
    assertEquals(cLayer.getPixel(2, 0).toString(), botLeft.toString());
    assertEquals(cLayer.getPixel(2, 1).toString(), botRight.toString());

    // testing that an added image has the layer's current filter applied to it before it is
    // merged onto the layer
    List<List<IPixel>> img = new ArrayList<>();
    img.add(new ArrayList<>());
    IPixel filteredImg = new RGBPixel(200, 200, 200, 200);
    img.get(0).add(filteredImg);
    IPixel filteredImgCopy = filteredImg.copy();
    filteredImgCopy.apply(FilterOption.BRIGHTI);
    List<List<IPixel>> layPix = new ArrayList<>();
    IPixel layerPix = new RGBPixel(50, 0, 160, 170);
    layPix.add(new ArrayList<>());
    layPix.get(0).add(layerPix);
    ILayer newLayer = new CollageLayer("first", layPix, 1, 1);
    newLayer.applyFilter(FilterOption.BRIGHTI);
    newLayer.addImg(img, 0, 0);
    layerPix.apply(FilterOption.BRIGHTI);
    IPixel expected = filteredImgCopy.merge(layerPix);
    assertEquals("151 118 225 236\n", expected.toString());

  }

  /**
   * This is a test for getPPM method in ILayer interface.
   */
  @Test
  public void testGetPPM() {
    try {
      assertEquals("P3\n" +
              "# first.ppm\n" +
              "2 3\n" +
              "255\n" +
              "250 250 250 250 250 250\n" +
              "30 100 170 170 100 30\n" +
              "250 250 0 250 0 250\n", this.cLayer.getPPM());

      List<List<IPixel>> listToAdd = new ArrayList<>(Arrays.asList(
              new ArrayList<IPixel>(Arrays.asList(topLeft, topLeft))));
      ILayer l2 = new CollageLayer("name", listToAdd, 1, 2);

      assertEquals("P3\n" +
              "# name.ppm\n" +
              "2 1\n" +
              "255\n" +
              "250 250 250 250 250 250\n", l2.getPPM());
    }
    catch (IOException e) {
      fail("Test threw IOException"); // fail test if IO is thrown
    }
  }


  /**
   * Test for set filter method.
   */
  @Test
  public void testSetFilter() {
    assertEquals("normal", cLayer.getFilter().toString());

    cLayer.setFilter(FilterOption.SCREEN);
    assertEquals("screen", cLayer.getFilter().toString());

    cLayer.setFilter(FilterOption.RED);
    assertEquals("red-component", cLayer.getFilter().toString());

    cLayer.setFilter(FilterOption.NORM);
    assertEquals("normal", cLayer.getFilter().toString());
  }

  /**
   * Test for set all pixelBelow in a layer.
   */
  @Test
  public void testSetAllBelowPixels() {
    // since this method is called in constructor of an ILayer, we will instantiate a new
    // collage layer and then test that all below pixels are set.

    ILayer clayerTest = new CollageLayer("clayer test", this.cLayerPix, 3, 2);

    clayerTest.setAllBelowPixels();

    // now test some pixels in the new layer to make sure their belowPixel is correct.
    assertEquals("30 100 170 100\n",
            clayerTest.getPixel(0, 0).getPixelBelow().toString());
    assertEquals("250 0 250 100\n",
            clayerTest.getPixel(1, 1).getPixelBelow().toString());
    assertEquals("250 250 0 0\n",
            clayerTest.getPixel(1, 0).getPixelBelow().toString());

    // test for no pixelBelow since this is a pixel at a bottom row
    assertEquals(null, clayerTest.getPixelArrayCopy().get(2).get(0).getPixelBelow());
  }

}