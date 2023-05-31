package terrain2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.vecmath.Point2f;

/**
 * @author chon
 * Model class contains set and get methods, a constructor for creating Model
 * instance and other methods for creating step file, writing steps array to the
 * file, and creating animation on SketchUp side
 */
public class Model {

  private static int count = 0;
  private String name;
  private Point2f position;
  private float height;
  private float angle;
  private float scale;
  private int width;
  private int length;
  private int sHeight;
  private int offsetZ;
  private String file;
  private boolean movable = false;
  private static SKPControl skp = new SKPControl();
  private static final String SKP_TITLE = "Untitled - SketchUp";
  private static final String EDITOR_TITLE = "Output - terrain2 (run)   - Editor";
  private static final String RC_TITLE = "Ruby Console";
  private static JAutoIt lib = JAutoIt.INSTANCE;
  private Step[] stepsArray;

  public Model(String file, Point2f position, int offsetZ, float scale, float angle, boolean movable)
  {
    /*
     Create static or dynamic model which is automatically named by the API ("Model" + count) 
     If the model is dynamic, empty step file will be created
        
     */
    name = "Model" + count;
    count++;
    this.position = position;
    this.scale = scale;
    this.angle = angle;
    this.file = file;
    this.movable = movable;
    this.offsetZ = offsetZ;
    if (scale <= 0) {
      try {
        throw new ModelException("Model scale must be greater than zero");
      } catch (ModelException ex) {
        System.err.println("ModelException: " + ex.getMessage());
      }
    }
    //If dynmamic model, create an empty step file
    if (movable) {
      String path = skp.getDirectory() + name + "Step.txt";
      try {
        FileWriter modelFile = new FileWriter(path);
        BufferedWriter out = new BufferedWriter(modelFile);
        out.close();
      } catch (IOException ex) {
        System.err.println("IOException: " + ex.getMessage());
      }
    }
  }//end of Model()

  
  public Model(String file, Point2f position, float scale, float angle, boolean movable)
  {
    //Create Model instance without offsetZ  
    this.name = "Model" + count;
    count++;
    this.position = position;
    this.scale = scale;
    this.angle = angle;
    this.file = file;
    this.movable = movable;
    this.offsetZ = 0;
    if (scale <= 0) {
      try {
        throw new ModelException("Model scale must be greater than zero");
      } catch (ModelException ex) {
        System.err.println("ModelException: " + ex.getMessage());
      }
    }
    //If dynamic, create step file for the model
    if (movable) {
      String path = skp.getDirectory() + name + "Step.txt";
      try {
        FileWriter modelFile = new FileWriter(path);
        BufferedWriter out = new BufferedWriter(modelFile);
        out.close();
      } catch (IOException ex) {
        System.err.println("IOException: " + ex.getMessage());
      }
    }
  }//end of Model()

  
  public void setName(String name)
  {this.name = name;}

  
  public String getName()
  {return name;}

  
  public void setPosition(Point2f position)
  {this.position = position;}

  
  public Point2f getPosition()
  {return position;}

  
  public void setZ(float height)
  {this.height = height;}

  
  public float getZ()
  {return height;}

  
  public void setRotation(float angle)
  {this.angle = angle;}

  
  public float getRotation()
  {return angle;}

  
  public void setScale(float scale)
  {this.scale = scale;}

  public float getScale()
  {return scale;}

  
  public void setWidth(int width)
  {this.width = width;}

  
  public int getWidth()
  {return width;}

  
  public void setLength(int length)
  {this.length = length;}
  

  public int getLength()
  {return length;}
  

