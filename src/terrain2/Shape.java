
package terrain2;

import javax.vecmath.Point2f;

/**
 * @author chon
 * abstract class for cylinder and block
 */

public class Shape extends Model {

  private int radius;
  private String type;
  private JAutoIt lib = JAutoIt.INSTANCE;
  private SKPControl skp = new SKPControl();
  private String SKP_TITLE = "Untitled - SketchUp";
  private String EDITOR_TITLE = "Output - terrain2 (run)   - Editor";
  private String RC_TITLE = "Ruby Console";
  private int offsetZ = 0;

  
  public Shape()
  {super("", new Point2f(0, 0), 0, 1, 0, false);}
   
   
  public void setRadius(int radius)
  {this.radius = radius;}

  
  public int getRadius()
  {return radius;}

  
  public void setType(String type)
  {this.type = type;}

  
  public String getType()
  {return type;}

  
  public boolean texture(String sideTex)
  { //apply sideTex to the side of shape and black color to the roof
    String roof = "black.jpg";
    lib.AU3_WinActivate(SKP_TITLE, "");
    lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
    lib.AU3_Send("shape.texture(\"" + getName() + "\", \"" + roof + "\", \"" + sideTex + "\") {ENTER}", 0);
    skp.getDisplay();
    lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
    // clear the display
    lib.AU3_Sleep(1000);
    lib.AU3_WinActivate(EDITOR_TITLE, "");
    return true;
  }//end of texture()

  
  public boolean texture(String roof, String side)
  {
    // apply side texture and roof texture to a shape
    lib.AU3_WinActivate(SKP_TITLE, "");
    lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
    lib.AU3_Send("shape.texture(\"" + getName() + "\", \"" + roof + "\", \"" + side + "\") {ENTER}", 0);
    skp.getDisplay();
    lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
    // clear the display
    lib.AU3_Sleep(1000);
    lib.AU3_WinActivate(EDITOR_TITLE, "");
    return true;
  }//end of texture()

}//end of Shape class
