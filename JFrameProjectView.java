package view;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPopupMenu;
import javax.swing.BoxLayout;


import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import controller.Features;
import model.ILayer;


/**
 * This is the implementation of the view for the Graphical User Interface.
 * It utlizes the JFrame library through extension (inheritance), and
 * implements ActionListener and IView. The IView is for the Collager, and
 * the ActionListener is for responding to inputs the user makes to the GUI.
 */
public class JFrameProjectView extends JFrame implements ActionListener, IView {

  // the controller for this GUI. communicates with the model depending on the actions
  // taken by the user
  private final Features controller;

  // the main panel on which all the other panels will be displayed
  private final JPanel mainPanel = new JPanel();

  // the dialog panel which will display various options to the user
  private final JPanel dialogPanel = new JPanel();

  // the buttons which will allow user the user to perform certain actions on the project
  private final JPanel buttonsPanel = new JPanel();

  // the view for the project panel to scroll through. the image of the project will be added
  // to this panel
  private final JPanel projView = new JPanel();
  // the panel which displays the collage that the user is working on
  private final JScrollPane projectPanel = new JScrollPane(projView);

  // the view for the layersPanel to scroll through
  private final JPanel layersView = new JPanel();
  // the panel which displays all the project's layers. the user will be able to scroll through the
  // layers as JButtons, which will each have the layer's name, and will be altered if the user
  // selects the button to indicate that the corresponding layer is the loi (layer-of-interest)
  private final JScrollPane layersPanel = new JScrollPane(layersView);

  // the panel which displays when the gui starts-up
  private final JPanel startUpPanel = new JPanel();

  // the panel which will be added to the start-up panel when the user wants to create
  // a new project
  private final JPanel createProj = new JPanel();
  // the panels for entries of the new project name, width, and height
  private final JPanel namePanel = new JPanel();
  private final JPanel widthPanel = new JPanel();
  private final JPanel heightPanel = new JPanel();
  private final JTextComponent nameEntry = new JTextField(10);
  private final JTextComponent heightEntry = new JTextField(10);
  private final JTextComponent widthEntry = new JTextField(10);
  // the button for when the user is submitting their inputs for a new project
  private final JButton submit = new JButton("Submit");

  // a list of the buttons representing different layers for the user to select
  private List<JComponent> layerButtons = new ArrayList<>();

  // the file chooser which allows the user
  // to select an image to place on the loi (layer-of-interest)
  private final JFileChooser imgMenu = new JFileChooser();
  // components allowing the user to enter a coordinate to place the selected image
  private final JTextComponent xCoord = new JTextField(5);
  private final JTextComponent yCoord = new JTextField(5);

  // the field which allows the user to input a name for a new layer
  private final JTextComponent layerNameEntry = new JTextField(10);

  // the file which is selected by the user, in the various instances whenever the user is loading
  // a project or image
  private File selected = null;

  /**
   * Constructor for JFrameProjectView that has no arguments
   * and uses the JFrame's inherited super constructor.
   */
  public JFrameProjectView(Features controller) {
    this.controller = controller;

    // setting the size of this frame
    this.setSize(1200, 700);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setVisible(true);

    // the controller is initialized with a dummy model. this model will be changed to either
    // the new project created by the user, or the project which they load

    this.controller.setLoi(0);

    // creating the main panel and setting its layout. it is set so that the panels will be
    // displayed next to each other
    this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
    this.mainPanel.setSize(400, 400);
    this.mainPanel.setVisible(true);
    this.add(mainPanel);

    // creating the start-up panel and adding it to the main panel
    this.startUpHelper();
    this.mainPanel.add(this.startUpPanel);

    // creating the panel which will have the various buttons for user features. layout is set
    // to display the buttons on top of each other
    this.initialButtonsHelper();
    mainPanel.add(this.buttonsPanel);

    // creating the project view and scrollable project panel
    this.initialProjHelper();
    this.mainPanel.add(this.projectPanel);

    // creating the panel which will display all the projects' layers. will display the layers
    // in a vertical format
    this.initialLayersHelper();
    this.mainPanel.add(this.layersPanel);

    this.createProj.add(new JLabel("Enter project's name and dimensions"));
    this.namePanel.add(new JLabel("Project Name:"));
    this.heightPanel.add(new JLabel("Height:"));
    this.widthPanel.add(new JLabel("Width:"));
    this.submit.setActionCommand("SUBMIT");
    this.submit.addActionListener(this);

    this.pack();
  }

