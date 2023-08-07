import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import controller.CollageController;
import controller.ControllerImpl;
import model.CollageProject;
import model.IProject;
import view.CollageTextView;
import view.IView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test for the controller's outputs to the view for the Collager Program.
 */
public class ControllerToViewTest {
  IProject model;
  Appendable append;
  IView view;
  Readable read;

  /**
   * Method to go before all tests initializing model, view, and readable.
   */
  @Before
  public void init() {
    model = new CollageProject("test", 3, 2);
    append = new StringBuilder();
    view = new CollageTextView(model, append);
    read = new StringReader("");
  }

  /**
   * Test for invalid constructor of Controller (checking for nulls).
   */
  @Test
  public void testInvalidConstructor() {
    read = new StringReader("q");

    try {
      CollageController cont1 = new ControllerImpl(null, view, read);
      fail("should have thrown IllegalArgumentException with null readable");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    try {
      CollageController cont2 = new ControllerImpl(model, null, read);
      fail("should have thrown IllegalArgumentException with null readable");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    try {
      CollageController cont3 = new ControllerImpl(model, view, null);
      fail("should have thrown IllegalArgumentException with null readable");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
  }


  /**
   * Test for start method for initial start message and quitting before user starts playing.
   */
  @Test
  public void testInitialRenderAndQuitMessage() {

    // other tests use lowercase q, quit, and Quit to quit, covering all cases.
    read = new StringReader("Q");
    CollageController controller = new ControllerImpl(model, view, read);

    controller.start();
    assertEquals("\n" +
                    "Welcome to the collage maker!\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Invalid instruction.\n" +
                    "\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Program quit. Goodbye.",
            append.toString());
  }


  /**
   * Test for start method when user tries to make a new project with invalid inputs,
   * then with  valid ones.
   */
  @Test
  public void testNewProject() {

    read = new StringReader("new-project 100 a 100 100 quit");
    CollageController controller = new ControllerImpl(model, view, read);

    controller.start();
    assertEquals("\n" +
                    "Welcome to the collage maker!\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "Invalid height or width input. Enter new height and then width.\n" +
                    "\n" +
                    "Project created\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Program quit. Goodbye.",
            append.toString());
  }



  /**
   * Test for start method when user tries to save a project and then load it.
   * Also test for save an image.
   */
  @Test
  public void testSaveAndLoadProjectAndImage() {

    read = new StringReader("new-project 100 100" +
            " save-project Documents/foodCollage.collage" +
            " load-project Documents/foodCollage.collage " +
            " save-image Documents/food.ppm Quit");
    CollageController controller = new ControllerImpl(model, view, read);

    controller.start();
    assertEquals("\n" +
                    "Welcome to the collage maker!\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Project created\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Layer created with name: Documents/foodCollage.collage\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "File Documents/foodCollage.collage is not found.\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Program quit. Goodbye.",
            append.toString());
  }

  /**
   * Test for start method when user tries to add a Layer and add an image to that layer
   * in a project, and to a wrongly named layer. Also tests adding multiple images to a layer.
   * Also tests the set-filter operation.
   */
  @Test
  public void testAddLayerAndAddImageToLayerAndSetFilter() {

    read = new StringReader("new-project 100 100" +
            " add-layer myLayer" +
            " add-image-to-layer myLayer Documents/tako.ppm 0 0" +
            " add-image-to-layer myLayer Documents/tako.ppm 40 37" +
            " add-image-to-layer wrongLayer Documents/tako.ppm 10 22" +
            " set-filter myLayer brighten-luma" +
            " set-filter myLayer darken-intensity Q");

    CollageController controller = new ControllerImpl(model, view, read);

    controller.start();

    // THIS TEST ONLY WORKS IF YOU HAVE TAKO.PPM IN DOCUMENTS.
    assertEquals("\n" +
                    "Welcome to the collage maker!\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Project created\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Layer created with name: myLayer\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "Image added to the layer: myLayer\n" +
                    "\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "Image added to the layer: myLayer\n" +
                    "\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "Invalid file path.\n" +
                    "\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "Filter applied to layer: myLayer\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "Filter applied to layer: myLayer\n" +
                    "Type instruction. Type q or quit to quit.",

            append.toString());
  }



  /**
   * Test for start method for when a user enters an invalid instruction.
   */
  @Test
  public void testInvalidInstruction() {

    read = new StringReader("badInstruction quit");
    CollageController controller = new ControllerImpl(model, view, read);

    controller.start();
    assertEquals("\n" +
                    "Welcome to the collage maker!\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Invalid instruction.\n" +
                    "\n" +
                    "Type instruction. Type q or quit to quit.\n" +
                    "\n" +
                    "\n" +
                    "Program quit. Goodbye.",
            append.toString());
  }










}