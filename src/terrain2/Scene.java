/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain2;

import javax.vecmath.Point3d;

/**
 *
 * @author chon
 */
public class Scene {

  private static SKPControl skp = new SKPControl();
  private static final String SKP_TITLE = "Untitled - SketchUp";
  private static final String EDITOR_TITLE = "Output - terrain2 (run)   - Editor";
  private static final String RC_TITLE = "Ruby Console";
  private static JAutoIt lib = JAutoIt.INSTANCE;

  
  public boolean setCam(Point3d eye, Point3d target)
  {
    /*initiate camera by setting  eye(position of the camera) and target
     (the position where camera is staring at).*/
    lib.AU3_WinActivate(SKP_TITLE, "");
    lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");

    lib.AU3_Send("anim.setCam(" + eye.x + "," + eye.y + "," + eye.z + "," + target.x + "," + target.y + "," + target.z + ")", 1);
    lib.AU3_Send("{ENTER}", 0);

    lib.AU3_Sleep(1000);
    skp.getDisplay();
    lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
    // clear the display
    lib.AU3_Sleep(1000);
    lib.AU3_WinActivate(EDITOR_TITLE, "");
    return true;
  }
}