  /**
   * A helper for initializing the layers panel.
   */
  private void initialLayersHelper() {
    this.layersView.setLayout(new GridLayout(10, 1));
    this.layersView.setSize(500, 700);
    this.layersPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.layersPanel.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.layersPanel.setVisible(false);
  }

  /**
   * A helper for initializing the project view, which the project panel uses,
   * and the scrollable project panel itself.
   */
  private void initialProjHelper() {
    this.projView.setLayout(new BorderLayout());
    this.projView.setBackground(Color.GRAY);
    this.projectPanel.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.projectPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.projectPanel.getHorizontalScrollBar().setUnitIncrement(10);
    this.projectPanel.getVerticalScrollBar().setUnitIncrement(10);
    this.projectPanel.setBorder(new LineBorder(Color.GRAY));
    this.projectPanel.setPreferredSize(new Dimension(700, 500));
    this.projectPanel.setBackground(Color.GRAY);
    this.projectPanel.setVisible(false);
  }

  /**
   * A helper for initializing the buttons panel. Displays the different buttons
   * of functionalities which are available to the user, and sets the action commands.
   */
  private void initialButtonsHelper() {
    this.buttonsPanel.setLayout(new BoxLayout(this.buttonsPanel, BoxLayout.Y_AXIS));
    // creating the GUI feature buttons
    // initially creating the different options for saving an image of the collage project
    JButton ppmImg = new JButton("Save as PPM");
    ppmImg.setActionCommand("SAVEIMGPPM");
    ppmImg.addActionListener(this);
    JButton jpegImg = new JButton("Save as JPEG");
    jpegImg.setActionCommand("SAVEIMGJPEG");
    jpegImg.addActionListener(this);
    JButton pngImg = new JButton("Save as PNG");
    pngImg.setActionCommand("SAVEIMGPNG");
    pngImg.addActionListener(this);
    JButton saveProj = new JButton("Save Project");
    saveProj.setActionCommand("SAVEPROJ");
    saveProj.addActionListener(this);
    JButton addLayer = new JButton("Add Layer");
    addLayer.addActionListener(this);
    addLayer.setActionCommand("ADDLAYER");
    JButton addImgToLayer = new JButton("Add Image to Layer");
    addImgToLayer.addActionListener(this);
    addImgToLayer.setActionCommand("ADDIMG");
    JButton setFilter = new JButton("Set Filter");
    setFilter.addActionListener(this);
    setFilter.setActionCommand("SET");

    // adding the different GUI features to the buttons panel
    buttonsPanel.add(ppmImg);
    buttonsPanel.add(jpegImg);
    buttonsPanel.add(pngImg);
    buttonsPanel.add(saveProj);
    buttonsPanel.add(addLayer);
    buttonsPanel.add(addImgToLayer);
    buttonsPanel.add(setFilter);
    buttonsPanel.setVisible(false);
  }