  public void setHeight(int height)
  {sHeight = height;}

  
  public int getHeight()
  {return sHeight;}

  
  public void setFile(String file)
  {this.file = file;}

  
  public String getFile()
  {return file;}

  
  public void setDynamic(boolean movable)
  {this.movable = movable;}

  
  public boolean isMovable()
  {return movable;}

  
  public void setOffsetZ(int offsetZ)
  {this.offsetZ = offsetZ;}

  
  public int getOffsetZ()
  {return offsetZ;}

  
  public Step[] getStepsArray()
  {return stepsArray;}
  
  
  public void setAnimation(Step[] stepsArray)
  {
    /*
     Read incoming steps array and write each element to step file of the model
     as a new line: time, x, y, z, rotation
     */
    if (stepsArray == null) {
      try {
        throw new MovementException("Empty steps array cannot be set to the model");
      } catch (MovementException ex) {
        System.err.println("MovementException: " + ex.getMessage());
      }
    }
    this.stepsArray = stepsArray;
    String path = skp.getDirectory() + name + "Step.txt";
    try {
      FileWriter file = new FileWriter(path, true);
      BufferedWriter out = new BufferedWriter(file);
      for (Step step : stepsArray) {
        out.write((double)step.getTime() + ", "
                + (int) step.getX() + ", "
                + (int) step.getY() + ", "
                + (int) step.getZ() + ", "
                + (int) step.getAngle());
        out.newLine();
      }
      out.close();
    } catch (IOException ex) {
      System.err.println("IOException: " + ex.getMessage());
    }
  }//end of setAnimation()

  
  public boolean loadAnimation()
  {
    //Send  Ruby commands for creating animaton for the model to SketchUp
    lib.AU3_WinActivate(SKP_TITLE, "");
    lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
    lib.AU3_Send("anim.createAnimation(" + "\"" + name + "\"" + "){ENTER}", 0);
    lib.AU3_Sleep(2000);
    skp.getDisplay();
    lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
    //Clear the display
    lib.AU3_WinActivate(EDITOR_TITLE, "");
    return true;
  }//end of loadAnimation()
  
  public Step[] getAnimation()
  {return stepsArray;}

  public Step getAnimation(int index)
  {return stepsArray[index];}

}//end of Model Class    


/*
 public Model(String name, String file, Point2f p, int offsetZ, float scale, float angle, boolean movable) {        
 /*Create Model instance with specific name; the constructor is called from
 Shape class so that 
          
 this.name = name;
 count++;
 this.position = p;
 this.scale = scale;
 this.angle = angle;
 this.file = file;
 this.movable = movable;
 this.offsetZ = offsetZ;
 if(scale<=0){
 try {
 throw new ModelException("Model scale must be greater than zero");
 } catch (ModelException ex) {
 Logger.getLogger(Landscape.class.getName()).log(Level.SEVERE, null, ex);
 }
 }
 if (movable) {
 String path = skp.getDirectory() + getName() + "Step.txt";
 try {
 FileWriter modelFile = new FileWriter(path);
 BufferedWriter out;
 out = new BufferedWriter(modelFile);
 out.close();
 } catch (IOException ex) {
 Logger.getLogger(Landscape.class.getName()).log(Level.SEVERE, null, ex);
 }
 }
 }
    
 public Model(String name, String file, Point2f p, float scale, float angle, boolean movable) {        
        
 this.name = name;
 count++;
 this.position = p;
 this.scale = scale;
 this.angle = angle;
 this.file = file;
 this.movable = movable;
 this.offsetZ = 0;
 if(scale<=0){
 try {
 throw new ModelException("Model scale must be greater than zero");
 } catch (ModelException ex) {
 Logger.getLogger(Landscape.class.getName()).log(Level.SEVERE, null, ex);
 }
 }
 if (movable) {
 String path = skp.getDirectory() + getName() + "Step.txt";
 try {
 FileWriter modelFile = new FileWriter(path);
 BufferedWriter out;
 out = new BufferedWriter(modelFile);
 out.close();
 } catch (IOException ex) {
 Logger.getLogger(Landscape.class.getName()).log(Level.SEVERE, null, ex);
 }
 }
 }
 */
