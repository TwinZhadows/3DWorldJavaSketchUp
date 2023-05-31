/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain2;

/**
 * @author Chon
 * store information in each step of the model's animation including time, x, y
 * z, and angle
 */
public class Step {

  private float time;
  private float x;
  private float y;
  private float z;
  private float angle;

  public float getX()
  {return x;}

  
  public float getY()
  {return y;}

  
  public float getZ()
  {return z;}

  
  public float getAngle()
  {return angle;}

  
  public float getTime()
  {return time;}

  
  public void setX(float x)
  {this.x = x;}

  
  public void setY(float y)
  {this.y = y;}

  
  public void setZ(float z)
  {this.z = z;}

  
  public void setAngle(float angle)
  {this.angle = angle;}

  
  public void setTime(int time)
  {this.time = time;}

  
  public Step(float time, float x, float y, float z, float angle)
  {
    this.time = time;
    this.x = x;
    this.y = y;
    this.z = z;
    this.angle = angle;
  }
}//end of Step