  /**
   * A helper for initializing the start-up panel.
   */
  private void startUpHelper() {
    // creating the start-up panel
    this.startUpPanel.setSize(600, 400);
    this.startUpPanel.add(new JLabel("Welcome! How would you like to begin?"));
    // adding buttons for creating a new project and loading an existing one
    JButton createButton = new JButton("Create New Project");
    createButton.addActionListener(this);
    createButton.setActionCommand("NEW");
    JButton loadButton = new JButton("Load Existing Project");
    loadButton.addActionListener(this);
    loadButton.setActionCommand("LOAD");
    this.startUpPanel.add(createButton);
    this.startUpPanel.add(loadButton);
    this.startUpPanel.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // the object which the action command came from
    String command = e.getActionCommand();
    switch (command) {
      case "NEW":
        // the case where the user has selected to start a new project
        // creating the new project visual
        this.newProjHelper();
        this.startUpPanel.removeAll();
        this.startUpPanel.add(createProj);
        this.startUpPanel.setSize(500, 500);
        this.pack();
        break;
      case "SUBMIT":
        // the case where the user has entered inputs for a new project
        boolean badInputs = false;
        try {
          this.controller.createProj(nameEntry.getText(), Integer.parseInt(heightEntry.getText()),
                  Integer.parseInt(widthEntry.getText()));
        } catch (IllegalArgumentException e3) {
          badInputs = true;
          // if the controller cannot create a project with the user's arguments,
          // ask the user to input new arguments.
          try {
            this.renderMessage("Please submit new inputs. Project's height and width"
                    + " must be positive, and every project must have a name.");
          } catch (IOException e1) {
            throw new IllegalStateException("Could not transmit to the view.");
          }
        }
        if (!badInputs) {
          // the case where the inputs are not bad, and a project can be created
          // creating the layer buttons
          this.layerButtonsHelper();
          // updating the loi button to have the correct display
          JComponent loi = this.layerButtons.get(this.controller.getLoi());
          loi.setForeground(Color.RED);
          loi.setBorder(new LineBorder(Color.RED));
          loi.setFont(new Font("Selected", Font.BOLD, 20));
          this.resetPanelsHelper();
          // displaying the project
          this.projDisplayHelper();
          this.resetPanelsHelper();
        }
        break;
      case "LOAD":
        // when the user wants to load a project
        JFileChooser projMenu = new JFileChooser();
        projMenu.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filt = new FileNameExtensionFilter(
                "Collage Projects", "collage");
        projMenu.addChoosableFileFilter(filt);
        projMenu.setAcceptAllFileFilterUsed(false);
        int returnVal = projMenu.showOpenDialog(projMenu.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = projMenu.getSelectedFile();
          try {
            this.controller.loadProj(file);
          } catch (IOException e1) {
            try {
              this.renderMessage("Could not load the project.");
            } catch (IOException ex) {
              try {
                throw new IOException("Could not render the message.");
              } catch (IOException exc) {
                throw new RuntimeException(exc);
              }
            }
          }
          BufferedImage currentProj = this.controller.projDisplay();
          JLabel projLabel = new JLabel(new ImageIcon(currentProj));
          this.projView.add(projLabel, BorderLayout.CENTER);
          // load in the layers and set the loi button to have a different visual
          this.layerButtonsHelper();
          JComponent loiButton = this.layerButtons.get(this.controller.getLoi());
          loiButton.setForeground(Color.RED);
          loiButton.setBorder(new LineBorder(Color.RED));
          loiButton.setFont(new Font("Selected", Font.BOLD, 20));
          this.resetPanelsHelper();
        }
        break;
      case "SAVEIMGPPM":
        // when the user wants to save an image of the project they're
        // working on as a ppm file
        // the new file chooser. set so that the user is only able to save to directories
        JFileChooser chooseFold1 = new JFileChooser();
        chooseFold1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // add the new file chooser to the dialog panel
        this.dialogPanel.add(chooseFold1);
        this.dialogPanel.setVisible(true);
        this.dialogPanel.setSize(400, 400);
        // get the path of the selected folder and use it with the controller's
        // method for saving the image of a project
        int chooseVal1 = chooseFold1.showOpenDialog(chooseFold1.getParent());
        if (chooseVal1 == JFileChooser.APPROVE_OPTION) {
          // set this.selected = chosen directory
          this.selected = chooseFold1.getSelectedFile();
          // pass the selected folder's path to the controller
          this.controller.saveImg(this.selected.getPath() + "/"
                  + this.controller.getProjName() + ".ppm");
          // reset the panels
          this.resetPanelsHelper();
        }
        break;
      case "SAVEIMGJPEG":
        // when the user wants to save an image of the project they're
        // working on as a jpeg file
        // the new file chooser. set so that the user is only able to save to directories
        JFileChooser chooseFold2 = new JFileChooser();
        chooseFold2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // add the new file chooser to the dialog panel
        this.dialogPanel.add(chooseFold2);
        this.dialogPanel.setVisible(true);
        this.dialogPanel.setSize(400, 400);
        // get the path of the selected folder and use it with the controller's
        // method for saving the image of a project
        int chooseVal2 = chooseFold2.showOpenDialog(chooseFold2.getParent());
        if (chooseVal2 == JFileChooser.APPROVE_OPTION) {
          // set this.selected = chosen directory
          this.selected = chooseFold2.getSelectedFile();
          // pass the selected folder's path to the controller
          this.controller.saveImg(this.selected.getPath()
                  + "/" + this.controller.getProjName() + ".jpeg");
          // reset the panels
          this.resetPanelsHelper();
        }
        break;
      case "SAVEIMGPNG":
        // when the user wants to save an image of the project they're
        // working on as a png file
        // the new file chooser. set so that the user is only able to save to directories
        JFileChooser chooseFold3 = new JFileChooser();
        chooseFold3.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // add the new file chooser to the dialog panel
        this.dialogPanel.add(chooseFold3);
        this.dialogPanel.setVisible(true);
        this.dialogPanel.setSize(400, 400);
        // get the path of the selected folder and use it with the controller's
        // method for saving the image of a project
        int chooseVal3 = chooseFold3.showOpenDialog(chooseFold3.getParent());
        if (chooseVal3 == JFileChooser.APPROVE_OPTION) {
          // set this.selected = chosen directory
          this.selected = chooseFold3.getSelectedFile();
          // pass the selected folder's path to the controller
          this.controller.saveImg(this.selected.getPath() + "/"
                  + this.controller.getProjName() + ".png");
          // reset the panels
          this.resetPanelsHelper();
        }
        break;
      case "SAVEPROJ":
        // when the user wants to save the project they're working on.
        // will bring up a file chooser for the user to choose the folder
        // in which to save their collage project.
        // the new file chooser. set so that the user is only able to save to directories
        JFileChooser foldChoose = new JFileChooser();
        foldChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // add the new file chooser to the dialog panel
        this.dialogPanel.add(foldChoose);
        this.dialogPanel.setVisible(true);
        this.dialogPanel.setSize(400, 400);
        // get the path of the selected folder and use it with the controller's
        // method for saving a collage project file
        int foldVal = foldChoose.showOpenDialog(foldChoose.getParent());
        if (foldVal == JFileChooser.APPROVE_OPTION) {
          // set this.selected = chosen directory
          this.selected = foldChoose.getSelectedFile();
          // pass the selected file's path to the controller
          this.controller.saveProj(this.selected.getPath());
          // reset the panels
          this.resetPanelsHelper();
        }
        break;
      case "ADDLAYER":
        // when the user wants to add a layer to the project
        this.dialogPanel.setSize(400, 200);
        this.dialogPanel.add(new JLabel("Please enter layer name."));
        this.dialogPanel.add(this.layerNameEntry);
        JButton submitLayer = new JButton("Submit");
        submitLayer.addActionListener(this);
        submitLayer.setActionCommand("LAYERNAME");
        this.dialogPanel.add(submitLayer);
        this.dialogPanel.setVisible(true);
        this.hidePanels();
        this.mainPanel.add(dialogPanel);
        this.pack();
        break;
      case "LAYERNAME":
        // when the user has submitted a name for the new layer
        boolean badLayerName = false;
        String newLayerName = this.layerNameEntry.getText();
        try {
          this.controller.createLayer(newLayerName);
        } catch (IllegalArgumentException e2) {
          badLayerName = true;
        }
        if (badLayerName) {
          try {
            this.renderMessage("A layer with that name already exists. Layer not created.");
          } catch (IOException e1) {
            throw new IllegalStateException("Error when displaying message.");
          }
        } else {
          this.layerButtonsHelper();
          // updating the loi button to have the correct display
          JComponent loi = this.layerButtons.get(this.controller.getLoi());
          loi.setForeground(Color.RED);
          loi.setBorder(new LineBorder(Color.RED));
          loi.setFont(new Font("Selected", Font.BOLD, 20));
          this.resetPanelsHelper();
        }
        break;
      case "ADDIMG":
        // calling the helper for adding an image to the loi
        this.addImgHelper();
        break;
      case "IMG SUBMIT":
        // the case where the user has submitted coordinates
        // at which to place an image on the loi
        boolean badCoord = false;
        int xEntry = Integer.parseInt(this.xCoord.getText());
        int yEntry = Integer.parseInt(this.yCoord.getText());
        try {
          this.controller.addLoiImage(this.selected, xEntry, yEntry);
        } catch (IllegalArgumentException e2) {
          badCoord = true;
          try {
            this.renderMessage("Please enter a new coordinate to place the image"
                    + " at. Coordinate values must be positive integers, and should let the image"
                    + " fit on the layer.");
          } catch (IOException e1) {
            throw new IllegalStateException("Could not render message.");
          }
        }
        if (!badCoord) {
          this.dialogPanel.setVisible(false);
          this.projDisplayHelper();
          this.resetPanelsHelper();
        }
        break;
      case "SET":
        // the case where the user wants to set the loi's filter
        this.filtSetHelper();
        break;
      default:
        if (command.charAt(0) == 'L') {
          // the case where the user has clicked on a layer. that layer has to be kept as the
          // layer of interest.
          String[] loiChar = command.split("L");
          int newLoi = Integer.parseInt(loiChar[1]);
          this.loiDisplayHelper(newLoi, this.controller.getLoi());
          this.controller.setLoi(newLoi);
          break;
        }
        // the case where the user has chosen a filter
        try {
          this.controller.loiFilter(command);
        } catch (IOException ex) {
          throw new IllegalStateException("Could not set the filter.");
        }
        this.projDisplayHelper();
        this.resetPanelsHelper();
        break;
    }
  }

