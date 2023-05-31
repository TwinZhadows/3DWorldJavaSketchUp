
package terrain2;

import javax.vecmath.Point2f;

/**
 * @author chon
 * automatically solve a collision by updating steps array of source and target 
 * in order to have target waits for small amount of time, while source is 
 * reversing and moving around the target. The class contains methods 
 * for recovering a collision and manipulating steps array of the model
 */
public class CollisionRecovery {

  Step[] stepsArray;
  static Step[] modelStep;
  TerrainConstructor terrain;
  private static SKPControl skp = new SKPControl();
  private static final String EDITOR_TITLE = "Output - terrain2 (run)   - Editor";
  private static final String RC_TITLE = "Ruby Console";
  private static JAutoIt lib = JAutoIt.INSTANCE;

  public static void basicRecovery(TerrainConstructor terrain, CollisionEvent e)
  {
    /* use collision information to calculate coordinate P1, P2, P3, and P4. 
       Then update steps array of source and target in order to make source 
       model moves to P1-P4(around the target), while the target is waiting.
    */
    Step[] targetStep;
    float angle = e.getSourceAngle();
    int time = e.getTime();
    //model param
    modelStep = e.getSourceStep();
    Point2f p0 = e.getSourcePos();
    Point2f dimen = e.getSourceDimen();

    //target param
    targetStep = e.getTargetStep();
    Point2f tarPosition = e.getTargetPos();
    Point2f tarDimen = e.getTargetDimen();

    Step[] mov1 = null, mov2 = null, mov3 = null, mov4 = null, mov5 = null, mov6 = null, mov7 = null, mov8 = null, mov9 = null, mov10 = null;
    Step[] tarMov1 = null, tarMov2 = null;
    Point2f p1 = null, p2 = null, p3 = null, p4 = null;

    float offsetX = 0, offsetY = 0, reverse = 0;
    int rot = -90;
    int p4Index = 0;
    reverse = dimen.x / 2;
    offsetX = dimen.x + tarDimen.x + reverse;
    offsetY = dimen.y + tarDimen.y;
    p1 = new Point2f((float) (p0.x - reverse * Math.cos(Math.toRadians(angle))), (float) (p0.y - reverse * Math.sin(Math.toRadians(angle))));
    p2 = new Point2f((float) (p1.x + offsetY * Math.cos(Math.toRadians(90 - angle))), (float) (p1.y - offsetY * Math.sin(Math.toRadians(90 - angle))));
    p3 = new Point2f((float) (p2.x + offsetX * Math.cos(Math.toRadians(angle))), (float) (p2.y + offsetX * Math.sin(Math.toRadians(angle))));
    p4Index = findClosestPoint(new Point2f((float) (p1.x + offsetX * Math.cos(Math.toRadians(angle))), (float) (p1.y + offsetX * Math.sin(Math.toRadians(angle)))), modelStep);
    p4 = new Point2f(modelStep[p4Index].getX(), modelStep[p4Index].getY());
    //update source steps array
    int tarIndex = findClosestPoint(new Point2f(tarPosition.x, tarPosition.y), targetStep);
    mov1 = terrain.genStepsArray(p0, p1, 0, 0, time, time + 1);
    mov2 = terrain.genStepsArray(p1, p1, 0, rot, time + 1, time + 2);
    mov3 = terrain.genStepsArray(p1, p2, 0, 0, time + 2, time + 3);
    mov4 = terrain.genStepsArray(p2, p2, 0, -1 * rot, time + 3, time + 4);
    mov5 = terrain.genStepsArray(p2, p3, 0, 0, time + 4, time + 5);
    mov6 = terrain.genStepsArray(p3, p3, 0, -1 * rot, time + 5, time + 6);
    mov7 = terrain.genStepsArray(p3, p4, 0, 0, time + 6, time + 7);
    mov8 = terrain.genStepsArray(p4, p4, 0, rot, time + 7, time + 8);
    mov9 = extractArray(p4Index, time + 8, modelStep);
    //update target steps array
    tarMov1 = terrain.genStepsArray(tarPosition, tarPosition, 0, 0, time - 1, time + 4);
    tarMov2 = extractArray(tarIndex, time + 4, targetStep);
    
    //load animation to SketchUp side
    for (Model model : terrain.getDynamicModels()) {
      if (model.getName().equals(e.getSourceName())) {
        model.setAnimation(mov1);
        model.setAnimation(mov2);
        model.setAnimation(mov3);
        model.setAnimation(mov4);
        model.setAnimation(mov5);
        model.setAnimation(mov6);
        model.setAnimation(mov7);
        model.setAnimation(mov8);
        model.setAnimation(mov9);
      }
      if (model.getName().equals(e.getTargetName())) {
        model.setAnimation(tarMov1);
        model.setAnimation(tarMov2);
      }
    }
    //resume animations at 'time'
    terrain.startAnimationAt(time);
  }//end of basicRecovery()

  
  public static Step[] extractArray(int index, int time, Step[] stepsArray)
  {
    /*extract subarray from stepsArray started from the given index and update 
      the time of each element of the array started from 'time'
    */
    int count = 0;
    int size = stepsArray.length - index;
    float x, y, z, angle;
    Step step;
    Step[] newSteps = new Step[size];
    for (int i = index; i < stepsArray.length; i++) {
      step = stepsArray[i];
      x = step.getX();
      y = step.getY();
      z = step.getZ();
      angle = step.getAngle();
      newSteps[count] = new Step(time + count, x, y, z, angle);
      count++;
    }
    return newSteps;
  }//end of extractArray()

  
  public static int findClosestPoint(Point2f p4, Step[] stepsArray)
  { //return the index of position closest to given p4
    Point2f p;
    int index = 0;
    Point2f start = new Point2f(stepsArray[0].getX(), stepsArray[0].getY());
    float dist = start.distance(p4);

    for (int i = 1; i < stepsArray.length; i++) {
      p = new Point2f(stepsArray[i].getX(), stepsArray[i].getY());
      if (p.distance(p4) <= dist) {
        dist = p.distance(p4);
        index = i;
      } else {
        break;
      }
    }
    return index;
  }//end of findClosestPoint()

  
  public static boolean stopModel(String name)
  {
    //stop the animation of the model
    lib.AU3_Send("anim.stopModel(\"" + name + "\") {ENTER}", 0);
    skp.getDisplay();
    lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
    lib.AU3_Sleep(1000);
    lib.AU3_WinActivate(EDITOR_TITLE, "");
    return true;
  }//end of stopModel()

}//end of CollisionRecovery
