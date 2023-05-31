package terrain2;

/**
 * @author Chon
 * Superclass for static shapes:
 * StaticCylinder, StaticBlock, StaticModel
*/


public class StaticShape
{
  private String id;
  private float x;
  private float y;
  private float rotation;


  public StaticShape(String id, float x, float y, float rot)
  {
    this.id = id;
    this.x = x; this.y = y;
    rotation = rot;
  }

  public String getID()
  {  return id;  }

  public float getX()
  {  return x;  }

  public float getY()
  {  return y;  }

  public float getRotation()
  {  return rotation;  }


  public String toString()
  {  return id + " (" + x + ", " + y + ") r:" + rotation;  }


}  // end of StaticShape