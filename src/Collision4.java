/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point2f;
import static terrain2.City.models;
import static terrain2.Collision.terrain;
import static terrain2.CollisionRecovery.modelStep;

/**
 *
 * @author chon
 */
public class Collision4 implements AnimationListener{
  
    static TerrainConstructor terrain;
    public static void main(String args[]){
        
        

        try {
            
            
            //Initiate SketchUp
            SketchUp skp = new SketchUp();
            skp.setListener(new Collision4());
            skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
            skp.startSketchUp();
            
            //generate the terrain
            int size = 3; //terrain dimesion
            int plgSize = 25;//polygon's width
            String terrainFile = "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/file.txt";
            /* try {
            terrain = new TerrainWrapper("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/height64.png", 100, plgSize);
            } catch (IOException ex) {
            Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
            try {
                terrain = new TerrainConstructor(0, size, plgSize);
            } catch (IOException ex) {
                Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //set the camera
            
            Point3d eye = new Point3d(100, -40, 20);
            Point3d target = new Point3d(100, 30, 0);
            //Point3d eye = new Point3d(71, -144, 168);
            
            //Point3d target = new Point3d(94, 630, -406);
            //terrain.setCam(eye, target);
            
            //texture the terrain
            int level = 1;
            terrain.setLevel(level);
            
            String street = "street.jpg";
            
            try {
                terrain.setTexture(1, street, 1);
                terrain.texture();
            } catch (IOException ex) {
                Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            //  Block b1 = new Block("b1", new Point2f(100,75),30,30,100);
            //terrain.addShape(b1);
            //b1.texture("roof.jpg","bld1.jpg");
            
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
            Step[] stepsArray1, stepsArray2, movStep3 = null;
            
            Point2f start = new Point2f(100, 150);
            Point2f dest = new Point2f(100, 0);
            
            
            stepsArray1 = terrain.genStepsArray( start, dest, 0, 0, 0, 15);
            stepsArray2 = terrain.genStepsArray( new Point2f(100, 10), new Point2f(100, 170), 0, 0, 0, 15);
            //movStep3 = new MovementWrapper().genMovements(terrain, new Point2f(50, 20), new Point2f(50, 170), 0, 0, 0, 15);
            //add user model
            UserWrapper user = new UserWrapper("kiwiBot.skp", new Point2f(100, 150), 5, (float)3, 270);
            Point3d eye2 = new Point3d(140, 190, 25);
            
            terrain.addUserModel(user);
            Model car = new Model ("car","bigCrunch.skp", new Point2f(100,10), 5,(float) 0.05, 90, true);
            terrain.addModel(car);
            
            
            user.setAnimation(stepsArray1);
            car.setAnimation(stepsArray2);
            
            //user.setAnimation(stepsArray2);
            // user.loadAnimation();
            terrain.startAnimation();
            //   System.exit(0);
            //add the car model
            //user.setCam(eye2);
            
            
            
            
            //    car.loadAnimation();
            
            //animate the car accordingly to the movement steps
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
        }
        

     
    }

    @Override
    public void finish(FinishEvent e) {
        System.out.println("finished");
    }

    @Override
    public void collision(CollisionEvent e) {

        int offset = 50;
        Step[] targetStep;
        float angle = e.getSourceAngle();
        int time = e.getTime();
        //model param
        modelStep = e.getSourceStep();
        Point2f p0 = e.getSourcePos();
        Point2f dimen = e.getSourceDimen();
        //target param
        targetStep = e.getTargetStep();
        Point2f tarPosition = e.getTargetPos();
        Point2f tarDimen = e.getTargetDimen();
        float targetAngle = e.getTargetAngle();
        
        //Steps Generation
        Step[]  mov1 = null, mov2 = null, mov3 = null, mov4 = null, mov5 = null, mov6 = null, mov7 = null, mov8 = null, mov9 = null;
        Step[]  tarMov1 = null, tarMov2 = null;
        Point2f p1 = null, p2 = null, p3 = null, p4 = null;


        p1 = new Point2f((float) (p0.x + offset * Math.cos(Math.toRadians(90 - angle))), (float) (p0.y - offset * Math.sin(Math.toRadians(90 - angle))));
        mov1 = terrain.genStepsArray( p0, p0, 0, -90, time, time + 1);
        mov2 = terrain.genStepsArray( p0, p1, 0, 0, time+1, time + 2);
        p2 = new Point2f((float) (tarPosition.x + offset * Math.cos(Math.toRadians(90 - targetAngle))), (float) (tarPosition.y - offset * Math.sin(Math.toRadians(90 - targetAngle))));
        tarMov1 = terrain.genStepsArray( tarPosition, tarPosition, 0, -90, time , time + 1);
        tarMov2 = terrain.genStepsArray( tarPosition, p2, 0, 0, time+1 , time + 2);
        
        for (Model model : terrain.getDynamicModels()) {



                if (model.getName().equals(e.getSourceName())) {

                    try {
                        model.setAnimation(mov1);
                        model.setAnimation(mov2);
                    } catch (IOException ex) {
                        Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                if (model.getName().equals(e.getTargetName())) {

                    try {
                        model.setAnimation(tarMov1);
                        model.setAnimation(tarMov2);
                    } catch (IOException ex) {
                        Logger.getLogger(Collision4.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }


        }
        terrain.startAnimationAt(time);
    }
    }

