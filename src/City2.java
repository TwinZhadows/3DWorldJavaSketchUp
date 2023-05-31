/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Event;
import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.Scanner;
import javax.vecmath.Point2f;

/**
 *
 * @author chon
 */
public class City2 {
  
    
        static TerrainConstructor terrain;
        static ArrayList<Model> models = new ArrayList<Model>();
    
    public static void main(String args[]) throws IOException, MovementException {
        
        //listener

        //Initiate SketchUp
        
        SketchUp skp = new SketchUp();
        skp.startSketchUp();
        
      
        /*generate the terrain
        TerrainWrapper(isFlat, size, polygonSize, file);
        */
        String terrainFile = "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/file.txt"; 
        terrain = new TerrainConstructor(true, 3, 25, terrainFile);   
        //terrain.setListener(lst);
        //set the camera
        //Point3d eye = new Point3d(100, -40, 20);
        //Point3d target = new Point3d(100, 30, 0);
        Point3d eye = new Point3d(105, -115, 137);
        Point3d target = new Point3d(105, -57, 94);        
        terrain.setCam(eye, target);
        
        //texture the terrain
        
        terrain.setLevel(1);
                
        String street = "street.jpg";
           
        terrain.setTexture(1, street, 1);
       
        terrain.texture();
    
        //generate and add buildings
        Block b1 = new Block("b1", new Point2f(50,100),50,50,70);
        Block b2 = new Block("b2", new Point2f(150,100),50,50,60);
        Block b3 = new Block("b3", new Point2f(100,50),50,50,50);
        Cylinder b4 = new Cylinder("b4", new Point2f(100,100),25,100);
        Cylinder b5 = new Cylinder("b5", new Point2f(150,50),25,50);
        /*Block b1 = new Block("b1", new Point2f(25,25),50,50,100);
        Block b2 = new Block("b2", new Point2f(75,25),50,50,120);
        Block b3 = new Block("b3", new Point2f(125,25),50,50,100);
        Block b4 = new Block("b4", new Point2f(25,75),50,50,140);
        Block b5 = new Block("b5", new Point2f(75,75),50,50,120);
        Cylinder b6 = new Cylinder("b6", new Point2f(125,75),25,160);
        Cylinder b7 = new Cylinder("b7", new Point2f(25,175),25,200);
        Cylinder b8 = new Cylinder("b8", new Point2f(75,175),25,160);
        Block b9 = new Block("b9", new Point2f(125,175),50,50,120);
       */
        terrain.addShape(b1);
        terrain.addShape(b2);
        terrain.addShape(b3);
        terrain.addShape(b4);
        terrain.addShape(b5);
        //terrain.addShape(b6);
        //terrain.addShape(b7);
        //terrain.addShape(b8);
        //terrain.addShape(b9);
        
        b1.texture("roof.jpg","bld1.jpg");
        b2.texture("roof.jpg","bld2.jpg");
        b3.texture("roof.jpg","bld3.jpg");
        b4.texture("roof.jpg","bld5.jpg");
        b5.texture("roof.jpg","bld2.jpg");
        //b6.texture("roof.jpg","bld4.jpg");
        //b7.texture("roof.jpg","bld6.jpg");
        //b8.texture("roof.jpg","bld4.jpg");
        //b9.texture("roof.jpg","bld7.jpg");
    
/*
  
       //generate movement steps
        Step[] movStep, movStep2, movStep3, movStep4;
        Point2f start = new Point2f(90, 30);   //100,30 
        Point2f dest = new Point2f(90, 150); 
        float rotation = 90;
        double dAngle = 90;
        int step = 10;
        
        //movStep = new MovementWrapper().genMovements(terrain, start, dest, rotation, dAngle, 0, 10);
        //movStep2 = new MovementWrapper().genMovements(terrain, new Point2f(100, 100), new Point2f(100, 100), 0, -90, 0, 5);
        //movStep3 = new MovementWrapper().genMovements(terrain, new Point2f(100, 100), new Point2f(100, 50), -90, -90, 6, 10);
        //movStep4 = new MovementWrapper().genMovements(terrain, new Point2f(100, 50), new Point2f(100, 50), -90, 0, 11, 15);
        /*add the car model
        new Model(modelName, file, position, scale, rotation, isDynamic)
        
        Model car = new Model ("car","car2.skp", new Point2f(170,175), (float) 0.6, -90, true);
        Model car2 = new Model ("car","car2.skp", new Point2f(190,175), (float) 0.6, -90, true);
        terrain.addModel(car);
        terrain.addModel(car2);
        //car.setAnimation(movStep2, false);
        //car.setAnimation(movStep3, true);
        //car.setAnimation(movStep4, true);
        //car.loadAnimation();
        models.add(car);
        models.add(car2);
        UserWrapper user = new UserWrapper("car.skp", new Point2f(175, 30), (float)0.2, rotation);
        
        terrain.addUserModel(user);
        //user.setAnimation(movStep, false);
        //user.loadAnimation();
        //animate the car accordingly to the movement steps       
        models.add(user);
        //terrain.startAnimation();
*/
    }

}
