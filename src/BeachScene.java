/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;

import javax.vecmath.Point3d;
import javax.vecmath.Point2f;


/**
 *
 * @author chon
 */
public class BeachScene extends Scene {

    private TerrainConstructor terrain;
    private UserWrapper user;

   
    public BeachScene() 
    {
        //Initiate SketchUp
        SketchUp skp = new SketchUp();
        //skp.setListener(this);
        skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
        skp.startSketchUp();
        setCam(new Point3d(172, -265, 473), new Point3d(-10, 89, 86));
        createTerrain();      
        addStaticModels();
        addDynamicModels();        
        //startAnimations();
    }

    
    private void createTerrain() 
    {   
        //create rough terrain (depth = 10) and apply water and sand texture
        try {
            terrain = new TerrainConstructor(10, 4, 30);   
            terrain.setLevel(3);
            terrain.setTexture(1, "water.jpg",50);
            terrain.setTexture(2, "sand2.jpg", 25);
            terrain.setTexture(3, "sand.jpg", 25);
            terrain.texture();
            terrain.enableShadow();
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }//end of createTerrain()
    
    private void addStaticModels() 
    {
        //add scenery models to the scene including beach chairs and palm tree    
        //Model chair = new Model("beachChair.skp", "chair1", new Point2f(200,200), (float) 0.3, -45, false);        
        //Model chair2 = new Model("beachChair.skp", "chair2", new Point2f(210,360), (float) 0.3, 45, false);
        //Model palmTree = new Model("palmTree.skp", "palmtree", new Point2f(180,300), (float) 1.0, 180, false);
       // terrain.addModel(chair);
        //terrain.addModel(chair2);
        //terrain.addModel(palmTree);
    }//end of addStaticModels()

    
    private void addDynamicModels() 
    {
        //add a floating frone as a user model on the terrain
        user = new UserWrapper("drone.skp", new Point2f(200, 30), (float) 0.3, 90);        
        terrain.addUserModel(user);
    }//end of addDynamicModels
    
    
    private void startAnimations() 
    {
        //create and start animations
        terrain.enableShadow();
        Point2f start = new Point2f(200, 30);
        Point2f dest = new Point2f(200, 200);
        Step[] userSteps = terrain.genStepsArray(start, dest, 90, 180, 0, 5);
        user.setAnimation(userSteps);
        terrain.startAnimation();
    }//end of startAnimations()

/*    
    public void collision(CollisionEvent e) 
    {
        int time = e.getTime();
        int offset = 20;
        genSourceSteps(e, offset);
        genTargetSteps(e, offset);
        terrain.startAnimationAt(time);
    }//end of collision()
    
    private void genTargetSteps(CollisionEvent e, int offset) 
    {
        //target parameters
        Point2f targetPosition = e.getTargetPos();
        float targetAngle = e.getTargetAngle();
        int time = e.getTime();
        String targetName = e.getTargetName();
        //generate new target animation            
        recoverCollision(targetName, time, targetPosition, targetAngle, offset);
    }//end of genTargetSteps()

    
    private void genSourceSteps(CollisionEvent e, int offset) 
    {
        //source parameters      
        float angle = e.getSourceAngle();
        int time = e.getTime();
        Point2f position = e.getSourcePos();
        String sourceName = e.getSourceName();
        //source animation   
        recoverCollision(sourceName, time, position, angle, offset);

    }//end pf genSourceSteps()

    
    private void recoverCollision(String modelName, int time, Point2f position, float angle, int offset) 
    {
        
        // recover collision of the model accordingly to given name. the model will turn right for 90 degrees and move along for offset.
        
        try {

            Step[] steps1, step2;
            Point2f p1;
            float x, y;
            x = (float) (position.x + offset * Math.cos(Math.toRadians(90 - angle)));
            y = (float) (position.y - offset * Math.sin(Math.toRadians(90 - angle)));
            p1 = new Point2f(x, y);
            steps1 = terrain.genStepsArray(position, position, 0, -90, time, time + 1);
            step2 = terrain.genStepsArray(position, p1, time + 1, time + 2);

            Model target = terrain.getModel(modelName);
            target.setAnimation(steps1);
            target.setAnimation(step2);

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }
    //end of recoverCollision()
    
    
    public void finish(FinishEvent e) 
    {
        System.out.println("finished");
    }
 */
    
    public static void main(String args[])
    {
        new BeachScene();       
    }

}
