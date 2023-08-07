import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import controller.CollageController;
import controller.ControllerImpl;
import controller.Features;
import controller.FeaturesImpl;
import model.CollageProject;
import model.IProject;
import view.CollageTextView;
import view.IView;

/**
 * This class contains the main method to run the Collager. It can be run in text mode
 * by using the command line argument -text or using a script by -file filePath.
 * It can also run in GUI mode by default (no command line arguments).
 */
public class ProjectUI {
  /**
   * This is the main method which runs the Collager in the command-line specified manner.
   * It will create an empty collager and allow the user to edit in text based or gui mode.
   * @param args arguments in command line
   */
  public static void main(String[] args) {
    // place-holder model. user will have to input height and width in actual implementation
    IProject model = new CollageProject("example", 700, 1200);


    // case for if arg[0], since taking arg[0] results in index out of bounds, we check length
    if (args.length == 0) {
      Features controller = new FeaturesImpl(model);
    } else {
      switch (args[0]) {

        case "-text": {
          IView view = new CollageTextView(model); // make the view be text mode.
          CollageController controller =
                  new ControllerImpl(model, view, new InputStreamReader(System.in));
          try {
            controller.start(); // start the program
          } catch (IllegalStateException ex) {
            break;
          }
          break;
        }

        case "-file": {
          if (args.length != 2) {
            System.out.println("Invalid number of inputs after -file");
            break;
          } else {
            try {
              Readable reader = new FileReader(args[1]);
              IView view = new CollageTextView(model); // make the view be text mode.
              // now make the controller with the Readable given after -file
              CollageController controller = new ControllerImpl(model, view, reader);
            } catch (FileNotFoundException e) {
              System.out.println("File path does not exist.");
              break;
            }
          }
        }
        break;

        default: {
          // this case is for when the command line input is not recognized.
          System.out.println("Command line input is not recognized.");
        }
      }
    }
  }

}

