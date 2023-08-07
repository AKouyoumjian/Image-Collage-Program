import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import controller.CollageController;
import controller.ControllerImpl;
import model.CollageLayer;
import model.ILayer;
import model.IPixel;
import model.IProject;
import model.RGBPixel;
import view.CollageTextView;
import view.IView;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing with the CollageProjectMock.
 */
public class CollageProjectMockTest {

  IProject collage;
  ILayer cLayer;
  List<List<IPixel>> layerPix;
  IPixel topLeft;
  IPixel topRight;
  IPixel botLeft;
  IPixel botRight;
  List<List<IPixel>> img;
  IPixel imgLeft;
  IPixel imgRight;
  IView view;
  StringBuilder log;
  Readable read;
  CollageController controller;





  @Before
  public void init() {
    topLeft = new RGBPixel(100, 100, 100, 100);
    topRight = new RGBPixel(50, 100, 150, 200);
    botLeft = new RGBPixel(0, 10, 255, 30);
    botRight = new RGBPixel(40, 50, 60, 70);
    layerPix = new ArrayList<>();
    layerPix.add(new ArrayList<>());
    layerPix.add(new ArrayList<>());
    layerPix.get(0).add(topLeft);
    layerPix.get(0).add(topRight);
    layerPix.get(1).add(botLeft);
    layerPix.get(1).add(botRight);
    imgLeft = new RGBPixel(255, 250, 245, 240);
    imgRight = new RGBPixel(40, 160, 70, 100);
    img = new ArrayList<>();
    img.add(new ArrayList<>());
    img.get(0).add(imgLeft);
    img.get(0).add(imgRight);
    this.log = new StringBuilder();
    cLayer = new CollageLayer("layer", layerPix, 2, 2);
    collage = new CollageProjectMock("mock", log);
    view = new CollageTextView(collage, new StringBuilder());
    this.read = new StringReader("");

  }

  /**
   * Testing that new-project's called method, makeBackgroundLayer, is given the right
   * arguments by the controller.
   */
  @Test
  public void testMakeBackgroundLayer() {
    // read is a script that is set up to get makeBackgroundLayer to be called by controller.
    read = new StringReader("new-project 100 100 q");
    this.controller = new ControllerImpl(this.collage, this.view, this.read);
    this.controller.start();

    assertEquals("height: 2 width: 2\n", this.log.toString());
  }

  /**
   * Testing that addLayer receives the correct arguments from the controller.
   */
  @Test
  public void testAddLayer() {
    read = new StringReader("new-project 100 100 add-layer layerName q");
    CollageController controller = new ControllerImpl(this.collage, this.view, this.read);
    assertEquals("height: 2 width: 2\n name: layerName", this.log.toString());
  }

  /**
   * Testing that addLayerImg receives the correct arguments from the controller.
   */
  @Test
  public void testAddLayerImg() {
    read = new StringReader("new-project 100 100 q" +
            "add-image-to-layer background documents/tako.ppm 0 0 q");
    CollageController controller = new ControllerImpl(this.collage, this.view, this.read);
    assertEquals("height: 2 width: 2 " +
            "layerName: background img: documents/take.ppm x: 0 y: 0\n", this.log.toString());
  }

  /**
   * Testing that addLayerImg receives the correct arguments from the controller.
   */
  @Test
  public void testCompressToImage() {
    read = new StringReader("new-project 100 100 " +
            "save-project q");
    CollageController controller = new ControllerImpl(this.collage, this.view, this.read);
    assertEquals("height: 2 width: 2 name: untitled", this.log.toString());
  }

  /**
   * Testing that addLayerImg receives the correct arguments from the controller.
   */
  @Test
  public void testApplyFilterToCertainLayer() {
    read = new StringReader("new-project 100 100 set-filter background red-component q");
    CollageController controller = new ControllerImpl(this.collage, this.view, this.read);
    assertEquals("height: 2 width: 2 FilterOption: red-component LayerName: background",
            this.log.toString());
  }
}