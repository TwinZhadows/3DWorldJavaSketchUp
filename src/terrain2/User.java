package terrain2;

import javax.vecmath.Point2f;
import javax.vecmath.Point3d;

//contains methods for creating user model and setting camera to the model
public class User extends Model {

  private static SKPControl skp = new SKPControl();
  private static final String SKP_TITLE = "Untitled - SketchUp";
  private static final String EDITOR_TITLE = "Output-terrain2(run)- Editor";
  private static final String RC_TITLE = "Ruby Console";
  private static JAutoIt lib = JAutoIt.INSTANCE;


  public User(String file, Point2f position, int offsetZ, float scale, float 
  rotation)
  {super(file, position, offsetZ, scale, rotation, true);}
  
  
  public boolean setCam(Point3d eye)
  { 
    //set the camera to follow the user model
    Point2f position = getPosition();
    float height = getHeight();
    lib.AU3_WinActivate(SKP_TITLE, "");
    lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
    lib.AU3_Send("anim.setCam(" + eye.x + "," + eye.y + "," + eye.z + "," + 
                  position.x + "," + position.y + "," + height + ")", 1);
    lib.AU3_Send("{ENTER}", 0);
    lib.AU3_Send("anim.setDynamicCam(true)", 1);
    lib.AU3_Send("{ENTER}", 0);
    lib.AU3_Sleep(1000);
    skp.getDisplay();
    lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
    // clear the display
    lib.AU3_Sleep(1000);
    lib.AU3_WinActivate(EDITOR_TITLE, "");
    return true;
  }//end of setCam()

}//end of User class
