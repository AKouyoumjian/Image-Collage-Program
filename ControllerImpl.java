package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import controller.command.AddImageToLayerCmd;
import controller.command.AddLayerCmd;
import controller.command.ICommand;
import controller.command.SaveImageCmd;
import controller.command.SaveProjectCmd;
import controller.command.SetFilterCmd;
import model.CollageLayer;
import model.ILayer;
import model.IPixel;
import model.IProject;
import model.RGBPixel;
import view.IView;

/**
 * Controller for Collage Maker which takes in inputs and delegates to view and model.
 */
public class ControllerImpl implements CollageController {
  private IProject model;
  private IView view;
  private Readable input;

  /**
   * Constructor for controller, in which the model, view, and input are provided.
   *
   * @param model model
   * @param view  view
   * @param input a readable
   */
  public ControllerImpl(IProject model, IView view, Readable input) {
    this.model = model;
    this.view = view;
    this.input = input;
    if (this.model == null || this.view == null || this.input == null) {
      throw new IllegalArgumentException("Model, view, and readable input cannot be null");
    }
  }


  /**
   * Controller method to start the controller.
   */
  public void start() {
    Scanner sc = new Scanner(input);
    boolean quit = false;

    // prints message for start of collage maker
    this.welcomeMessage();

    //continue until the user quits
    while (!quit && sc.hasNext()) {

      // takes a command instruction from user
      String instruction = sc.next();

      if (instruction.equals("quit") || instruction.equals("q")) {
        quit = true;
        break;
      } else {
        processCommand(instruction, sc);
      }
      try {
        this.view.renderMessage("Type instruction. Type q or quit to quit.\n");
      } catch (IOException e) {
        throw new IllegalStateException("Controller has run out of inputs");
      }
    }

    // while loop is exited since quit is true, so we print ending message.
    this.endMessage();


  }