  /**
   * A helper for adding an image to the loi.
   */
  private void addImgHelper() {
    // when the user wants to add an image to the loi
    // intializing the dialog panel with the imgMenu file chooser
    // creating a file filter which only allows for the selection of PPM's
    FileFilter ppmOnly = new FileNameExtensionFilter("PPM Images", "ppm");
    this.imgMenu.addChoosableFileFilter(ppmOnly);
    this.imgMenu.setAcceptAllFileFilterUsed(false);
    // visualizing the dialog panel
    this.dialogPanel.removeAll();
    this.dialogPanel.add(this.imgMenu);
    this.dialogPanel.setSize(400, 400);
    this.dialogPanel.setVisible(true);
    this.mainPanel.add(dialogPanel);
    this.pack();
    int imgVal = this.imgMenu.showOpenDialog(this.imgMenu.getParent());
    if (imgVal == JFileChooser.APPROVE_OPTION) {
      // getting the selected file.
      this.selected = this.imgMenu.getSelectedFile();
      // removing the filechooser and adding the coordinate panel
      this.hidePanels();
      this.dialogPanel.remove(imgMenu);
      this.dialogPanel.setLayout(new BoxLayout(this.dialogPanel, BoxLayout.Y_AXIS));
      JPanel labelPanel = new JPanel();
      labelPanel.setLayout(new BorderLayout());
      labelPanel.add(new JLabel("Please enter the coordinate at which to place the "
              + "selected image"), BorderLayout.WEST);
      this.dialogPanel.add(labelPanel);
      // creating the button to submit the coordinate
      JButton submit = new JButton("Submit");
      submit.addActionListener(this);
      submit.setActionCommand("IMG SUBMIT");
      // creating the coordinate panel
      JPanel coordPanel = new JPanel();
      coordPanel.setLayout(new GridLayout(1, 5));
      coordPanel.add(new JLabel("X-coordinate:"));
      coordPanel.add(this.xCoord);
      coordPanel.add(new JLabel("Y-coordinate:"));
      coordPanel.add(this.yCoord);
      coordPanel.add(submit);
      coordPanel.setSize(800, 200);
      this.dialogPanel.add(coordPanel);
      this.setResizable(false);
      this.pack();
    }
  }

