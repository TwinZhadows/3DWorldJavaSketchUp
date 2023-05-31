package terrain2;

/**
 * @author chon
 * Holds information on a static cylinder:
 * block-name x y width length height rotation texture-fnm [roof-texture-fnm ]
 */

public class StaticBlock extends StaticShape
{
  private String textureFnm, roofFnm;
  private float width, length, height;



  public StaticBlock(String id, float x, float y, 
                     float width, float length, float height,
                     float rot, String textureFnm, String roofFnm)
  {
    super(id, x, y, rot);
    this.width = width;  this.length = length;  this.height = height;
    this.textureFnm = textureFnm; this.roofFnm = roofFnm;

  }


  public float getWidth()
  {  return width;  }

  public float getLength()
  {  return length;  }

  public float getHeight()
  {  return height;  }

  public String getTextureFnm()
  {  return textureFnm;  }

  public String getRoofFnm()
  {  return roofFnm;  }

  public String toString()
  {  return  super.toString() + " <" + width + ", " + length + ", " + height +
             "> t:" + textureFnm + " rt:" + roofFnm;
  }

}  // end of StaticBlock