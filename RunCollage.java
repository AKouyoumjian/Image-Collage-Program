package view;

import java.io.InputStreamReader;

import controller.CollageController;
import controller.ControllerImpl;
import model.CollageProject;
import model.IProject;



/**
 * SetGameProgram is the class for running the game through the method Main().
 */
public final class RunCollage {

  /**
   * Method, required to be named main, is used to run the game in the console.
   */
  public static void main(String[] args) {
    // model information like name and size does not matter since controller.
    IProject model = new CollageProject("name", 500, 500);
    IView view = new CollageTextView(model);
    CollageController controller =
            new ControllerImpl(model, view, new InputStreamReader(System.in));

    try {
      controller.start();
    } catch (IllegalStateException ise) {
      // do nothing if IllegalStateException happens.
    }

  }
}