  /**
   * A helper which creates the panel for when the user wants to create a new project.
   */
  private void newProjHelper() {
    this.namePanel.add(this.nameEntry);
    this.heightPanel.add(this.heightEntry);
    this.widthPanel.add(this.widthEntry);
    this.createProj.setLayout(new BoxLayout(this.createProj, BoxLayout.Y_AXIS));
    this.createProj.setSize(new Dimension(400, 400));
    this.createProj.add(this.namePanel);
    this.createProj.add(this.heightPanel);
    this.createProj.add(this.widthPanel);
    this.createProj.add(this.submit);
    this.submit.addActionListener(this);
    this.createProj.setVisible(true);
  }

  /**
   * A helper for when the user wants to set the filter of the loi.
   * Brings up the different filters and allows the user to select one
   */
  private void filtSetHelper() {
    // when the user wants to set the filter for the layer-of-interest.
    // declaring the filter JButtons and creating a list to store them.
    // the list will be iterated over to add the GUI as an action listener,
    // and to add each JButton to the panel.
    JButton f1;
    JButton f2;
    JButton f3;
    JButton f4;
    JButton f5;
    JButton f6;
    JButton f7;
    JButton f8;
    JButton f9;
    JButton f10;
    JButton f11;
    JButton f12;
    JButton f13;
    List<JButton> fButtons = new ArrayList<>();

    // initializing each button, giving it a unique action command, and adding it to the list
    f1 = new JButton("Remove Filter");
    f1.setActionCommand("normal");
    fButtons.add(f1);
    f2 = new JButton("Red Component");
    f2.setActionCommand("red-component");
    fButtons.add(f2);
    f3 = new JButton("Blue Component");
    f3.setActionCommand("blue-component");
    fButtons.add(f3);
    f4 = new JButton("Green Component");
    f4.setActionCommand("green-component");
    fButtons.add(f4);
    f5 = new JButton("Value Brighten");
    f5.setActionCommand("brighten-value");
    fButtons.add(f5);
    f6 = new JButton("Value Darken");
    f6.setActionCommand("darken-value");
    fButtons.add(f6);
    f7 = new JButton("Intensity Brighten");
    f7.setActionCommand("brighten-intensity");
    fButtons.add(f7);
    f8 = new JButton("Intensity Darken");
    f8.setActionCommand("darken-intensity");
    fButtons.add(f8);
    f9 = new JButton("Luma Brighten");
    f9.setActionCommand("brighten-luma");
    fButtons.add(f9);
    f10 = new JButton("Luma Darken");
    f10.setActionCommand("darken-luma");
    fButtons.add(f10);
    f11 = new JButton("Multiply Filter");
    f11.setActionCommand("multiply");
    fButtons.add(f11);
    f12 = new JButton("Difference Filter");
    f12.setActionCommand("difference");
    fButtons.add(f12);
    f13 = new JButton("Screen Filter");
    f13.setActionCommand("screen");
    fButtons.add(f13);

    // now, each filter button will be have its action
    // listener set and be placed on the panel
    for (JButton b : fButtons) {
      b.addActionListener(this);
      this.dialogPanel.add(b);
    }

    // displaying the buttons over the hidden panels
    this.dialogPanel.setLayout(new GridLayout(4, 4));
    this.dialogPanel.setVisible(true);
    this.mainPanel.setSize(400, 400);
    this.mainPanel.add(this.dialogPanel);
    // other vis updates
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.hidePanels();
    this.revalidate();
    this.repaint();
    this.pack();
  }

