/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain2;

import javax.vecmath.Point2f;

/**
 * @author chon
 * provide two block constructor for generating a Block instance with and without 
 * specifying Z offset of the block
 */
public class Block extends Shape {

  private static int count = 0;

  public Block(Point2f position, int offsetZ, int width, int length, int height)
  {
    //create block instance with position, offsetZ, and block dimension
    if (width <= 0) {
      try {
        throw new ShapeException("Block width cannot be negative or zero");
      } catch (ShapeException ex) {      
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    if (length <= 0) {
      try {
        throw new ShapeException("Block length cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    if (height <= 0) {
      try {
        throw new ShapeException("Block height cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    String name = "Block" + count;
    setName(name);
    setPosition(position);
    setWidth(width);
    setLength(length);
    setHeight(height);
    setType("block");
    setOffsetZ(offsetZ);
    count++;
  }//end of Block constructor

  
  public Block(Point2f position, int width, int length, int height)
  { 
    //create block instance without offsetZ
    if (width <= 0) {
      try {
        throw new ShapeException("Block width cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    if (length <= 0) {
      try {
        throw new ShapeException("Block length cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    if (height <= 0) {
      try {
        throw new ShapeException("Block height cannot be negative or zero");
      } catch (ShapeException ex) {
        System.err.println("ShapeException: " + ex.getMessage());
      }
    }
    String name = "Block" + count;
    setName(name);
    setPosition(position);
    setWidth(width);
    setLength(length);
    setHeight(height);
    setType("block");
    setOffsetZ(0);
    count++;
  }//end of Block constructor

}//end of Block
