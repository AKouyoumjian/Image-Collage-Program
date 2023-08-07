import org.junit.Test;

import java.io.IOException;

import model.IProject;
import view.CollageTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import view.IView;


/**
 * Class for Collage text view testing.
 */
public class CollageTextViewTest {
  IProject collage;
  Appendable dest;
  IView collageView;
  IView exceptionThrower;

  @Test
  public void testConstructor() {
    // checking if an exception is thrown when null is given as the model
    try {
      IView fail = new CollageTextView(null);
      fail("Failed to throw an IllegalArg exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    try {
      IView fail = new CollageTextView(null, dest);
      fail("Failed to throw an IllegalArg exception.");
    } catch (IllegalArgumentException e) {
      // do nothing
    }
    // checking that no error is thrown when a new view is constructed
    IView collageView1 = new CollageTextView(collage);
    IView collageView2 = new CollageTextView(collage, dest);
  }

  @Test
  public void testRenderMessage() {
    // testing that an IOException is thrown
    try {
      exceptionThrower.renderMessage("fail");
      fail("Did not catch thrown IOException.");
    } catch (IOException e) {
      // do nothing
    }
    // rendering a message, and seeing that it was correctly transmitted to the destination
    try {
      collageView.renderMessage("Test message.");
    } catch (IOException e) {
      // do nothing
    }
    assertEquals("Test message.\n", dest.toString());
  }
}
