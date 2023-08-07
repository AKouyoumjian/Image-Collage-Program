Collager Version 1.0

Features:

- This is an image collaging program that allows the user to utilize multiple layers to add images and
apply filters to the project.
- We support .ppm, .png, and .jpeg file type for adding and saving images.
- Using the Model, View, Controller (MVC) design, our code is intended to be as decoupled as possible
so that unnecessary information and access is not leaked between the three components.

The program is available in two modes:
    * Text-based Mode:
                - Command line arg: "-text" to use the terminal to input instructions for the Collager.

                - Command line arg: "-file filePath" to use a script's instructions.

    * Graphical User Interface Mode:
                - Leave command line arguments blank.
                - User is prompted to creat new project or load existing.


*** An example UseMe file has been provided, offering a set of instructions on how to use
                        the program, is located in the res Folder ***

*** All features of the Collager are available in both text-based and GUI modes. ***


Requirements/dependencies:
    - Java 11 or higher JRE
    - JUnit 4 for running the tests


Design and Structure of the code:

MODEL

- We created an Enumeration for FilterOption since there is a finite number of options.

- We created an IPixel interface and RGBPixel, HSLPixel classes representing pixels with rgba or hsl fields,
and a maxValue field so we can know what the maximum value can be when doing our clamping.

- We implemented an ILayer interface and a CollageLayer class which uses an
List<List<IPixel>> to represent the picture. It also keeps track of the original image
in case later on, we need to add revert/redo functionality.

- We created an IProject interface and CollageProject class that represents the Collager model,
containing a list<ILayer> along with other informative fields about the project.


VIEW

- IView interface and CollageTextView class was created for the text-based view operations.
- For the GUI mode, JFrameProjectView class was created.


CONTROLLER

- For the text-based mode, interface CollageController was created,
 with implementation of ControllerImpl for the Collager.

- For GUI mode, interface Features and class FeaturesImpl were created. More information on this below.

* Utilities:
- We used the command design pattern to facilitate the "text based scripting" operations in
the controller to ICommands. The user's collage instructions are handled this way.
- Since these commands are part of the controller, they have access to the model and view if needed.
The commands for new-project and load-project do not have separate classes because they
need to be in the ControllerImpl class so that they can mutate the controller's model.
- For converting from png, jpeg, and ppm to our IProject format and back, as well as converting from
HSL to RBG pixel representations, ImageUtil, JpegAndPngUtil, and RepresentationConverter in the
utilities package of the controller were implemented.



DEEPER DIVE INTO THE IMPLEMENTATION OF THE GUI
- the collager gui extends JFrame, and has a large main panel on which three other panels are displayed:
    1. the buttons panel. this contains options for the user to save the current project as a .collage file,
       to set the filter of the loi (layer-of-interest, the layer currently selected by the user),
       to add a layer to the project, and to add a PPM image to the loi.
    2. the project panel. this displays the current state of the collage project. everytime an update is
       made to the project, the controller calls a method which recompresses the project's layers, and
       this image is displayed in the project panel. if the project is too large to fit inside the
       project panel, vertical and horizontal scrollbars are added as needed.
    3. the layers panel. this displays the name of each layer in the project as a button. when the user
       clicks on one of these buttons, the loi changes to be the layer selected by the user, and all
       layer edits will happen to that layer until the user selects a different loi. the loi is indicated
       on the layers panel, with a larger, bolded, and red font.
- when the program first runs, the start-up panel is shown. it gives options for the user to either
  create a new project, or to load an existing one. depending on what the user chooses, the start-up
  panel will change to ask for inputs for a new project, or will bring up a file-chooser which the
  user can use to select a .collage file to load.
- else, there is a dialog panel that will be added to the main panel whenever further input is needed from
  the user. this dialog panel displays different options based on the action that the user is taking.

HANDLING USER ACTIONS
- various user actions are handled through the actionPerformed method. the action command from the gui
  is used in a switch statement to alter the gui/call a helper to alter the gui, and to call a controller
  method/call a helper method which will act on the controller somehow.

EDITING A LAYER
- the loi (layer-of-interest) is the layer that all changes will be made to. when a change is made to the loi,
  the project panel is automatically updated to reflect any filter, added image, or other changes to the loi.
  a new loi can be selected by clicking on it on the right side of the GUI. the loi will change color to
  indicate that it has been selected.
- IMPORTANT NOTE: my partner and i were unable to implement the multiply, screen, and difference filters within
  the gui. as such, we do not have example images with them included in this submission.


COMMAND LINE IMPLEMENTATION
- As per A5 specifications, program accepts the following command line inputs:
        * java -jar Program.jar -file path-of-script-file
        * java -jar Program.jar -text
        * java -jar Program.jar

- This has been implemented in the ProjectUI's main method with a switch case. By default, if the
command line input is not recognized, error message is displayed on console via System.out.println
that describes the problem to the user and quits the program.



Necessary Files for View(s) to Compile:

- Controller: Features Interface
    - This interface was necessary, as it detailed the features which the GUI's controller needed to
      implement. The Features interface exists to describe actions on the model which allow the GUI
      to function properly, based on user inputs.
- Model
    1. IFilterOption: Provides methods for which a filter option must have, in order to properly work as
       filters should on the project. Needed to be included, as the other Model interfaces have methods
       which return or take in objects of this interface.
    2. ILayer: Details the methods which are necessary for a layer in the collage. Necessary to include
       because other Model interfaces have methods which return or take in objects of this interface.
    3. IPixel: Gives various methods which are needed for the actions that could be taken on a pixel
       within the collage. Necessary to include because other Model interfaces have methods which return
       or take in objects of this interface.
    4. IProject: Has various methods which act on the collage project as a whole. Necessary to include
       since the controller must call the project's methods in response to user inputs.
- View
    1. IView: Details methods which are needed in order to properly display information to the user.
       Necessary to include because these methods are called in response to user input, and thus the
       controllers must use objects of this interface.
    2. CollageTextView: The text-view for a collage project.
    3. JFrameProjectView: The GUI for a collage project.