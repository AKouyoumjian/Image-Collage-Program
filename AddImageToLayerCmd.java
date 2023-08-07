package controller.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.utilities.ImageUtil;
import controller.utilities.JpegAndPngUtil;
import model.IPixel;
import model.IProject;
import view.IView;

/**
 * Class for add-image-to-layer command.
 */
public class AddImageToLayerCmd implements ICommand {
  Scanner sc;
  IProject project;
  IView view;

  /**
   * Constructor for this method, it is public.
   *
   * @param sc      scanner
   * @param project model we want
   */
  public AddImageToLayerCmd(Scanner sc, IProject project, IView view) {
    this.sc = sc;
    this.project = project;
    this.view = view;
  }

  /**
   * Method adds an image to a layer, given layer and image name and x,y ints.
   */
  @Override
  public void execute() {

    String layerName = "";
    String imageName = "";
    int xPos = -1;
    int yPos = -1;

    boolean flag = true;

    while (flag) {

      List<String> inputs = new ArrayList<>();


      // take next 4 inputs and put into inputs list

      for (int a = 0; a < 4; a++) {
        if (sc.hasNext()) {
          inputs.add(sc.next());
        }
      }

      layerName = inputs.remove(0);
      imageName = inputs.remove(0);

      try {
        xPos = Integer.parseInt(inputs.remove(0));
        yPos = Integer.parseInt(inputs.remove(0));
        flag = false;
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


    // use util file to read given image and add
    List<List<IPixel>> img = null;


    // now cases for which image type

    // To get the file type (png vs jpeg vs ppm), first reverse the path
    String[] reversedPath = new StringBuilder(imageName).reverse().toString().split(".");

    // then take characters before period and reverse it back to just get the extension
    String extension = new StringBuilder(reversedPath[0]).reverse().toString();


    switch (extension) {
      case "ppm":
        try {
          img = ImageUtil.readPPM(imageName);
        } catch (IllegalArgumentException e) {
          // if IllegalArg, then there was no file w that name so we
          // restart method and ask user to enter new instruction.
          try {
            this.view.renderMessage("Invalid file path.\n");
            return;
          } catch (IOException ignore) {
            throw new IllegalStateException("IOException thrown.");
          }
        }

        // now add the image to the layer
        this.project.addLayerImg(layerName, img, xPos, yPos);
        try {
          this.view.renderMessage(
                  "\nImage added to the layer: " + layerName);
        } catch (IOException ignore) {
          throw new IllegalStateException("IOException thrown.");
        }

        break;

      case "png":
      case "jpeg":

        try {
          img = JpegAndPngUtil.readImage(imageName);
        }
        catch (IllegalArgumentException e) {
          // if IllegalArg, then there was no file w that name so we
          // restart method and ask user to enter new instruction.
          try {
            this.view.renderMessage("Invalid file path.\n");
            return;
          } catch (IOException ignore) {
            throw new IllegalStateException("IOException thrown.");
          }
        }

        // now add the image to the layer
        this.project.addLayerImg(layerName, img, xPos, yPos);
        try {
          this.view.renderMessage(
                  "\nImage added to the layer: " + layerName);
        } catch (IOException ignore) {
          throw new IllegalStateException("IOException thrown.");
        }

        break;

      default:
        // if it gets to here, render a message that the image extension type is not valid.
        try {
          view.renderMessage("Image extension type is not valid. Must be jpeg, png, or ppm.");
        } catch (IOException e) {
          throw new IllegalStateException("Could not transmit to the view.");
        }
    }
  }
}
