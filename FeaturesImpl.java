package controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.command.AddLayerCmd;
import controller.command.ICommand;
import controller.command.SaveImageCmd;
import controller.command.SaveProjectCmd;
import controller.command.SetFilterCmd;
import controller.utilities.ImageUtil;
import model.ILayer;
import model.IPixel;
import model.IProject;
import model.RGBPixel;
import view.IView;
import view.JFrameProjectView;

/**
 * This is the implementation of the Features controller for the Graphical User Interface.
 * It interacts with the view and the model to provide the user with a collager.
 * We use a delegate (composition) for code reuse.
 */
public class FeaturesImpl implements Features {
  private IProject model;
  private final IView view;
  private ICommand delegate;
  private int loi;


  /**
   * Constructor for FeaturesImpl with model and view.
   *
   * @param model IProject model
   */
  public FeaturesImpl(IProject model) {
    this.model = model;
    this.view = new JFrameProjectView(this);
    // loi is initialized to be the background layer
    this.loi = 0;
    // ppm is the default image type when saving the image of a project
  }

  @Override
  public void createProj(String name, int height, int width) {
    this.model.startProject(name, height, width);
  }

  @Override
  public void saveProj(String foldPath) {
    // initialize the input for the delegate
    Readable input = new StringReader(foldPath + "/" + this.model.getName() + ".collage");
    // create the delegate with the input
    this.delegate = new SaveProjectCmd(new Scanner(input), this.model, this.view);
    // execute the delegate to save the project at the specified location
    try {
      this.delegate.execute();
    } catch (IllegalStateException e) {
      try {
        this.view.renderMessage("IOException occurred when saving the project. Please try again.");
      } catch (IOException e1) {
        throw new IllegalStateException("Could not transmit to the view.");
      }
    }
  }

  @Override
  public void saveImg(String foldPath) {

    // create the delegate with the input
    this.delegate = new SaveImageCmd(new Scanner(foldPath), this.model, this.view);
    // execute the delegate to save the image at the specified location
    try {
      this.delegate.execute();
    } catch (IllegalStateException e) {
      try {
        this.view.renderMessage("IOException occurred when saving the project image. " +
                "Please try again.");
      } catch (IOException e1) {
        throw new IllegalStateException("Could not transmit to the view.");
      }
    }

  }

  @Override
  public void loadProj(File project) throws IOException {
    // need to read this file, and to create a new model based on its text
    Scanner scan = null;
    try {
      scan = new Scanner(project);
    } catch (FileNotFoundException e) {
      try {
        this.view.renderMessage("Provided file not found.");
      } catch (IOException e1) {
        this.view.renderMessage("Could not transmit to the view.");
      }
    }
    // initiating the project's name, along with its height and width
    String projName = scan.nextLine();
    int height = scan.nextInt();
    int width = scan.nextInt();
    int maxVal = scan.nextInt();
    this.model.startProject(projName, height, width);
    // now, looping through the layers that the project has
    while (scan.hasNext()) {
      String layerName = scan.next();
      String filt = scan.next();
      List<List<IPixel>> layerPix = new ArrayList<>();
      // while the scanner still has a pixel to process
      while (scan.hasNextInt()) {
        for (int heightIndex = 0; heightIndex < height; heightIndex++) {
          List<IPixel> row = new ArrayList<>();
          for (int widthIndex = 0; widthIndex < width; widthIndex++) {
            int r = scan.nextInt();
            int g = scan.nextInt();
            int b = scan.nextInt();
            int a = scan.nextInt();
            IPixel newPixel = new RGBPixel(r, g, b, a);
            row.add(newPixel);
          }
          layerPix.add(row);
        }
      }
      if (!layerName.equals("background")) {
        try {
          this.model.addLayer(layerName);
        } catch (IllegalArgumentException e2) {
          this.view.renderMessage("Could not add a layer with that name. Please try again.");
        }
      }
      this.model.addLayerImg(layerName, layerPix, 0, 0);

      // creating inputs to apply the filter to the layer
      Readable input = new StringReader(filt + " " + layerName);
      // initializing the delegate to apply the filter to the given layer
      this.delegate = new SetFilterCmd(new Scanner(input), this.model, this.view);
    }
    scan.close();
  }

  @Override
  public List<ILayer> getLayers() {
    return this.model.returnAllLayers();
  }

  @Override
  public void setLoi(int indexLoi) throws IllegalArgumentException {
    if (indexLoi < 0 || indexLoi > this.getLayers().size()) {
      try {
        this.view.renderMessage("Cannot set the loi to an invalid value.");
      } catch (IOException e) {
        throw new IllegalStateException("Could not transmit to the view.");
      }
    }
    this.loi = indexLoi;
  }

  @Override
  public int getLoi() {
    return this.loi;
  }

  @Override
  public void loiFilter(String fName) throws IOException {
    // creating a new input for the delegate
    Readable input = new StringReader(this.getLayers().get(this.loi).getName() + " " + fName);
    // initializing the delegate command
    this.delegate = new SetFilterCmd(new Scanner(input), this.model, this.view);
    // executing the delegate to set the filter to the given layer
    this.delegate.execute();
  }

  @Override
  public void createLayer(String name) throws IllegalArgumentException {
    // creating a new input for the delegate
    Readable input = new StringReader(name);
    // initializing the delegate command
    this.delegate = new AddLayerCmd(new Scanner(input), this.model, this.view);
    // executing the delegate to add a layer with the given name
    this.delegate.execute();
  }

  @Override
  public BufferedImage projDisplay() {
    ILayer finalLayer = this.model.compressToImage("current view");
    int width = finalLayer.getWidth();
    int height = finalLayer.getHeight();
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // looping through the newly created image and setting each of its pixel values equal to
    // the corresponding pixel
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        String pixel = finalLayer.getPixel(row, col).toString();
        String[] pixVals = pixel.split(" ");
        int r = Integer.parseInt(pixVals[0]);
        int g = Integer.parseInt(pixVals[1]);
        int b = Integer.parseInt(pixVals[2]);
        Color c = new Color(r, g, b);
        img.setRGB(col, row, c.getRGB());
      }
    }
    return img;
  }

  @Override
  public void addLoiImage(File ppm, int x, int y) {
    List<List<IPixel>> pix = ImageUtil.readPPM(ppm.getPath());
    try {
      this.model.addLayerImg(this.getLayers().get(this.loi).getName(), pix, x, y);
    } catch (IllegalArgumentException e) {
      try {
        this.view.renderMessage("Invalid inputs, please try again.");
      } catch (IOException e1) {
        throw new IllegalStateException("Could not transmit to the view.");
      }
    }
  }

  @Override
  public String getProjName() {
    return this.model.getName();
  }
}