/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain2;

import java.io.IOException;

import javax.vecmath.Point3d;
import javax.vecmath.Point2f;

/**
 * @author chon
 * Construct a scene containing static house, table, griller, trees, and dynamic
 * cars
 */
public class HouseScene extends Scene implements AnimationListener {

  private static TerrainConstructor terrain;
  private static Model redCar;
  private static Model yellowCar;

  public HouseScene()
  {
    SketchUp skp = new SketchUp();
    skp.setListener(this);
    skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
    skp.startSketchUp();
    createTerrain();
    setCam(new Point3d(299, -383, 296), new Point3d(301, -315, 265));
    addStaticModels();
    addDynamicModels();
    startAnimations();
  }

  
  private static boolean createTerrain()
  {
    //generate rough terrain with two texture levels from heightmap before applying street texture to the lower region and grass texture to the higher region
    try {
      //create terrain
      terrain = new TerrainConstructor("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/height64.png", 10, 10);
      terrain.setLevel(2);
      terrain.setTexture(1, "street2.jpg", 30);
      terrain.setTexture(2, "grass.jpg", 70);
      terrain.texture();
      return true;
    } catch (IOException ex) {
      System.err.println("IOException: " + ex.getMessage());
    }
    return false;
  }//end of createTerrain()

  
  private static boolean addStaticModels()
  {
    //add scenery models to the scene including a house, griller, trees, and table set
    Model house = new Model("house.skp", new Point2f(320, 480), 8, (float) 0.5, 0, false);//String file, Point2f p, int offsetZ, float scale, float angle, boolean movable
    Model griller = new Model("griller.skp", new Point2f(160, 320), 0, (float) 1, 0, false);
    Model tableSet = new Model("tableSet.skp", new Point2f(320, 290), 0, (float) 1, 90, false);
    Model tree = new Model("greenTree.skp", new Point2f(120, 480), 0, (float) 0.5, 90, false);
    Model tree2 = new Model("greenTree.skp", new Point2f(520, 480), 0, (float) 0.3, 90, false);
    terrain.addModel(house);
    terrain.addModel(griller);
    terrain.addModel(tableSet);
    terrain.addModel(tree);
    terrain.addModel(tree2);
    return true;
  }//end of addStaticModels()

  
  private static boolean addDynamicModels()
  {
    //add moving models on the road including a yellow car and another red car
    redCar = new Model("redCar.skp", new Point2f(60, 80), 0, (float) 0.5, 0, true);
    yellowCar = new Model("yellowCar.skp", new Point2f(570, 60), 0, (float) 0.5, 180, true);
    terrain.addModel(redCar);
    terrain.addModel(yellowCar);
    return true;
  }//end of addDynamicModels()

  
  private static boolean startAnimations()
  {
    Step[] yellowCarSteps = terrain.genStepsArray(new Point2f(570, 60), new Point2f(60, 60), 0, 10);
    Step[] redCarSteps = terrain.genStepsArray(new Point2f(60, 80), new Point2f(570, 80), 0, 10);
    redCar.setAnimation(redCarSteps);
    yellowCar.setAnimation(yellowCarSteps);
    terrain.startAnimation();
    return true;
  }//end of startAnimations()

  
  private void genTargetSteps(CollisionEvent e, int offset)
  {
    /*get collision information and update steps array of the collision target
    */
    Point2f targetPosition = e.getTargetPos();
    float targetAngle = e.getTargetAngle();
    int time = e.getTime();
    String targetName = e.getTargetName();
    recoverCollision(targetName, time, targetPosition, targetAngle, offset);
  }//end of genTargetSteps()

  
  private void genSourceSteps(CollisionEvent e, int offset)
  {
    /*get collision information of collision source and update the steps array 
      the same way as collision target 
    */
    float angle = e.getSourceAngle();
    int time = e.getTime();
    Point2f position = e.getSourcePos();
    String sourceName = e.getSourceName();
    recoverCollision(sourceName, time, position, angle, offset);
  }//end of genSourceSteps()

  private void recoverCollision(String modelName, int time, Point2f position, float angle, int offset)
  { /*update steps array of the model accordingly to a given name; the model 
      rotate 180 degrees and continue for small distance
    */
    Step[] steps1, step2;
    Point2f p1;
    p1 = new Point2f((float) (position.x + offset * Math.cos(Math.toRadians(180 - angle))), (float) (position.y - offset * Math.sin(Math.toRadians(180 - angle))));
    steps1 = terrain.genStepsArray(position, position, 0, -180, time, time + 1);
    step2 = terrain.genStepsArray(position, p1, time + 1, time + 5);
    Model target = terrain.getModel(modelName);
    target.setAnimation(steps1);
    target.setAnimation(step2);
  }//end of recoverCollision()

  
  public void finish(FinishEvent e)
  {System.out.println("finished");}

  
  public void collision(CollisionEvent e)
  { //response to a collision by updating steps array of source and target before resuming animations
    int time = e.getTime();
    int offset = 200;
    genSourceSteps(e, offset);
    genTargetSteps(e, offset);
    terrain.startAnimationAt(time);
  }//end of collision()

  
  public static void main(String args[])
  {new HouseScene();}

}
