/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Point3d;
import javax.vecmath.Point2f;
import static terrain2.Ocean.terrain;


/**
 *
 * @author chon
 */
public class OceanScene extends Scene {

    private TerrainConstructor terrain;
    private UserWrapper user;
    private Model helicopter;

   
    public OceanScene() 
    {
        //Initiate SketchUp
        SketchUp skp = new SketchUp();
        //skp.setListener(this);
        skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
        skp.startSketchUp();
        setCam(new Point3d(71, -144, 168), new Point3d(94, 630, -406));
        createTerrain();      
        addModels();
      
        //startAnimations();
    }

    
    private void createTerrain() 
    {   
        //create ocean with iceberg from a heightmap and apply water and ice texture 
        try {
            terrain = new TerrainConstructor("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/ocean.png", 100, 10);
            terrain.setLevel(2);
            terrain.setTexture(1, "water.jpg", 5);
            terrain.setTexture(2, "ice.jpg", 95);
            terrain.texture();
        } catch (IOException ex) {
            Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//end of createTerrain()
    
    private void addModels() 
    {
        //add a static helicopter and a dynamic ship to the scene   
        user = new UserWrapper("lpd.skp", new Point2f(300, 300), 30, (float)0.05, 45);
        helicopter = new Model ("MD902_4.skp", new Point2f(200,350), 30, (float) 0.3, 60, true);        
        terrain.addModel(helicopter);
        terrain.addUserModel(user);
    }//end of addStaticModels()

    
    private void startAnimations() 
    {
        //create and start animations
        //no animation 
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
        new OceanScene();       
    }

}
