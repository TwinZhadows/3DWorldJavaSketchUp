package terrain2;

import javax.vecmath.*;
import java.io.*;
import java.lang.*;
import javax.media.j3d.QuadArray;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author chon
 * generate steps array for dynamic models
 */
public class Movements {

  private ArrayList<QuadArray> polygonAr;
  private ArrayList<Point3f> steps = new ArrayList<Point3f>(); //no height
  private ArrayList<Step> steps2 = new ArrayList<Step>(); //has height
  private float subAngle;
  private int totalStep;
  private Step[] stepsArray;
  private Terrain terrain;


  public Step[] genStepsArray(Terrain terrain, Point2f start, Point2f dest, double sAngle, double dAngle, int startTime, int stopTime) 
  {
    /*calculate position and rotation of each coordimate along the animation path
    from start tp dest
    */
    if (startTime < 0 || stopTime < 0) {
      try {
        throw new MovementException("Time cannot be negative number");
      } catch (MovementException ex) {
        System.err.println("MovementException: "+ ex.getMessage());
      }
    }

    if (startTime > stopTime) {
      try {
        throw new MovementException("Stop time must be greater than start time");
      } catch (MovementException ex) {
        System.err.println("MovementException: "+ ex.getMessage());
      }
    }

    this.terrain = terrain;
    polygonAr = terrain.getPolygonArray();
    int step = stopTime - startTime;
    totalStep = step;
    subAngle = (float) (dAngle - sAngle) / (step); // /step
    int index = 0;
    Point3f t1;
    float h;
    Point3f prevP = new Point3f();
    int preIndex = 0;
    //calculate x, y position of each coordinate
    calcStep(start, dest);
    prevP = steps.get(0);
    //find the intersection coodinate and store every coordinate in steps2
    for (int i = 0; i < steps.size(); i++) {
      t1 = new Point3f(steps.get(i).x, steps.get(i).y, 0);
      h = terrain.getHeight((int) t1.x, (int) t1.y);
      index = findPolygon((int) t1.x, (int) t1.y);
      steps.get(i).setZ(h);   
      //if different polygon
      if (i > 0 && index != preIndex) {
        //calculate intersection and add to steps2
        calcIntersection(prevP, t1, index, startTime);
      }
      steps2.add(new Step(startTime, steps.get(i).x, steps.get(i).y, h, 0));        
      startTime++;
      prevP = steps.get(i);
      preIndex = index;
    }
    //crate steps array from steps2
    stepsArray = new Step[steps2.size()];
    for (int i = 0; i < steps2.size(); i++) {
      stepsArray[i] = steps2.get(i);
    }
    //calculate angle at each coordinate of steps array
    calAngle();
    System.out.println("Movements are generated");
    return stepsArray;

  }//end of genStepsArray

  
  private int calcStep(Point2f start, Point2f end) 
  {
    /*calculate small vector from start position to next coordinate and increment
      the start position by the vector in each iteration of the loop, in order 
      to calculate each coordinate along the path to the destination
    */
    float d = 0;
    float px = 0, py = 0;
    int count = 0;
    float vx = end.x - start.x;
    float vy = end.y - start.y;
    d = (float) Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));

    float step = d / (totalStep);
    /*calculate each coordinate from the vector and check if the coordinate is 
      in the terrain and if there is a static collision, before adding the 
      coordinate to the array 
      **static collision will raise and exception**
    */
    for (int i = 0; i <= totalStep; i++) {
      if (vx == 0 && vy == 0) {
        steps.add(new Point3f(start.x, start.y, 0));

      } else {
        if (i == 0) {
          if (!isCollision(start.x, start.y) && !isOutOfTerrain(start.x, start.y)) {
            steps.add(new Point3f(start.x, start.y, 0));
            System.out.println("first added");
          } else {
            break;
          }

        } else if (i == totalStep) {
          if (!isCollision(end.x, end.y) && !isOutOfTerrain(end.x, end.y)) {
            steps.add(new Point3f(end.x, end.y, 0));
            System.out.println("added");
          } else {
            break;
          }

        } else {
          step *= i;
          px = (float) (start.x + vx * (step / d));
          py = (float) (start.y + vy * (step / d));
          step /= i;
          if (!isCollision(px, py) && !isOutOfTerrain(px, py)) {
            steps.add(new Point3f(px, py, 0));
            
          } else {
            break;
          }
        }
      }
      count++;
    }
    return count;

  }//end of calcStep

  
  private void calcIntersection(Point3f p0, Point3f p1, int i, int time)
  {
    /*
    get three sides of polygon where p0 is on and try calculating intersection
    between vector p0p1 and each side of the polygon
    */
    Point3f c0 = new Point3f(),
            c1 = new Point3f(),
            c2 = new Point3f();

    polygonAr.get(i).getCoordinate(0, c0);
    polygonAr.get(i).getCoordinate(1, c1);
    polygonAr.get(i).getCoordinate(2, c2);

    lineIntersect(c0.x, c0.y, c1.x, c1.y, p0.x, p0.y, p1.x, p1.y, time);
    lineIntersect(c0.x, c0.y, c2.x, c2.y, p0.x, p0.y, p1.x, p1.y, time);
    lineIntersect(c1.x, c1.y, c2.x, c2.y, p0.x, p0.y, p1.x, p1.y, time);
  }//end of calcIntersection

  
  private int lineIntersect(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, int time)
  { 
    //calculate intersection between four coordinates representing two vectors
    double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
    Point3f p;
    float h;
    if (denom == 0.0) { //lines are parallel.
      return 0;
    }
    double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
    double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
    if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
      //get the intersection point.
      p = new Point3f((float) (x1 + ua * (x2 - x1)), (float) (y1 + ua * (y2 - y1)), 0);
      h = terrain.getHeight((float) p.x, (float) p.y);
      p.setZ(h);

      steps2.add(new Step((float) (time - 0.5), p.x, p.y, p.z, 0));
      return 1;

    }
    return 0;
  }//end of line Intersect

  
  private void calAngle() 
  {
    //assign angle to each point in the steps array
    float angle = 0;
    for (int i = 0; i < stepsArray.length; i++) {
      if (i == 0 || i == steps.size()) {
        angle = 0;
      } else {
        angle = subAngle;
      }
      stepsArray[i].setAngle(angle);
    }
  }//end of calAngle()

  
  private boolean isOutOfTerrain(float x, float y) 
  {
    //chech whether the coordinate is out of the terrain
    double edge = terrain.getResolution();
    if (x > edge || y > edge) {
      try {
        throw new MovementException("Edge is detected at " + (int) x + "," + (int) y);
      } catch (MovementException ex) {
        System.err.println("MovementException: "+ ex.getMessage());
      }
    } 
    return false;
  }//end of isOutOfTerrain

  
  private boolean isCollision(float x, float y) 
  {
    //find if there is a static collision at the given coordinate
    ArrayList<Shape> shapes;
    ArrayList<Model> models;
    int i, width, length;
    Point2f position;
    //collision with shapes
    if ((shapes = terrain.getShapes()) != null) {
      for (i = 0; i < shapes.size(); i++) {

        width = shapes.get(i).getWidth() / 2;
        length = shapes.get(i).getLength() / 2;
        position = shapes.get(i).getPosition();

        if (x >= position.x - width && x <= position.x + width) {
          if (y >= position.y - length && y <= position.y + length) {
            try {
              throw new MovementException("Collision is detected at " + x + ", " + y);
            } catch (MovementException ex) {
              System.err.println("MovementException: "+ ex.getMessage());
            }

          }
        }
      }
    }
    //collision with static models
    if ((models = terrain.getStaticModels()) != null) {
      Point3f c0, c1, c2, c3;

      for (i = 0; i < models.size(); i++) {

        width = models.get(i).getWidth() / 2;
        length = models.get(i).getLength() / 2;
        position = models.get(i).getPosition();
        c0 = new Point3f(position.x - width, position.y - length, 0);
        c1 = new Point3f(position.x - width, position.y + length, 0);
        c2 = new Point3f(position.x + width, position.y + length, 0);
        c3 = new Point3f(position.x + width, position.y - length, 0);

        if ((terrain.calcZ(c0, c1, c2, x, y) != -1) || (terrain.calcZ(c0, c2, c3, x, y) != -1)) {
          try {
            throw new MovementException("Collision is detected at " + x + ", " + y);
          } catch (MovementException ex) {
            System.err.println("MovementException: "+ ex.getMessage());
          }
        }
      }
    }
    return false;
  }//end of isCollision()

  
  public int findPolygon(float x, float y)
  {
    //find which polygon the coordinate is on using Barycentric approach 
    Point3f c0;
    Point3f c1;
    Point3f c2;

    int i = 0;
    while (i < polygonAr.size()) {
      c0 = new Point3f();
      c1 = new Point3f();
      c2 = new Point3f();

      polygonAr.get(i).getCoordinate(0, c0);
      polygonAr.get(i).getCoordinate(1, c1);
      polygonAr.get(i).getCoordinate(2, c2);

      if (terrain.calcZ(c0, c1, c2, x, y) > -1) {
        return i;
      }
      i++;
    }
    return -1;
  }//end of findPolygon()

}//end of Movement class