  @Override
  public void renderMessage(String message) throws IOException {
    JLabel messageLabel = new JLabel(message);
    JPanel messagePanel = new JPanel();
    JPopupMenu messageMenu = new JPopupMenu();
    messageLabel.setFont(new Font("Message", Font.ITALIC, 16));
    messageLabel.setForeground(Color.BLACK);
    messageLabel.setBackground(Color.WHITE);
    messagePanel.setSize(400, 400);
    messagePanel.add(messageLabel);
    messagePanel.setVisible(true);
    messageMenu.add(messagePanel);
    messageMenu.show(this.mainPanel, 0, 0);
  }

  private void layerButtonsHelper() {
    List<ILayer> layers = this.controller.getLayers();
    this.layersView.removeAll();
    this.layerButtons = new ArrayList<>();
    int i = 0;
    for (ILayer l : layers) {
      // for each ILayer of this project, adds it to the layers panel as a button.
      // to source the ILayer of interest, its index will be part of the button's action command,
      // which will be separable using the split method.
      // this is needed for when a layer is clicked on, and the user wants to edit it in some way.
      // the layer selected by the user becomes the "layer of interest" (loi).
      JButton newButton = new JButton(l.getName());
      newButton.setSize(300, 150);
      newButton.addActionListener(this);
      newButton.setActionCommand("L" + i);
      newButton.setBorder(new LineBorder(Color.LIGHT_GRAY));
      newButton.setVisible(true);
      this.layerButtons.add(newButton);
      this.layersView.add(newButton);
      i++;
    }
    this.layersView.setVisible(true);
  }

