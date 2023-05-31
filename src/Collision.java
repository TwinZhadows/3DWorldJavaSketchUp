/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.Scanner;
import javax.vecmath.Point2f;
import static terrain2.City.models;

/**
 *
 * @author chon
 */
public class Collision implements AnimationListener{
  
    static TerrainConstructor terrain;
    static ArrayList<Model> models = new ArrayList<Model>();
    
    
    public static void main(String args[]) throws IOException, MovementException {
        
        
        //Initiate SketchUp
        SketchUp skp = new SketchUp();
        
        skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
        skp.startSketchUp();
        //generate the terrain
        int size = 3; //terrain dimesion
        int plgSize = 25;//polygon's width
        String terrainFile = "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/file.txt"; 
        terrain = new TerrainConstructor(0, size, plgSize);   
      
        //set the camera
        //Point3d eye = new Point3d(100, -40, 20);
        //Point3d target = new Point3d(100, 30, 0);
        Point3d eye = new Point3d(125, -141, 260);
        Point3d target = new Point3d(129, 515, -446);
        //terrain.setCam(eye, target);
        
        //texture the terrain
        int level = 1;
        terrain.setLevel(level);
                
        String street = "street.jpg";

        terrain.setTexture(1, street, 1);
       
        terrain.texture();
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
        Point2f start = new Point2f(50, 100);    
        Point2f dest = new Point2f(150, 100); 
        float rotation = 90;
        double dAngle = 90;
        int step = 10;
      
        movStep = new MovementWrapper().genStepsArray(terrain, start, dest, rotation, dAngle, 5, 15);
        movStep2 = new MovementWrapper().genStepsArray(terrain, new Point2f(180, 100), new Point2f(40, 100), rotation, 90, 0, 10);
        movStep3 = new MovementWrapper().genStepsArray(terrain, new Point2f(180, 10), new Point2f(0, 10), rotation, 90, 0, 10);
        //add user model
        UserWrapper user = new UserWrapper("car.skp", new Point2f(50, 100),0, (float)0.2, 0);
        
        terrain.addUserModel(user);
        user.setAnimation(movStep);
        user.loadAnimation();
        models.add(user);
        //add the car model
        
        Model car = new Model ("car","car.skp", new Point2f(180,100), 0,(float) 0.2, 180, true); 
        
        terrain.addModel(car);
        car.setAnimation(movStep2);
        car.loadAnimation();
        models.add(car);

        Model car2 = new Model ("car2","car.skp", new Point2f(180,10),0, (float) 0.2, 180, true); 
      /*  
        terrain.addModel(car2);
        car2.setAnimation(movStep2, false);
        car2.loadAnimation();
        models.add(car2);
      */  
        //animate the car accordingly to the movement steps
        terrain.setListener(new Collision());
        terrain.startAnimationAt(0);

     
    }

    @Override
    public void finish(FinishEvent e) {
        System.out.println("finished");
    }

    @Override
    public void collision(CollisionEvent e) {
        CollisionRecovery.basicRecovery(terrain, e);
    }
}
