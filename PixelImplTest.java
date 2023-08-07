import org.junit.Before;
import org.junit.Test;


import model.FilterOption;
import model.HSLPixel;
import model.IPixel;
import model.RGBPixel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * Test class for methods in IPixel implementations.
 * This includes testing different pixel types like RGB and HSl.
 */
public class PixelImplTest {

  IPixel topLeft;
  IPixel topCent;
  IPixel topRight;
  IPixel midLeft;
  IPixel midCent;
  IPixel midRight;
  IPixel botLeft;
  IPixel botCent;
  IPixel botRight;

  IPixel testPix1;


  IPixel hsl1;
  IPixel hsl2;
  IPixel hsl3;
  IPixel hsl4;
  IPixel hsl5;

  IPixel hsl10;
  IPixel hsl11;
  IPixel hsl12;

  IPixel rgb1;
  IPixel rgb2;
  IPixel rgb3;

  @Before
  public void init() {
    topLeft = new RGBPixel(0, 0, 255, 255);
    topCent = new RGBPixel(0, 255, 255, 255);
    topRight = new RGBPixel(255, 255, 255, 100);
    midLeft = new RGBPixel(100, 100, 100, 20);
    midCent = new RGBPixel(100, 100, 100, 50);
    midRight = new RGBPixel(100, 100, 100, 80);
    botLeft = new RGBPixel(255, 170, 90, 0);
    botCent = new RGBPixel(90, 255, 170, 0);
    botRight = new RGBPixel(170, 90, 255, 0);

    testPix1 = new RGBPixel(170, 90, 155, 0);

    hsl5 = new HSLPixel(360, 0.2, 1, 255, 360, null);
    hsl4 = new HSLPixel(360, 0, 0, 255, 360, hsl5);
    hsl3 = new HSLPixel(100, 0.5, 1, 255, 360, hsl4);
    hsl2 = new HSLPixel(0, 1, 0.5, 255, 360, hsl3);
    hsl1 = new HSLPixel(222, 0.5, 0.2, 255, 360, hsl2);

    rgb1 = new RGBPixel(200, 100, 0, 255, 255, hsl1);
    rgb2 = new RGBPixel(100, 100, 200, 255, 255, rgb1);
    rgb3 = new RGBPixel(0, 20, 255, 40, 255, rgb2);

    hsl10 = new HSLPixel(100, .8, 0, 255, 360, rgb3);
    hsl11 = new HSLPixel(20, .1, 0, 255, 360, hsl10);
    hsl12 = new HSLPixel(218, 0.5, 0.9, 40, 360, hsl11);




  }



  /**
   * Test to make sure exception is thrown when IPixel is made with invalid constructor.
   */
  @Test
  public void testInvalidConstructor() {

    // tests for invalid constructor with RGB Pixel
    try {
      IPixel r1 = new RGBPixel(-1, 100, 100, 100);
      fail("should have thrown illegal argument exception.");
    }
    catch (IllegalArgumentException e) {
      // do nothing
    }

    try {
      IPixel r2 = new RGBPixel(100, 1000, 100, 100);
      fail("should have thrown illegal argument exception.");
    }
    catch (IllegalArgumentException e) {
      // do nothing
    }

    try {
      IPixel r3 = new RGBPixel(0, 100, 256, 100);
      fail("should have thrown illegal argument exception.");
    }
    catch (IllegalArgumentException e) {
      // do nothing
    }
    try {
      IPixel r4 = new RGBPixel(1, 100, 100, -255);
      fail("should have thrown illegal argument exception.");
    }
    catch (IllegalArgumentException e) {
      // do nothing
    }


    // now tests for invalid constructor with HSL pixel
    try {
      IPixel h1 = new HSLPixel(361, 0, 1);
      fail("should have thrown illegal argument exception.");
    }
    catch (IllegalArgumentException e) {
      // do nothing
    }

    try {
      IPixel h2 = new HSLPixel(300, -0.5, 0);
      fail("should have thrown illegal argument exception.");
    }
    catch (IllegalArgumentException e) {
      // do nothing
    }

    try {
      IPixel h3 = new HSLPixel(0, 0.0, 1.1);
      fail("should have thrown illegal argument exception.");
    }
    catch (IllegalArgumentException e) {
      // do nothing
    }


  }

  @org.junit.Test
  public void testToString() {
    assertEquals("0 0 255 255\n", topLeft.toString());
    assertEquals("170 90 255 0\n", botRight.toString());

    assertEquals("222 0.5 0.2\n", this.hsl1.toString());
    assertEquals("0 1.0 0.5\n", this.hsl2.toString());

  }

