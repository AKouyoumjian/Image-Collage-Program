package controller.command;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import controller.FeaturesImpl;
import controller.utilities.JpegAndPngUtil;
import view.IView;

import model.ILayer;
import model.IProject;

/**
 * Class for save-image command for the collage maker.
 */
public class SaveImageCmd implements ICommand {
  Scanner sc;
  IProject project;
  IView view;

  /**
   * Constructor for this method, it is public.
   *
   * @param sc      scanner
   * @param project model we want
   */
  public SaveImageCmd(Scanner sc, IProject project, IView view) {
    this.sc = sc;
    this.project = project;
    this.view = view;
  }

  /**
   * Method to save an image given the file name.
   */
  @Override
  public void execute() {

    String path = "";

    // get the path from scanner
    if (sc.hasNext()) {
      path = sc.next();
    }

    // To get the file type (png vs jpeg vs ppm), first reverse the path
    String[] reversedPath = new StringBuilder(path).reverse().toString().split("\\.");

    // then take characters before period and reverse it back to just get the extension
    String extension = new StringBuilder(reversedPath[0]).reverse().toString();

    switch (extension) {
      case "ppm":
        // convert image to a single layer ppm to be saved.
        ILayer image = this.project.compressToImage("Project 1");
        String imageSave = "";
        try {
          imageSave = image.getPPM();
        } catch (IOException e) {
          return;
        }

        try {
          FileWriter fw = new FileWriter(path);

          fw.write(imageSave);

          fw.close();

          try {
            this.view.renderMessage(
                    "\n Image saved to path: " + path);
          } catch (IOException ignore) {
            throw new IllegalStateException("IOException thrown.");
          }

        } catch (IOException e) {
          throw new IllegalStateException("IO Exception thrown.");
        }
        break;

      case "png":
      case "jpeg":
        BufferedImage b = new FeaturesImpl(project).projDisplay();
        try {
          JpegAndPngUtil.saveImage(path, b);
        }
        catch (IllegalStateException e) {
          // if exception is caught, that means that there was an error when trying to write.
          try {
            this.view.renderMessage("Error occurred while trying to write image to path.");
          }
          catch (IOException ex) {
            throw new IllegalStateException("IO exception thrown.");
          }
        }
        try {
          this.view.renderMessage(
                  "\n Image saved to path: " + path);
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
