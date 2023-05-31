package terrain2;

import java.nio.file.Paths;

/**
 * @author chon
 */
public class SKPControl {

  private static final String SKP_EXE = "C:/Program Files (x86)/SketchUp/SketchUp 2013/SketchUp.exe";
  private static final String SKP_START_TITLE = "Welcome to SketchUp";
  private static final String START_BUTTON = "Start using SketchUp";
  private static final String SKP_TITLE = "Untitled - SketchUp";
  private static final String EDITOR_TITLE = "Output - terrain2 (run)   - Editor";
  private static final String RC_TITLE = "Ruby Console";
  private static JAutoIt lib = JAutoIt.INSTANCE;
  private static String directory;
  private AnimationListener lis;

  /* If we get here then the Ruby Console is the active
   window. It consists of two controls:
   * Edit2 is the display control (at the top)
   * Edit1 is the input text field (at the bottom)
   */
    // enter commands into Edit1
  // ========================= support methods ==============
  public boolean startSketchUp() /* Start Sketchup and press the start button to get an empty window*/
  {
    String skpPath = System.getenv("ProgramFiles") + "/" + SKP_EXE;
    if (!createWin(SKP_START_TITLE, SKP_EXE)) {
      System.out.println("Sketchup window could not be created");
      System.exit(1);
    }

    pressButton(SKP_START_TITLE, START_BUTTON);
    lib.AU3_Sleep(2000);

    if (lib.AU3_WinExists(SKP_TITLE, "") == 0) {
      System.out.println("\"" + SKP_TITLE + "\" does not exist");
      System.exit(1);
    }
    System.out.println("\"" + SKP_TITLE + "\" created");
    lib.AU3_WinActivate(EDITOR_TITLE, "");

    startRubyConsole();

    //here
    AnimThread msg = new AnimThread(Paths.get(directory));
    msg.setListener(lis);
    Thread msgThread = new Thread(msg, "message");
    //Start the threads

    msgThread.start();

    String terrainClass = directory + "Terrain.rb";
    String Shape = directory + "Shape.rb";
    String Model = directory + "model.rb";
    String Animation = directory + "Animation.rb";
    String cameraClass = directory + "Camera.rb";
    lib.AU3_WinActivate(SKP_TITLE, "");
    lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
    lib.AU3_Send("load{SPACE}\"" + terrainClass + "\"{ENTER}", 0);
    lib.AU3_Send("terrain = Terrain.new {ENTER}", 0);
    lib.AU3_Send("load{SPACE}\"" + Shape + "\"{ENTER}", 0);
    lib.AU3_Send("shape = Shape.new {ENTER}", 0);
    lib.AU3_Send("load{SPACE}\"" + Model + "\"{ENTER}", 0);
    lib.AU3_Send("load{SPACE}\"" + cameraClass + "\"{ENTER}", 0);
    lib.AU3_Send("load{SPACE}\"" + Animation + "\"{ENTER}", 0);
    lib.AU3_Send("anim = Animation.new {ENTER}", 0);
    lib.AU3_Send("anim.setPath(\"" + directory + "\") {ENTER}", 0);

    return true;
  }  // end of startSketchUp()

  private boolean startRubyConsole() /* Assuming that the sketchup window is active, send 
   alt-w alt-r to open the Riby Console window
   */ {
    lib.AU3_WinActivate(SKP_TITLE, "");
    System.out.println("Bring up \"" + RC_TITLE + "\"");
    lib.AU3_Send("{^a", 0);
    lib.AU3_Send("{DEL}", 0);
    lib.AU3_Send("{ALT down}wr{ALT up}", 0);   // alt-w, alt-r

    if (lib.AU3_WinWaitActive(RC_TITLE, "", 4) == 0) {
      System.out.println("\"" + RC_TITLE + "\" does not exist");
      System.exit(1);
    }

    System.out.println("\"" + RC_TITLE + "\" created");
    lib.AU3_WinActivate(EDITOR_TITLE, "");
    return true;
  }  // end of startRubyConsole()

  private boolean createWin(String title, String appName) // run app and wait for specified window title
  {
    if (lib.AU3_WinExists(title, "") != 0) {
      System.out.println("\"" + title + "\" already exists");
      lib.AU3_WinActivate(title, "");
      return true;
    }

    if (lib.AU3_Run(appName, "", JAutoIt.SW_SHOWNORMAL) == 0) {
      System.out.println("\"" + appName + "\" could not be executed");
      return false;
    }

    System.out.println("Waiting for \"" + appName
            + "\" window to appear...");
    lib.AU3_WinWaitActive(title, "", 10000);

    if (lib.AU3_WinExists(title, "") == 1) {
      System.out.println("\"" + title + "\" has appeared");
      return true;
    } else {
      System.out.println(appName + " has not appeared");
      return false;
    }
  }  // end of createWin()

  private void pressButton(String title, String butStr)
  {
    System.out.println("Pressing \"" + butStr + "\" in \"" + title + "\"");
    lib.AU3_ControlClick(title, "",
            "[CLASS:Button; TEXT:" + butStr + "]",
            "left", 1, JAutoIt.AU3_INTDEFAULT, JAutoIt.AU3_INTDEFAULT);
  } // end of pressButton()

  public boolean closeWin(String title) // close window titled if it exists
  {
    if (lib.AU3_WinExists(title, "") == 1) {
      lib.AU3_WinClose(title, "");
      if (lib.AU3_WinExists(title, "") != 0) {
        System.out.println("\"" + title + "\" not closed");
      } else {
        System.out.println("\"" + title + "\" has been closed");
      }
    } else {
      System.out.println("\"" + title + "\" does not exist");
    }
    return true;
  }  // end of closeWin()

  public String[] getDisplay() // return the Edit2 display text as an array of lines

  {
    char[] display = new char[200];

    lib.AU3_ControlGetText(RC_TITLE, "", "Edit2",
            display, display.length);
    String displayText = AUUtils.chars2String(display);
    System.out.println("----------- Display -----------");
    System.out.println(displayText);
    System.out.println("-------------------------------");

    String[] lns = displayText.split("\\r?\\n");
    System.out.println("Display lines count: " + lns.length);

    return lns;
  }  // end of getDisplay()

  public void setDirectory(String directory)
  {this.directory = directory;}

  public String getDirectory()
  {return directory;}

  public void setListener(AnimationListener lis)
  {this.lis = lis;}

}  // end of SKPControl class
