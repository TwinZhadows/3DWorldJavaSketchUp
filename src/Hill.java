/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileNotFoundException;
import java.io.IOException;
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
public class Hill {
  
    //private static CameraWrapper cam = new CameraWrapper();
    
    public static void main(String args[]) throws IOException {
        
        
        //Initiate SketchUp
        SketchUp skp = new SketchUp();
        skp.startSketchUp();
        
        
        //generate the terrain
        int size = 4; //terrain dimesion
        int plgSize = 30;//polygon's width
        String terrainFile = "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/file.txt"; 
        TerrainConstructor terrain = new TerrainConstructor(false, size, plgSize, terrainFile);   
      /*
        //set the camera
        Point3d eye = new Point3d(172.017, -265.869, 473.955);
        Point3d target = new Point3d(-10.9832, 89.1315, 86.955);
        terrain.setCam(eye, target);
      */  
        //texture the terrain
        int level = 3;
        terrain.setLevel(level);
                
        String darkSand = "sand2.jpg";
        String sand = "sand.jpg";   
        String grass = "grass.jpg";
        terrain.setTexture(1, darkSand, 20);
        terrain.setTexture(2, sand, 20);
        terrain.setTexture(3, grass, 60);
        terrain.texture();
      /*
        //generate and add shapes
        int radius = 5;
        int height = 30;
        int shScale = 1;
        int shRotation = 0;
        Point2f shPosition = new Point2f(150,150);
        Cylinder cylinder = new Cylinder("c1", shPosition,shScale,height);
      
        Block block = new Block("b1", new Point2f(200,200),10,10,30);
        terrain.addShape(block);
        terrain.addShape(cylinder);
        cylinder.texture("roof.jpg","bld2.jpg");
      */  
       
        //add trees to the terrain
        String treeFile = "tree.skp";
        Point2f tPosition = new Point2f(100,100);
        float treeScale = 1;
        float treeRotation = 0;      
        Model tree = new Model("tree1",treeFile, tPosition, treeScale, treeRotation, false);
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
       //generate movement steps
        Step[] movStep = null;
        Point2f start = new Point2f(50, 50);    
        Point2f dest = new Point2f(100, 100); 
        float rotation = 45;
        double dAngle = 90;
        int step = 10;
        Movements mov = new MovementWrapper();
        try {
            movStep = mov.genStepsArray(terrain, start, dest, rotation, dAngle, 0, 15);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Hill.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MovementException ex) {
            Logger.getLogger(Hill.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        //add the user model
        String man = "man.skp";      
        float scale = (float) 0.3;
        UserWrapper user = new UserWrapper(man, start, scale, rotation);   
        user.setAnimation(movStep, false);
        terrain.addUserModel(user);
    /*    
        //animate the user model accordingly to the movement steps
        for(int i = 0; i< movStep.length; i++ ){
            user.animate(movStep[i]);
        }
     */
    }
}