  /**
   * Takes a single user command and delegates its action.
   *
   * @param instruction instruction from user
   * @param sc          scanner
   */
  public void processCommand(String instruction, Scanner sc) {
    ICommand command = null; // null placeholder, will be changed to Command we want to execute.

    switch (instruction) {

      // *** The new-project command is in the processCommand method and not a new class
      // because it needs to mutate the project field of the controller.
      case "new-project":
        int height = 0;
        int width = 0;
        boolean flag = true;

        while (flag) {
          List<String> inputs = new ArrayList<>();

          // take next 4 inputs and put into inputs list
          for (int a = 0; a < 2; a++) {
            if (sc.hasNext()) {
              inputs.add(sc.next());
            }
          }
          try {
            height = Integer.parseInt(inputs.remove(0));
            width = Integer.parseInt(inputs.remove(0));
            flag = false;
          } catch (NumberFormatException e) {
            // if caught, this means there is no integer in the string.
            // So we catch and ask to try again and restart the loop
            try {
              this.view.renderMessage(
                      "Invalid height or width input. Enter new height and then width.");
            } catch (IOException ignore) {
              throw new IllegalStateException("IOException thrown.");
            }
          }
        }

        this.model.startProject("untitled", height, width);

        try {
          this.view.renderMessage("\nProject created");
        } catch (IOException e) {
          throw new IllegalStateException("IOE thrown");
        }
        break;

      // did not send to a new class because we need to mutate the model field in the controller
      // making it the newly loaded project.
      case "load-project":
        boolean lpFlag = true; // flag for while loop to get the file path
        boolean lpFlag2 = true;// flag for while loop to get the name, height, and width of project.
        Scanner sc2 = null; // set to null now, will either be set to path from user input.
        String lpName = "";
        int lpHeight = 0;
        int lpWidth = 0;
        int lpMaxValue = 0;

        // loads in the formatProject format.
        String lpPath = "";

        // gets the file path
        while (lpFlag) {
          if (sc.hasNext()) {
            lpPath = sc.next();
          }

          try {
            sc2 = new Scanner(new FileInputStream(lpPath));
            lpFlag = false;
          } catch (FileNotFoundException e) {
            try {
              this.view.renderMessage("File " + lpPath + " is not found.");
              break; // leave while loop without setting lpFlag to true
            } catch (IOException ignore) {
              throw new IllegalStateException("IOException thrown.");
            }
          }
        }

        // this condition is true when the file path is invalid, so we break out of this command
        // and user will be prompted (done outside this command) to enter an instruction again.
        if (lpFlag) {
          break;
        }


        // now we get the name, height, and width
        List<String> inputs = new ArrayList<>();

        while (lpFlag2) {
          for (int a = 0; a < 4; a++) {
            if (sc2.hasNext()) {
              inputs.add(sc2.next());
            }
          }


          lpName = inputs.remove(0);


          try {

            lpWidth = Integer.parseInt(inputs.remove(0));
            lpHeight = Integer.parseInt(inputs.remove(0));
            lpMaxValue = Integer.parseInt(inputs.remove(0));
            lpFlag2 = false;
          } catch (NumberFormatException e) {
            // if caught, this means there is no integer in the string.
            // So we catch and ask to try again and restart the loop
            try {
              this.view.renderMessage(
                      "Invalid x or y position input. Start over enter layer name, " +
                              "image name, xPos, and then yPos.\n");
            } catch (IOException ignore) {
              throw new IllegalStateException("IOException thrown.");
            }
          }
        }


        // now the rest of the string in the scanner is
        // layerName, formatName, followed by Layer-content-format, repeated.


        ArrayList<ILayer> listOfLayers = new ArrayList<>();

        ILayer layer = null;

        IPixel[][] lpPixels = new RGBPixel[lpHeight][lpWidth];


        // this while loop will go until there are nothing in the read file.
        // it will form pixels from the rgb values, then form layers, then add these layers
        // to a list to be used in constructing the model (model = loaded project).
        while (sc2.hasNext()) {

          // getting all the pixels from one image into a list
          for (int i = 0; i < lpHeight; i++) {
            for (int j = 0; j < lpWidth; j++) {
              int r = sc2.nextInt();
              int g = sc2.nextInt();
              int b = sc2.nextInt();

              // makes pixel with these values then adds it to array
              IPixel pixCurrent = new RGBPixel(r, g, b, lpMaxValue);
              lpPixels[i][j] = pixCurrent;
            }
          }

          List<List<IPixel>> list = new ArrayList<>();

          // loop to turn lpPixels[][] into a list of list of IPixel.
          for (IPixel[] row : lpPixels) {
            ArrayList<IPixel> rowList = new ArrayList<IPixel>();

            rowList.addAll(Arrays.asList(row));

            list.add(rowList);
          }

          // makes a layer from the pixels.
          layer = new CollageLayer(lpName, list, lpHeight, lpWidth);

          // adds this layer to the list
          listOfLayers.add(layer);
        }
        this.model.startProject(lpName, listOfLayers, lpHeight, lpWidth);
        try {
          this.view.renderMessage(
                  "\n Project loaded: " + lpName + "\n");
        } catch (IOException ignore) {
          throw new IllegalStateException("IOException thrown.");
        }
        break;

      case "save-project":
        command = new SaveProjectCmd(sc, this.model, this.view);
        break;
      case "add-layer":
        command = new AddLayerCmd(sc, this.model, this.view);
        break;
      case "add-image-to-layer":
        command = new AddImageToLayerCmd(sc, this.model, this.view);
        break;
      case "set-filter":
        command = new SetFilterCmd(sc, this.model, this.view);
        break;
      case "save-image":
        command = new SaveImageCmd(sc, this.model, this.view);
        break;
      default:
        // if it gets to here, instruction is invalid, so we restart the while loop.
        try {
          this.view.renderMessage(
                  "\nInvalid instruction.\n");
        } catch (IOException ignore) {
          throw new IllegalStateException("IOException thrown.");
        }
        return;
    }


    // commands save-image, save-project, and set-filter "are never used"???




    // to prevent a null pointer, we make sure command is not left as null before executing.
    if (command != null) {
      command.execute();
    }
  }

  /**
   * This method renders a welcome message to the view.
   */
  private void welcomeMessage() throws IllegalStateException {
    try {
      view.renderMessage("Welcome to the collage maker!" +
              "\nType instruction. Type q or quit to quit.\n");
    } catch (IOException e) {
      throw new IllegalStateException("IOException thrown.");
    }
  }

  /**
   * This method renders an end/goodbye message to the view.
   */
  private void endMessage() throws IllegalStateException {
    try {
      view.renderMessage("\nProgram quit. Goodbye.");
    } catch (IOException e) {
      throw new IllegalStateException("IOException thrown.");
    }
  }
}