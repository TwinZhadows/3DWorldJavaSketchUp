/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import terrain2.*;
import java.awt.Point;
import javax.vecmath.*;
import java.io.*;
import java.lang.*;
import javax.media.j3d.QuadArray;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chon
 */
public class Movements {

    private SKPControl skp = new SKPControl();
    private static final String SKP_TITLE = "Untitled - SketchUp";
    private static final String EDITOR_TITLE = "Output - terrain2 (run)   - Editor";
    private static final String RC_TITLE = "Ruby Console";
    private static JAutoIt lib = JAutoIt.INSTANCE;
    private ArrayList<QuadArray> polygonAr = new ArrayList<QuadArray>();
    private ArrayList<Point3f> steps = new ArrayList<Point3f>(); //no height
    private ArrayList<Point3f> steps2 = new ArrayList<Point3f>(); //has height
    private int index = 0;
    private Point3f start;
    private float subAngle;
    private int totalStep;
    Step[] movStep;
  
    Terrain terrain;
    boolean append = false;

    public Step[] genStepsArray(Terrain terrain, Point2f start, Point2f dest, double sAngle, double dAngle, int startTime, int stopTime) throws IOException, FileNotFoundException, MovementException {
        
        if(startTime<0 || stopTime <0){
            try {
                throw new MovementException("Time cannot be negative number");
            } catch (MovementException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(startTime > stopTime){
            try {
                throw new MovementException("Stop time must be greater than start time");
            } catch (MovementException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.terrain = terrain;
        int step = stopTime - startTime;
        totalStep = step;
        subAngle = (float) (dAngle - sAngle) / (step); // /step

        Point3f t1;
        float h;
        Point3f prevP = new Point3f();
        int preIndex = 0;
        int numStep;
        //b.checkCoor(15,1);
        numStep = calcStep(start, dest);
        movStep = new Step[numStep];
        //calcStep(p2,p3);
        prevP = steps.get(0);
        for (int i = 0; i < steps.size(); i++) {
            t1 = new Point3f(steps.get(i).x, steps.get(i).y, 0);
            h = terrain.getHeight((int) t1.x, (int) t1.y);
            steps.get(i).setZ(h);
            //System.out.println(steps.get(i).x +","+steps.get(i).y+","+steps.get(i).z); 

            if (index != preIndex) {
                calcIntersection(prevP, t1, index, i);

            }
            movStep[i] = new Step(startTime, steps.get(i).x, steps.get(i).y, h, 0);
            startTime ++;
            //steps2.add(new Point3f(steps.get(i).x,steps.get(i).y,h));
            prevP = steps.get(i);
            preIndex = index;
        }

        calAngle();

        System.out.println("Movements are generated");
        return movStep;

    }

    private int calcStep(Point2f start, Point2f end) throws FileNotFoundException, IOException, MovementException {

        float d = 0;
        float px = 0, py = 0;
        int count = 0;
        float vx = end.x - start.x;
        float vy = end.y - start.y;
        d = (float) Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));

        float step = d / (totalStep);//ten steps
        //System.out.println(step);



        //System.out.println(steps.get(0).x+","+steps.get(0).y);
        for (int i = 0; i <= totalStep ; i++) {

            if (vx == 0 && vy == 0) {
                steps.add(new Point3f(start.x, start.y, 0));

            } else {
                if (i == 0) {
                    if (/*!isCollision(start.x, start.y)&&*/!isOutOfTerrain(start.x, start.y)) {
                        steps.add(new Point3f(start.x, start.y, 0));
                    } else {
                        break;
                    }

                } else if (i == totalStep ) {
                    if (/*!isCollision(end.x, end.y)&&*/!isOutOfTerrain(end.x, end.y))  {
                        steps.add(new Point3f(end.x, end.y, 0));
                    } else {
                        break;
                    }

                } else {
                    step *= i;
                    px = (float) (start.x + vx * (step / d));
                    py = (float) (start.y + vy * (step / d));
                    step /= i;
                    if (/*!isCollision(px, py)&&*/!isOutOfTerrain(px, py))  {
                        steps.add(new Point3f(px, py, 0));


                    } else {
                        break;
                    }
                }
            }
            //System.out.println((int)steps.get(i).x +", "+(int)steps.get(i).y +", "+ (int)steps.get(i).z);
            count++;
        }
        return count;


    }

    private void calcIntersection(Point3f p0, Point3f p1, int i, int stepIndex) {
        Point p;
        Point3f c0 = new Point3f(),
                c1 = new Point3f(),
                c2 = new Point3f();
        polygonAr.get(i).getCoordinate(0, c0);
        polygonAr.get(i).getCoordinate(1, c1);
        polygonAr.get(i).getCoordinate(2, c2);

        lineIntersect(c0.x, c0.y, c1.x, c1.y, p0.x, p0.y, p1.x, p1.y, stepIndex);
        lineIntersect(c0.x, c0.y, c2.x, c2.y, p0.x, p0.y, p1.x, p1.y, stepIndex);
        lineIntersect(c1.x, c1.y, c2.x, c2.y, p0.x, p0.y, p1.x, p1.y, stepIndex);
    }

    private int lineIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int stepIndex) {
        double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        Point3f p;
        float h;
        if (denom == 0.0) { // Lines are parallel.
            return 0;
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
        if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
            // Get the intersection point.
            p = new Point3f((float) (x1 + ua * (x2 - x1)), (float) (y1 + ua * (y2 - y1)), 0);
            h = terrain.getHeight((float) p.x, (float) p.y);
            p.setZ(h);
            steps2.add(new Point3f(p.x, p.y, p.z));
            //System.out.println("added");
            return 1;

        }

        return 0;
    }

    private void calAngle() throws IOException {

        Point3f p1, p2, p0, p3;
        p2 = new Point3f(0, 0, 0);
        float angle = 0;
        Vector2f v1, v2;


        start = new Point3f(movStep[0].getX(), movStep[0].getY(), movStep[0].getZ());
        for (int i = 0; i < movStep.length; i++) {
            p1 = new Point3f(movStep[i].getX(), movStep[i].getY(), movStep[i].getZ());

            if (i == 0 || i == steps.size()) {
                angle = 0;
            } else {
                angle = subAngle;
            }
            movStep[i].setAngle(angle);
/*
                if (angle != 0) {
                    out.write("r," + movStep[i].getX() + "," + movStep[i].getY() + "," + movStep[i].getZ() + "," + movStep[i].getAngle());
                    out.newLine();
                } else {
                    out.write(movStep[i].getX() + "," + movStep[i].getY() + "," + movStep[i].getZ());
                    out.newLine();
                }


                /*      
                 //rotation between 2 paths 
                 if(i>0){
                 if(i<steps2.size()-1)
                 p2 = new Point3f(steps2.get(i+1).x, steps2.get(i+1).y,0);
                   
                 p0 = new Point3f(steps2.get(i-1).x, steps2.get(i-1).y,0);
                 v1 = new Vector2f(p0.x-p1.x , p0.y-p1.y);
                 v2 = new Vector2f(p2.x-p1.x , p2.y-p1.y);
                   
                 if((v2.x==0 && v2.y==0)&&(i<steps2.size()-2)){
                 // System.out.println("true"+p0+","+p1+","+p2);
                 p3 = new Point3f(steps2.get(i+2).x, steps2.get(i+2).y,0);
                 p2 = p3;
                 // System.out.println(p2);
                 v2 = new Vector2f(p3.x-p1.x , p3.y-p1.y);
                   
                 }
                    
                 angle = (float) Math.toDegrees(Math.atan2(v2.y,v2.x) - Math.atan2(v1.y,v1.x));
                 if(v1.x==0 && v1.y==0) {
                 //do nothing 
                 }
                 else{
                 // System.out.println(angle+"+degree");
                 if((int)angle != 179 && (int)angle != 180 && angle>0){
                 if ((int)angle>180){
                 out.write("r,"+ (angle-180)+","+p1.x+","+p1.y+","+p1.z );
                 out.newLine();   
                 }
                 else{
                 out.write("r,"+(180-angle)*-1 +","+p1.x+","+p1.y+","+p1.z );
                 out.newLine();                            
                 }
                 }
                 }
                 }
                 */
            
        }

    }

    //  def animate(obj, x, y, z, rotation)
    /*
     public void animate(Step step) {
        
     lib.AU3_WinActivate(SKP_TITLE, "");
     lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
     lib.AU3_Send("anim.animate(model, " + step.getX() + "," + step.getY() + "," + step.getZ() + "," + step.getAngle()+ ")", 1);
     lib.AU3_Send("{ENTER}", 0);
     lib.AU3_Sleep(3000);
     skp.getDisplay();
     lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
     // clear the display

     lib.AU3_Sleep(1000);
     lib.AU3_WinActivate(EDITOR_TITLE, "");
     }
     */
    public boolean loadMov() throws IOException {

        String animationClass = "C:/Animation.rb";
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
        lib.AU3_Send("load{SPACE}\"" + animationClass + "\"{ENTER}", 0);
        lib.AU3_Send("anim = Animation.new {ENTER}", 0);
        lib.AU3_Send("anim.createAnimation()", 1);
        lib.AU3_Send("{ENTER}", 0);
        lib.AU3_Sleep(5000);
        skp.getDisplay();
        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        lib.AU3_Sleep(1000);
        lib.AU3_WinActivate(EDITOR_TITLE, "");
        return true;

    }

    private boolean isCollision(float x, float y) throws FileNotFoundException, IOException, MovementException {

        ArrayList<Shape> shapes;
        ArrayList<Model> models;
        int i, width, length;
        Point2f position;
        
        if ((shapes = terrain.getShapes())!=null)
        for (i = 0; i < shapes.size(); i++) {

            width = shapes.get(i).getWidth() / 2;
            length = shapes.get(i).getLength() / 2;
            position = shapes.get(i).getPosition();

            if (x >= position.x - width && x <= position.x + width) {
                if (y >= position.y - length && y <= position.y + length) {
                    
                    throw new MovementException("Collision is detected at " + x + ", " + y);

                }
            }
        }
        if ((models = terrain.getModels())!=null)
        for (i = 0; i < models.size(); i++) {

            width = models.get(i).getWidth() / 2;
            length = models.get(i).getLength() / 2;
            position = models.get(i).getPosition();

            if (x >= position.x - width && x <= position.x + width) {
                if (y >= position.y - length && y <= position.y + length) {

                    System.out.println("Collision is detected at " + x + ", " + y);
                    return true;
                }
            }
        }

        return false;


    }
    
    private boolean isOutOfTerrain(float x, float y) throws MovementException {
        
        double edge = terrain.getResolution();
       
        if (x>edge || y>edge) {          
            throw new MovementException("Edge is detected at "+ (int)x +"," + (int)y);
            
        } else {
            return false;
        }
        
    }
    /*  public static void main(String args[]) throws IOException {
     Point3f p1 = new Point3f(0,0,0);   //origin     
     Point3f p2 = new Point3f(50,50,0); //50 50 0
     Point3f p3 = new Point3f(50,100,0);// 50 100 0
     calMov(p1, p2, 45, 90, 10);
     }*/
}
