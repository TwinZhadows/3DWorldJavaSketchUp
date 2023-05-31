package terrain2;

import java.io.IOException;
import javax.vecmath.Point2f;
import javax.vecmath.Point3d;

/**
 * @author chon
 * Construct a city scene containing multiple shapes as buildings and some
 * dynamic cars
 */
public class CityScene extends Scene implements AnimationListener {

  private static TerrainConstructor terrain;
  private static Model redCar1, redCar2;
  private static Model yellowCar1, yellowCar2;

  public CityScene()
  {
    SketchUp skp = new SketchUp();
    skp.setListener(this);
    skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
    skp.startSketchUp();
    createTerrain();
    setCam(new Point3d(261, -6, 715), new Point3d(261, 18, 624));
    //addBuildings();
    addDynamicModels();
    startAnimations();
  }

  
  static boolean createTerrain()
  {   //generate flat terrain and apply street texture on it
    try {
      terrain = new TerrainConstructor(0, 3, 50);
      terrain.setLevel(1);
      terrain.setTexture(1, "street2.jpg", 1);
      terrain.texture();
      return true;
    } catch (IOException ex) {
      System.err.println("IOException: " + ex.getMessage());
    }
    return false;
  }//end of createTerrain()

  
  private static boolean addBuildings()
  {
    //create multiple blocks and cylinders as buildings and add to the terrain
    Block b1 = new Block(new Point2f(25, 25), 50, 50, 70);
    Block b2 = new Block(new Point2f(75, 25), 50, 50, 90);
    Block b3 = new Block(new Point2f(125, 25), 50, 50, 70);
    Block b4 = new Block(new Point2f(175, 25), 50, 50, 100);
    Block b5 = new Block(new Point2f(275, 25), 50, 50, 90);
    Cylinder b6 = new Cylinder(new Point2f(325, 25), 25, 130);
    Cylinder b7 = new Cylinder(new Point2f(25, 125), 25, 150);
    Cylinder b8 = new Cylinder(new Point2f(75, 125), 25, 130);
    Block b9 = new Block(new Point2f(125, 125), 50, 50, 90);
    Block b10 = new Block(new Point2f(175, 125), 50, 50, 70);
    Block b11 = new Block(new Point2f(275, 125), 50, 50, 90);
    Block b12 = new Block(new Point2f(325, 125), 50, 50, 70);
    Block b13 = new Block(new Point2f(25, 175), 50, 50, 110);
    Block b14 = new Block(new Point2f(75, 175), 50, 50, 90);
    Cylinder b15 = new Cylinder(new Point2f(125, 175), 25, 130);
    Cylinder b16 = new Cylinder(new Point2f(175, 175), 25, 150);
    Cylinder b17 = new Cylinder(new Point2f(275, 175), 25, 130);
    Block b18 = new Block(new Point2f(325, 175), 50, 50, 90);
    Block b19 = new Block(new Point2f(25, 275), 50, 50, 70);
    Block b20 = new Block(new Point2f(75, 275), 50, 50, 110);
    Block b21 = new Block(new Point2f(125, 275), 50, 50, 90);
    Cylinder b22 = new Cylinder(new Point2f(175, 275), 25, 130);
    Cylinder b23 = new Cylinder(new Point2f(275, 275), 25, 170);
    Cylinder b24 = new Cylinder(new Point2f(325, 275), 25, 130);

    terrain.addShape(b1);
    terrain.addShape(b2);
    terrain.addShape(b3);
    terrain.addShape(b5);
    terrain.addShape(b6);
    terrain.addShape(b7);
    terrain.addShape(b8);
    terrain.addShape(b9);
    terrain.addShape(b11);
    terrain.addShape(b12);
    terrain.addShape(b13);
    terrain.addShape(b14);
    terrain.addShape(b15);
    terrain.addShape(b17);
    terrain.addShape(b18);
    terrain.addShape(b19);
    terrain.addShape(b20);
    terrain.addShape(b21);
    terrain.addShape(b23);
    terrain.addShape(b24);

    b1.texture("bld1.jpg");
    b2.texture("bld2.jpg");
    b3.texture("bld3.jpg");
    b5.texture("bld2.jpg");
    b6.texture("bld4.jpg");
    b7.texture("bld6.jpg");
    b8.texture("bld4.jpg");
    b9.texture("bld7.jpg");
    b11.texture("bld1.jpg");
    b12.texture("bld2.jpg");
    b13.texture("bld3.jpg");
    b14.texture("bld5.jpg");
    b15.texture("bld2.jpg");
    b17.texture("bld6.jpg");
    b18.texture("bld4.jpg");
    b19.texture("bld7.jpg");
    b20.texture("bld1.jpg");
    b21.texture("bld2.jpg");
    b23.texture("bld5.jpg");
    b24.texture("bld2.jpg");
    //terrain.loadStatics("data.txt");
    return true;
  }//end of addBuildings()

  
  private static boolean addDynamicModels()
  {
    //add movable cars into the scene including two red cars and two yellow cars
    redCar1 = new Model("redCar.skp", new Point2f(125, 75), (float) 0.2, 0, true);
    redCar2 = new Model("redCar.skp", new Point2f(375, 375), (float) 0.2, 270, true);
    yellowCar1 = new Model("yellowCar.skp", new Point2f(225, 275), 0, (float) 0.25, 270, true);
    yellowCar2 = new Model("yellowCar.skp", new Point2f(375, 225), 0, (float) 0.25, 270, true);
    terrain.addModel(redCar1);
    terrain.addModel(redCar2);
    terrain.addModel(yellowCar1);
    terrain.addModel(yellowCar2);
    return true;
  }//end of addDynamicModels()

  
  private static boolean startAnimations()
  {
    Step[] yellowCar1Steps = terrain.genStepsArray(new Point2f(225, 275), new Point2f(225, 25), 0, 6);
    Step[] yellowCar2Steps = terrain.genStepsArray(new Point2f(375, 225), new Point2f(375, 25), 0, 6);
    Step[] redCar1Steps = terrain.genStepsArray(new Point2f(125, 75), new Point2f(375, 75), 0, 8);
    Step[] redCar2Steps = terrain.genStepsArray(new Point2f(375, 375), new Point2f(375, 25), 0, 6);
    redCar1.setAnimation(redCar1Steps);
    redCar2.setAnimation(redCar2Steps);
    yellowCar1.setAnimation(yellowCar1Steps);
    yellowCar2.setAnimation(yellowCar2Steps);
    terrain.startAnimation();
    return true;
  }//end of startAnimations()

  
  public void collision(CollisionEvent e)
  {
    //response to a collision by having souce model waits for two seconds before continue moving
    int collisionTime = e.getTime();
    int waitTime = 2;
    genSourceSteps(e, waitTime);
    terrain.startAnimationAt(collisionTime);
  }//end of collision()

  
  private void genSourceSteps(CollisionEvent e, int waitTime)
  {
    Step[] sourceSteps1, sourceSteps2;
    Step[] oldSteps = e.getSourceStep();
    int index = 0;
    int time = e.getTime();
    Point2f p0 = e.getSourcePos();
    sourceSteps1 = terrain.genStepsArray(p0, p0, time, time + waitTime);
    index = CollisionRecovery.findClosestPoint(p0, oldSteps);
    sourceSteps2 = CollisionRecovery.extractArray(index, time + waitTime, oldSteps);
    Model source = terrain.getModel(e.getSourceName());
    source.setAnimation(sourceSteps1);
    source.setAnimation(sourceSteps2);
  }//end of genSourceSteps()

  
  public void finish(FinishEvent e)
  {System.out.println("finished");}

  
  public static void main(String args[])
  {new CityScene();}

}
