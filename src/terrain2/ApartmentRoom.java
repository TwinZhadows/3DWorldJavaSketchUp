package terrain2;

import java.io.IOException;
import javax.vecmath.Point3d;
import javax.vecmath.Point2f;


/**
 * @author chon
 * Create apartment room containing static furniture, dynamic robots, and user model
 * A collision is handled by making each model rotating right and moving straight for certain distance
 */
public class ApartmentRoom extends Scene implements AnimationListener {

  private TerrainConstructor terrain;
  private Model crunchBot, cleaningBot, kiwiBot;

   
  public ApartmentRoom() 
  {
    //initiate SketchUp and Ruby console
    SketchUp skp = new SketchUp();
    skp.setListener(this);
    skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
    skp.startSketchUp();
    //construct 3D apartment room
    setCam(new Point3d(673, -20, 307), new Point3d(614, 6, 269));
    createTerrain();      
    //addWalls();
    //addStaticModels();
    addDynamicModels();      
    //create and start animation
    startAnimations();
  }//end of ApartmentRoom()

    
  private void createTerrain() 
  {   
    //generate flat terrain(450*450) with single texture level, apply a floor texture, and enable shadow
    try {
       terrain = new TerrainConstructor(0, 3, 50);
       terrain.setLevel(1);
       terrain.setTexture(1, "floor.jpg");
       terrain.texture();
       terrain.enableShadow();
    } catch (IOException ex) {
       System.err.println("IOException: " + ex.getMessage());
    }
  }//end of createTerrain()
    
  private void addStaticModels() 
  {
    //create and add static scenery models to the scene including sofa, bed, coffee table, tv, curtain, bookshelf, and picture
    //require String file, Point2f p, int offsetZ(optional), float scale, float angle, boolean movable
    Model sofa = new Model("sofa.skp", new Point2f(150, 150), (float) 1, 0,false);
    Model bed = new Model("bed.skp", new Point2f(100, 300), (float) 1.2, 90, false);
    Model coffeeTable = new Model("coffee table.skp", new Point2f(150, 90), (float) 1, 0, false);
    Model tv = new Model("tv.skp", new Point2f(150, 20), (float) 1, 180, false);
    Model curtain = new Model("curtain.skp", new Point2f(35, 380), (float) 1, -90, false);
    Model bookShelf = new Model("bookshelf.skp", new Point2f(300, 390), (float) 1, 0, false);
    Model picture = new Model("picture.skp", new Point2f(5, 200), 20, (float) 1.5, 90, false);
    terrain.addModel(sofa);
    terrain.addModel(bed);
    terrain.addModel(coffeeTable);
    terrain.addModel(tv);
    terrain.addModel(bookShelf);
    terrain.addModel(curtain);
    terrain.addModel(picture);
  }//end of addStaticModels()

    
  private void addDynamicModels() 
  {
    //add moving models on the room floor including crunchBot, cleaningBot, and kiwiBot(user model)
    crunchBot = new Model("bigCrunch.skp", new Point2f(200, 80), (float) 0.05, 45, true);
    cleaningBot = new Model("cleanBot.skp", new Point2f(300, 150), (float) 0.3, 30, true);
    kiwiBot = new UserWrapper("kiwiBot.skp", new Point2f(200, 350), (float) 3, 270);
    terrain.addModel(crunchBot);
    terrain.addModel(cleaningBot);
    terrain.addModel(kiwiBot);
  }//end of addDynamicModels()

    
  private void addWalls() 
  {
    //create flat blocks as room's walls
    //Block constructor requires 2D position, width, length, and height 
    Block wall1 = new Block(new Point2f(200, 400), 400, 3, 100);
    Block wall2 = new Block(new Point2f(0, 200), 3, 400, 100);
    terrain.addShape(wall1);
    terrain.addShape(wall2);
    wall1.texture("sky2.jpg");
    wall2.texture("black.jpg");
  }//end of addWalls()
    
    
  private void startAnimations() 
  {
    //create steps arrays for dynamic models, disable shadow, and start animations
    terrain.enableShadow();
    Point2f p1, p2, p3, p4, p5, p6, p7;
    p1 = new Point2f(200, 350);
    p2 = new Point2f(200, 300);
    p3 = new Point2f(330, 170);
    p4 = new Point2f(200, 80);
    p5 = new Point2f(310, 190);
    p6 = new Point2f(300, 150);
    p7 = new Point2f(370, 150);
    Step[] kiwiSteps1 = terrain.genStepsArray(p1, p2, 0, 3);
    Step[] kiwiSteps2 = terrain.genStepsArray(p2, p2, 0, 45, 3, 5);
    Step[] kiwiSteps3 = terrain.genStepsArray(p2, p3, 5, 13);
    kiwiBot.setAnimation(kiwiSteps1);
    kiwiBot.setAnimation(kiwiSteps2);
    kiwiBot.setAnimation(kiwiSteps3);
    Step[] crunchBotSteps1 = terrain.genStepsArray(p4, p5, 0, 10);
    crunchBot.setAnimation(crunchBotSteps1);
    Step[] cleanBotSteps1 = terrain.genStepsArray(p6, p6, 0, -30, 0, 3);
    Step[] cleanBotSteps2 = terrain.genStepsArray(p6, p7, 3, 10);
    cleaningBot.setAnimation(cleanBotSteps1);
    cleaningBot.setAnimation(cleanBotSteps2);
    terrain.startAnimation();
  }//end of startAnimations()

    
  public void collision(CollisionEvent e) 
  { /*
     extract collision time and then update the steps array of source and target 
     by calling genSourceSteps and genTargetSteps, before resume animations at collision time
    */
    int time = e.getTime();
    int offset = 20;
    genSourceSteps(e, offset);
    genTargetSteps(e, offset);
    terrain.startAnimationAt(time);
  }//end of collision()
    
    
  private void genTargetSteps(CollisionEvent e, int offset) 
  {   
    /*
      extract collision target parameters (position, rotation, time, and name) and 
      then call recoverCollision() in order to update its steps aray        
    */
    Point2f targetPosition = e.getTargetPos();
    float targetAngle = e.getTargetAngle();
    int time = e.getTime();
    String targetName = e.getTargetName();          
    recoverCollision(targetName, time, targetPosition, targetAngle, offset);
  }//end of genTargetSteps()

    
  private void genSourceSteps(CollisionEvent e, int offset) 
  {
    /*
     extract collision source parameters (position, rotation, time, and name) and 
     then call recoverCollision() in order to update its steps aray        
    */      
    float angle = e.getSourceAngle();
    int time = e.getTime();
    Point2f position = e.getSourcePos();
    String sourceName = e.getSourceName();  
    recoverCollision(sourceName, time, position, angle, offset);
  }//end pf genSourceSteps()

    
  private void recoverCollision(String modelName, int time, Point2f position, float angle, int offset) 
  {
    Step[] steps1, steps2;
    Point2f p1;
    float x, y;
    x = (float) (position.x + offset * Math.cos(Math.toRadians(90 - angle)));
    y = (float) (position.y - offset * Math.sin(Math.toRadians(90 - angle)));
    p1 = new Point2f(x, y);
    steps1 = terrain.genStepsArray(position, position, 0, -90, time, time + 1);
    steps2 = terrain.genStepsArray(position, p1, time + 1, time + 2);
    Model target = terrain.getModel(modelName);
    target.setAnimation(steps1);
    target.setAnimation(steps2);
  }//end of recoverCollision()
    
    
  public void finish(FinishEvent e) 
  { System.out.println("All animations finished"); }
 
    
  public static void main(String args[])
  { new ApartmentRoom(); }
}//end of ApartmentRoom
