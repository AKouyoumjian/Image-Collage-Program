package controller.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.FilterOption;
import model.IProject;
import view.IView;


/**
 * Class for set-filter command for the collager.
 */
public class SetFilterCmd implements ICommand {
  Scanner sc;
  IProject project;
  IView view;

  /**
   * Constructor for this method, it is public.
   * @param sc scanner
   * @param project model we want
   */
  public SetFilterCmd(Scanner sc, IProject project, IView view) {
    this.sc = sc;
    this.project = project;
    this.view = view;
  }

  /**
   * Method to set a filter, the given the layer name and filter option.
   */
  @Override
  public void execute() {

    String layerName = "";
    String filterOptionStr = ""; // this is a string version of filterOption
    FilterOption filterOption = FilterOption.NORM;
    boolean flag = true;

    while (flag) {

      List<String> inputs = new ArrayList<>();

      // get the 2 inputs from the scanner
      for (int a = 0; a < 2; a++) {
        if (sc.hasNext()) {
          inputs.add(sc.next());
        }
      }

      layerName = inputs.remove(0);
      filterOptionStr = inputs.remove(0);

      // this try block is for converting the string FilterOption to the enum
      // throws exception if no FilterOption w the name exists.
      try {
        filterOption = filterOption.fromString(filterOptionStr);
        flag = false;
      } catch (IllegalArgumentException e) {
        try {

          this.view.renderMessage("Invalid FilterOption, please retry entering " +
                  "the layer's name and FilterOption again.");
        } catch (IOException ex) {
          throw new IllegalStateException("IOE thrown.");
        }
      }
    }

    // this try block is for setting the filter,
    // will throw exception if no layer w the name exists
    try {
      this.project.setFilterToCertainLayers(filterOption, layerName);
      try {
        this.view.renderMessage("Filter applied to layer: " + layerName);
      } catch (IOException ex) {
        throw new IllegalStateException("IOE thrown.");
      }

    }
    catch (IllegalArgumentException e) {
      try {
        this.view.renderMessage("No layer with that name exists, please retry entering " +
                "the layer's name and FilterOption again.");
      } catch (IOException ex) {
        throw new IllegalStateException("IOE thrown.");
      }
    }
  }

}
