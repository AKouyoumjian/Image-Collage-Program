package controller.command;

import java.io.IOException;
import java.util.Scanner;

import model.IProject;
import view.IView;

/**
 * Class for add-layer command.
 */
public class AddLayerCmd implements ICommand {
  Scanner sc;
  IProject project;
  IView view;

  /**
   * Constructor for this method, it is public.
   * @param sc scanner
   * @param project model we want
   */
  public AddLayerCmd(Scanner sc, IProject project, IView view) {
    this.sc = sc;
    this.project = project;
    this.view = view;
  }

  /**
   * Method to add a layer given the layer name.
   */
  @Override
  public void execute() {

    while (sc.hasNext()) {
      String name = sc.next();
      try {
        project.addLayer(name);
        try {
          this.view.renderMessage("\nLayer created with name: " + name);
        }
        catch (IOException ex) {
          throw new IllegalStateException("IOE thrown.");

        }
        break;
      }
      catch (IllegalArgumentException e) {
        // if exception is caught, that means another layer with that name is found

        try {
          this.view.renderMessage("Layer already exists with this name, please enter new name.\n");
        }
        catch (IOException ex) {
          throw new IllegalStateException("IOE thrown.");

        }
      }
    }

  }
}