  @org.junit.Test
  public void testApply() {
    // testing that an exception is thrown when the given filter option does not exist
    try {
      topRight.apply(FilterOption.ERROR);
      fail("Failed to throw an exception for unsupported filter.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    topRight.apply(FilterOption.NORM);
    assertEquals("255 255 255 100\n", topRight.toString());
    topRight.apply(FilterOption.RED);
    assertEquals("255 0 0 100\n", topRight.toString());

    // test for HSL pixel with filter specific to RGB being applied
    hsl1.apply(FilterOption.RED);
    assertEquals("0 1.0 0.048828125\n", hsl1.toString());

    // test for new filter: multiply on hsl pixel with hsl below
    hsl2.apply(FilterOption.MULTIPLY);
    assertEquals("0 1.0 0.5\n", hsl2.toString());

    // test for multiply with hsl pixel with rgb below
    HSLPixel h1 = new HSLPixel(100, 1, .4);
    h1.setPixelBelow(new RGBPixel(200, 200, 1, 255));
    h1.apply(FilterOption.MULTIPLY);
    assertEquals("100 1.0 0.15703125\n", h1.toString());


    // test for multiply with rgb pixel
    RGBPixel r1 = new RGBPixel(200, 200, 200, 255);
    r1.setPixelBelow(rgb2);
    r1.apply(FilterOption.MULTIPLY);
    assertEquals("116 116 116 255\n", r1.toString());

    // test for multiply with rgb pixel with hsl below
    r1.setPixelBelow(new HSLPixel(100, 0, 1));
    r1.apply(FilterOption.MULTIPLY);
    assertEquals("115 115 115 255\n", r1.toString());



    // test for new filter: difference
    hsl3.apply(FilterOption.DIFFERENCE);
    assertEquals("0 0.0 0.99609375\n", hsl3.toString());

    // test for new filter: screen on hsl pixel
    hsl4.apply(FilterOption.SCREEN);
    assertEquals("360 0.0 1.0\n", hsl4.toString());

    // test for new filter: screen on rgb pixel
    rgb3.apply(FilterOption.SCREEN);
    assertEquals("149 157 255 40\n", rgb3.toString());

    // test for one of the new filter: difference called on a rgb pixels with a hsl pixel below.
    rgb1.apply(FilterOption.DIFFERENCE);
    assertEquals("176 100 0 255\n", rgb1.toString());

    // now test hsl pixel with rgb below
    hsl10.apply(FilterOption.DIFFERENCE);
    assertEquals("234 0.9636363636363636 0.78515625\n", hsl10.toString());

    // now test hsl pixel with hsl below
    hsl11.apply(FilterOption.DIFFERENCE);
    assertEquals("234 0.9464285714285714 0.78125\n", hsl11.toString());

    // now test rgb pixel with rgb below
    IPixel rgbForTest = new RGBPixel(111, 222, 2, 255);
    rgbForTest.setPixelBelow(rgb2);
    rgbForTest.apply(FilterOption.DIFFERENCE);
    assertEquals("11 122 198 255\n", rgbForTest.toString());


    // now test for a filter specific to hsl: Screen, applied to rgb with rgb below
    rgb2.apply(FilterOption.SCREEN);
    assertEquals("153 153 218 255\n", rgb2.toString());

    // now test for rgb with hsl below
    IPixel rgbForTest2 = new RGBPixel(111, 222, 2, 122);
    rgbForTest2.setPixelBelow(new HSLPixel(360, 0, 1));
    rgbForTest2.apply(FilterOption.SCREEN);
    assertEquals("255 255 255 122\n", rgbForTest2.toString());

    // now test for hsl with hsl below
    hsl12.apply(FilterOption.SCREEN);
    assertEquals("218 0.5 0.978125\n", hsl12.toString());

    // now test for hsl with rgb below
    IPixel hslForTest = new HSLPixel(300, .2, .2, 255, 360, hsl12);
    hslForTest.apply(FilterOption.SCREEN);
    assertEquals("300 0.2 0.9825\n", hslForTest.toString());


    // case for applying a filter that requires below pixel to a pixel that does not have a below
    IPixel hslNoBelow = new HSLPixel(100, .5, .33);
    IPixel rgbNoBelow = new RGBPixel(100, 1, 0, 150);
    IPixel otherRgbNoBelow = new RGBPixel(100, 44, 99, 25);

    hslNoBelow.setPixelBelow(null);
    rgbNoBelow.setPixelBelow(null);
    otherRgbNoBelow.setPixelBelow(null);

    // showing applying filter that does not require below pixel works
    hslNoBelow.apply(FilterOption.RED);
    assertEquals("0 1.0 0.13671875\n", hslNoBelow.toString());

    // now apply a filter and components remain unchanged
    hslNoBelow.apply(FilterOption.MULTIPLY);
    rgbNoBelow.apply(FilterOption.SCREEN);
    otherRgbNoBelow.apply(FilterOption.DIFFERENCE);
    // no null pointers are thrown

    assertEquals("0 1.0 0.13671875\n", hslNoBelow.toString());
    assertEquals("100 1 0 150\n", rgbNoBelow.toString());
    assertEquals("100 44 99 25\n", otherRgbNoBelow.toString());




  }

  @org.junit.Test
  public void testGetMaxValue() {
    assertEquals(255, topLeft.getMaxValue());
    assertEquals(255, topRight.getMaxValue());
    assertEquals(255, topCent.getMaxValue());
  }

  @org.junit.Test
  public void testCopy() {
    IPixel tlCopy = topLeft.copy();
    // checking that a separate object has been made
    assertNotEquals(topLeft, tlCopy);
    tlCopy.apply(FilterOption.RED);
    // checking that applying a filter to a copy alters it, but not the original
    assertEquals("0 0 0 255\n", tlCopy.toString());
    assertNotEquals("0 0 0 255\n", topLeft.toString());
  }

  @org.junit.Test
  public void testMerge() {
    assertEquals("217 217 217 112\n", topRight.merge(midLeft).toString());
    // testing that the new pixel values do not go above the max value
    IPixel max = new RGBPixel(255, 255, 255, 255);
    IPixel result2 = new RGBPixel(255, 255, 255, 255);
    assertEquals(result2.toString(), max.merge(max).toString());


    IPixel rgb1 = new RGBPixel(50, 50, 50, 30);
    IPixel rgb2 = new RGBPixel(200, 200, 0, 70);

    assertEquals("66 66 11 91\n", rgb1.merge(rgb2).toString());

    assertEquals("140 140 5 91\n", rgb2.merge(rgb1).toString());


    // tests for merging HSL pixels
    HSLPixel hsl1 = new HSLPixel(100, 0, 1);
    HSLPixel hsl2 = new HSLPixel(20, 0.3, 0.7);

    assertEquals("201 170 155 255\n", hsl1.merge(hsl2).toString());

    assertEquals("255 255 255 255\n", hsl2.merge(hsl1).toString());


  }


  /**
   * Test for get value method.
   */
  @org.junit.Test
  public void testGetValue() {
    assertEquals(255, botLeft.getValue(), .01);
    assertEquals(100, midCent.getValue(), .01);
    assertEquals(170, testPix1.getValue(), .01);
  }


  /**
   * Test for get luma method.
   */
  @org.junit.Test
  public void testGetLuma() {
    assertEquals(182.3, botLeft.getLuma(), .01);
    assertEquals(100, midCent.getLuma(), .01);
    assertEquals(111.7, testPix1.getLuma(), .01);
  }

  /**
   * Test for get intensity method.
   */
  @Test
  public void testGetIntensity() {
    assertEquals(171, botLeft.getIntensity(), .01);
    assertEquals(100, midCent.getIntensity(), .01);
    assertEquals(138, testPix1.getIntensity(), .01);
  }


  /**
   * Test for method in IPixel: isRGB.
   */
  @Test
  public void testIsRGB() {
    assertEquals(true, this.rgb1.isRGB());
    assertEquals(true, this.rgb2.isRGB());
    assertEquals(false, this.hsl1.isRGB());
    assertEquals(false, this.hsl2.isRGB());
  }

  /**
   * Test for method in IPixel: isHSL.
   */
  @Test
  public void testIsHSL() {
    assertEquals(false, this.rgb1.isHSL());
    assertEquals(false, this.rgb2.isHSL());
    assertEquals(true, this.hsl1.isHSL());
    assertEquals(true, this.hsl2.isHSL());
  }


  /**
   * Test for method in IPixel: getPixelBelow.
   */
  @Test
  public void testGetPixelBelow() {
    assertEquals("222 0.5 0.2\n", this.rgb1.getPixelBelow().toString());
    assertEquals("200 100 0 255\n", this.rgb2.getPixelBelow().toString());
    assertEquals("360 0.0 0.0\n", this.hsl3.getPixelBelow().toString());

  }

  /**
   * Test for method in IPixel: setPixelBelow.
   */
  @Test
  public void testSetPixelBelow() {
    // test with hsl pixel
    IPixel hslExample = new HSLPixel(100, 0, 0);
    // the pixel below is set to null after instantiation.
    assertEquals(null, hslExample.getPixelBelow());

    // now we set the pixel below using the method we are testing
    hslExample.setPixelBelow(new HSLPixel(10, 1, 1));
    assertEquals("10 1.0 1.0\n", hslExample.getPixelBelow().toString());


    // test with rgb pixel
    IPixel rgbExample = new HSLPixel(100, 0, 0);
    // the pixel below is set to null after instantiation.
    assertEquals(null, rgbExample.getPixelBelow());

    // now we set the pixel below using the method we are testing
    rgbExample.setPixelBelow(new HSLPixel(101, 0.0, 0.1));
    assertEquals("101 0.0 0.1\n", rgbExample.getPixelBelow().toString());

  }










}