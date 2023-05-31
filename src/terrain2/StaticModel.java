package terrain2;

/**
 * @author Chon
 * Holds information on a static cylinder:
 * model-fnm model-name x y scale rotation
*/


public class StaticModel extends StaticShape
{
  private String modelFnm;
  private float scale;



  public StaticModel(String id, float x, float y, 
                     float scale, float rot, String modelFnm)
  {
    super(id, x, y, rot);
    this.scale = scale; 
    this.modelFnm = modelFnm;

  }


  public float getScale()
  {  return scale;  }

  public String getModelFnm()
  {  return modelFnm;  }


  public String toString()
  {  return  super.toString() + " s:" + scale + " mt:" + modelFnm;  }

}  // end of StaticModel