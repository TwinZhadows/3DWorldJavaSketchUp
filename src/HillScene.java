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
public class HillScene extends Scene {

    private TerrainConstructor terrain;
    private UserWrapper user;

   
    public HillScene() 
    {
        //Initiate SketchUp
        SketchUp skp = new SketchUp();
        //skp.setListener(this);
        skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
        skp.startSketchUp();
        setCam(new Point3d(172, -265, 473), new Point3d(-10, 89, 86));
        createTerrain();      
        addTrees();
        addDynamicModels();        
        //startAnimations();
    }

    
    private void createTerrain() 
    {   
        //create rough terrain (depth = 10) and apply water and sand texture
        try {
            terrain = new TerrainConstructor(10, 4, 30);   
            terrain.setLevel(3);
            terrain.setTexture(1, "grass.jpg",50);
            terrain.setTexture(2, "sand2.jpg", 25);
            terrain.setTexture(3, "sand.jpg", 25);
            terrain.texture();
            terrain.enableShadow();
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }//end of createTerrain()
    
    private void addTrees() 
    {
        //add multiple trees to the scene
        String treeFile = "tree.skp";
        Model tree = new Model("tree1",treeFile, new Point2f(100,100), 1, 0, false);
        Model tree2 = new Model("tree2",treeFile, new Point2f(100,150), (float)1.2, 0, false);
        Model tree3 = new Model("tree3",treeFile, new Point2f(140,80), (float)1.2, 0, false);
        Model tree4 = new Model("tree4",treeFile, new Point2f(140,150), (float) 1.5, 0, false);
        Model tree5 = new Model("tree5",treeFile, new Point2f(200,200), (float) 1.5, 0, false);
        Model tree6 = new Model("tree6",treeFile, new Point2f(200,250), (float) 1.5, 0, false);
        Model drone = new Model("drone","drone.skp", new Point2f(50,100), (float)0.3, 0, false);
        terrain.addModel(tree);
        terrain.addModel(tree2);
        terrain.addModel(tree3);
        terrain.addModel(tree4);
        terrain.addModel(tree5);
        terrain.addModel(tree6);
        terrain.addModel(drone);
    }//end of addStaticModels()

    
    private void addDynamicModels() 
    {
        //add a floating frone as a user model on the terrain
        user = new UserWrapper("man.skp", new Point2f(50, 50), (float) 0.3, 45);        
        terrain.addUserModel(user);
    }//end of addDynamicModels
    
    
    private void startAnimations() 
    {
        //create and start animations
        terrain.enableShadow();
        try {
            Point2f start = new Point2f(50, 50);    
            Point2f dest = new Point2f(100, 100);    
            Step[] userSteps = terrain.genStepsArray(start, dest, 45, 90, 0, 5);

            user.setAnimation(userSteps);
            terrain.startAnimation();
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
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
        new HillScene();       
    }

}
