/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point2f;


/**
 *
 * @author chon
 */
public class Ocean implements AnimationListener{
  
    static TerrainConstructor terrain;
    static ArrayList<Model> models = new ArrayList<Model>();
    public static void main(String args[]) throws IOException, MovementException {
        
        
        //Initiate SketchUp
        SketchUp skp = new SketchUp();
        skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
        skp.startSketchUp();
        
      
        //generate the terrain
        int size = 3; //terrain dimesion
        int plgSize = 10;//polygon's width
        String terrainFile = "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/file.txt"; 
        //terrain = new TerrainWrapper(false, 3, plgSize);   
        try {
            terrain = new TerrainConstructor("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/ocean.png", 100, plgSize);
        } catch (IOException ex) {
            Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //set the camera
        //Point3d eye = new Point3d(100, -40, 20);
        //Point3d target = new Point3d(100, 30, 0);
        Point3d eye = new Point3d(71, -144, 168);
        Point3d target = new Point3d(94, 630, -406);
        terrain.setCam(eye, target);
        
     //texture the terrain
        //int level = 1;
        //terrain.setLevel(level);
                
        //String street = "street.jpg";

        //terrain.setTexture(1, street, 1);
       
        int level = 2;
        terrain.setLevel(2);               
        String ice = "ice.jpg";
        String water = "water.jpg";   
      
        terrain.setTexture(1, "water.jpg",5);
        terrain.setTexture(2, "ice.jpg", 95);
        terrain.texture();
        
        
        //Block b1 = new Block(new Point2f(25,125), -50, 50,50,100);
        //Block b2 = new Block(new Point2f(25,175), -50, 50, 50,120);
        //Block b3 = new Block("b3", new Point2f(125,25),50,50,100);
        //terrain.addShape(b1);
        //terrain.addShape(b2);
        //terrain.addShape(b3);
       // b1.texture("roof.jpg","bld1.jpg");
        //b2.texture("roof.jpg","bld2.jpg");
        //b3.texture("roof.jpg","bld3.jpg");
        //terrain.texture();
    /* 
        //generate and add buildings
   /*     
        Block b1 = new Block("b1", new Point2f(25,25),50,50,100);
        Block b2 = new Block("b2", new Point2f(75,25),50,50,120);
        Block b3 = new Block("b3", new Point2f(125,25),50,50,100);
        Block b4 = new Block("b4", new Point2f(25,75),50,50,140);
        Block b5 = new Block("b5", new Point2f(75,75),50,50,120);
        Cylinder b6 = new Cylinder("b6", new Point2f(125,75),25,160);
        Cylinder b7 = new Cylinder("b7", new Point2f(25,175),25,200);
        Cylinder b8 = new Cylinder("b8", new Point2f(75,175),25,160);
        Block b9 = new Block("b9", new Point2f(125,175),50,50,120);
        
        terrain.addShape(b1);
        terrain.addShape(b2);
        terrain.addShape(b3);
        terrain.addShape(b4);
        terrain.addShape(b5);
        terrain.addShape(b6);
        terrain.addShape(b7);
        terrain.addShape(b8);
        terrain.addShape(b9);
        
        b1.texture("roof.jpg","bld1.jpg");
        b2.texture("roof.jpg","bld2.jpg");
        b3.texture("roof.jpg","bld3.jpg");
        b4.texture("roof.jpg","bld5.jpg");
        b5.texture("roof.jpg","bld2.jpg");
        b6.texture("roof.jpg","bld4.jpg");
        b7.texture("roof.jpg","bld6.jpg");
        b8.texture("roof.jpg","bld4.jpg");
        b9.texture("roof.jpg","bld7.jpg");
    
*/
 
       //generate movement steps
        Step[] movStep, movStep2, movStep3;
        Point2f start = new Point2f(20, 20);    
        Point2f dest = new Point2f(100, 100); 
        float rotation = 90;
        double dAngle = 90;
        int step = 10;
      
        //movStep = new MovementWrapper().genStepsArray(terrain, start, dest, 0, 0, 5, 15);
        //movStep2 = new MovementWrapper().genStepsArray(terrain, new Point2f(100, 100), new Point2f(50, 50), 0, 0, 0, 10);
        //movStep3 = new MovementWrapper().genStepsArray(terrain, new Point2f(100, 100), new Point2f(50, 50), 0, 0, 0, 10);
        
        //add user model
        UserWrapper user = new UserWrapper("lpd.skp", new Point2f(300, 300), 30, (float)0.05, 45);
        
        terrain.addUserModel(user);
       // user.setAnimation(movStep);
        user.loadAnimation();
        models.add(user);
        //add the car model
        
        Model copter = new Model ("MD902_4.skp", new Point2f(200,350), 30, (float) 0.3, 60, true); 
        
        terrain.addModel(copter);
        //copter.setAnimation(movStep2);
        copter.loadAnimation();
        models.add(copter);
            
        terrain.setListener((AnimationListener) new Ocean());
        //animate the car accordingly to the movement steps
        //terrain.startAnimation(0);

     
    }
    
    public void finish(FinishEvent e) {
        System.out.println("finished");
    }

    public void collision(CollisionEvent e) {
        CollisionRecovery.basicRecovery(terrain, e);
    }
}
