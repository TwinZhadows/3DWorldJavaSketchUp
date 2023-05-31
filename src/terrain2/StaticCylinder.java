package terrain2;

/**
 * @author Chon
 * Holds information on a static cylinder:
 * cylinder-name x y radius height rotation texture-fnm
*/


public class StaticCylinder extends StaticShape
{
  private String textureFnm;
  private float radius, height;


  public StaticCylinder(String id, float x, float y, float radius, float height,
                                 float rot, String textureFnm)
  {
    super(id, x, y, rot);
    this.radius = radius;  this.height = height;
    this.textureFnm = textureFnm;
  }


  public float getRadius()
  {  return radius;  }

  public float getHeight()
  {  return height;  }

  public String getTextureFnm()
  {  return textureFnm;  }

  public String toString()
  {  return  super.toString() + " <" + radius + ", " + height + "> t:" + textureFnm;  }

}  // end of StaticCylinder