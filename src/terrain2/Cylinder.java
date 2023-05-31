
package terrain2;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point2f;

/**
 * @author chon
 * provide constructors for creating Cylinder
 *
 */
public class Cylinder extends Shape {

  private static int count = 0;

  public Cylinder(Point2f position, int offsetZ, int radius, int height)
  { //create cylinder instance from position, Z offset, radius, and height
    if (radius <= 0) {
      try {
        throw new ShapeException("Cylinder radius cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    if (height <= 0) {
      try {
        throw new ShapeException("Cylinder height cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    String name = "Cylinder" + count;
    setName(name);
    setPosition(position);
    setRadius(radius);
    setHeight(height);
    setType("cylinder");
    setOffsetZ(offsetZ);
    count++;
  }//end of Cylinder constructor

  
  public Cylinder(Point2f position, int radius, int height)
  { //create cylinder without Z offset
    if (radius <= 0) {
      try {
        throw new ShapeException("Cylinder radius cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    if (height <= 0) {
      try {
        throw new ShapeException("Cylinder height cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    String name = "Cylinder" + count;
    setName(name);
    setPosition(position);
    setRadius(radius);
    setHeight(height);
    setType("cylinder");
    setOffsetZ(0);
    count++;

  }//end of Cylinder constructor

}//end of Cylinder