  /**
   * A helper to reset the GUI to display the buttons, project, and layers panel.
   */
  private void resetPanelsHelper() {
    this.startUpPanel.removeAll();
    this.startUpPanel.setVisible(false);
    this.dialogPanel.removeAll();
    this.dialogPanel.setVisible(false);
    this.buttonsPanel.setVisible(true);
    this.projectPanel.setVisible(true);
    this.layersPanel.setVisible(true);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.revalidate();
    this.repaint();
    this.pack();
  }

  /**
   * A helper for changing the representation of the previous layer-of-interest, and
   * for updating the representation of the new layer-of-interest.
   *
   * @param currentLoi the index of the new loi, which will have its display changed to indicate
   *                   that it has been selected by the user
   * @param oldLoi     the index of the previous loi, which will have its display changed to
   *                   indicate that it is no longer selected by the user
   */
  private void loiDisplayHelper(int currentLoi, int oldLoi) {
    JComponent oldLoiButton = this.layerButtons.get(oldLoi);
    oldLoiButton.setForeground(Color.BLACK);
    oldLoiButton.setBorder(new LineBorder(Color.LIGHT_GRAY));
    oldLoiButton.setFont(new Font("Normal", Font.PLAIN, 12));
    JComponent loiButton = this.layerButtons.get(currentLoi);
    loiButton.setForeground(Color.RED);
    loiButton.setBorder(new LineBorder(Color.RED));
    loiButton.setFont(new Font("Selected", Font.BOLD, 20));
    this.repaint();
    this.revalidate();
    this.pack();
  }

  /**
   * A helper for displaying the new state of the collage after any changes have been made.
   * Also updates the layers panel by calling resetPanelsHelper
   */
  private void projDisplayHelper() {
    BufferedImage img = this.controller.projDisplay();
    JLabel imgLabel = new JLabel(new ImageIcon(img));
    imgLabel.setVisible(true);
    this.projView.removeAll();
    this.projView.add(imgLabel, BorderLayout.CENTER);
    this.resetPanelsHelper();
    this.revalidate();
    this.repaint();
  }

  /**
   * This is a helper to hide the button, project, and layer panels.
   */
  private void hidePanels() {
    this.buttonsPanel.setVisible(false);
    this.projectPanel.setVisible(false);
    this.layersPanel.setVisible(false);
  }
}
