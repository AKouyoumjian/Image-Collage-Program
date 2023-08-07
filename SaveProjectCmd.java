package controller.command;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import model.IProject;
import view.IView;


/**
 * Class for the command, save-project, for the collager.
 */
public class SaveProjectCmd implements ICommand {
  Scanner sc;
  IProject project;
  IView view;

  /**
   * Constructor for this method, it is public.
   *
   * @param sc      scanner
   * @param project model we want
   */
  public SaveProjectCmd(Scanner sc, IProject project, IView view) {
    this.sc = sc;
    this.project = project;
    this.view = view;
  }

  /**
   * Method to save a project. If a path does not exist it will make a new one,
   * else overwrite existing.
   */
  @Override
  public void execute() {
    String path = "";

    if (sc.hasNext()) {
      path = sc.next();
    }

    try {

      FileWriter fw = new FileWriter(path);

      fw.write(project.formatProject());
      fw.close();


      this.view.renderMessage("\nFile saved successfully to " + path);

    } catch (IOException e) {
      throw new IllegalStateException("IO Exception thrown.");
    }

  }

}
