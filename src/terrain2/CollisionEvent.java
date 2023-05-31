
package terrain2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.vecmath.Point2f;

/**
 * @author chon
 *Read collision file and and store collision information. The models names are
 *utilized to open their steps file, regenerate steps arrays of source and target 
 *and store in the CollisionEvent instance.
 */
public class CollisionEvent {

  private String name, targetName;
  private int x0, y0, w0, h0, x1, y1, w1, h1, vx, vy, time;
  float angle, tAngle;
  private ArrayList<String[]> sourceFile = new ArrayList<String[]>();
  private ArrayList<String[]> targetFile = new ArrayList<String[]>();
  private Step[] sourceSteps, targetSteps;
  private static SKPControl skp = new SKPControl();
  private String path;

  public CollisionEvent(Object source, String file)
  { //read collision file and create steps array out of each model's step file
    path = skp.getDirectory();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(path + file));
    } catch (FileNotFoundException ex) {
      System.err.println("FileNotFoundException: "+ ex.getMessage());
    }
    String line;
    String[] collisionInfo = null;
    //extract collision information
    try {
      while ((line = br.readLine()) != null) {
        collisionInfo = line.split(", ");
        angle = Float.parseFloat(collisionInfo[13]);
        tAngle = Float.parseFloat(collisionInfo[14]);
        time = Integer.parseInt(collisionInfo[12]);
        w0 = Integer.parseInt(collisionInfo[1]);
        h0 = Integer.parseInt(collisionInfo[2]);
        x0 = Integer.parseInt(collisionInfo[3]);
        y0 = Integer.parseInt(collisionInfo[4]);
        w1 = Integer.parseInt(collisionInfo[6]);
        h1 = Integer.parseInt(collisionInfo[7]);
        x1 = Integer.parseInt(collisionInfo[8]);
        y1 = Integer.parseInt(collisionInfo[9]);

        name = collisionInfo[0];
        targetName = collisionInfo[5];

      }
    } catch (IOException ex) {
      Logger.getLogger(CollisionEvent.class.getName()).log(Level.SEVERE, null, ex);
    }

    //read step files 
    String[] stepInfo;
    try {
      BufferedReader step = new BufferedReader(new FileReader(path + name + "Step.txt"));
      BufferedReader tarStep = new BufferedReader(new FileReader(path + targetName + "Step.txt"));
      while ((line = step.readLine()) != null) {
        stepInfo = line.split(", ");
        sourceFile.add(stepInfo);
      }
      while ((line = tarStep.readLine()) != null) {
        stepInfo = line.split(", ");
        targetFile.add(stepInfo);
      }
      step.close();
      tarStep.close();
      //reset the files
      new FileWriter(path + name + "Step.txt");
      new FileWriter(path + targetName + "Step.txt");

    } catch (IOException ex) {
      Logger.getLogger(CollisionEvent.class.getName()).log(Level.SEVERE, null, ex);
    }
    sourceSteps = new Step[sourceFile.size()];
    targetSteps = new Step[targetFile.size()];
    double time;
    float x, y, z, angle;
    //recreate source's steps array

    for (int i = 0; i < sourceFile.size(); i++) {

        time = Double.parseDouble(sourceFile.get(i)[0]);
        x = Integer.parseInt(sourceFile.get(i)[1]);
        y = Integer.parseInt(sourceFile.get(i)[2]);
        z = Integer.parseInt(sourceFile.get(i)[3]);
        angle = Integer.parseInt(sourceFile.get(i)[4]);
        sourceSteps[i] = new Step((float) time, x, y, z, angle);

    }
    //create target's steps array
    for (int i = 0; i < targetFile.size(); i++) {
      time = Double.valueOf(targetFile.get(i)[0]);
      x = Integer.parseInt(targetFile.get(i)[1]);
      y = Integer.parseInt(targetFile.get(i)[2]);
      z = Integer.parseInt(targetFile.get(i)[3]);
      angle = Integer.parseInt(targetFile.get(i)[4]);
      targetSteps[i] = new Step((float) time, x, y, z, angle);
    }
  }//end of CollisionEvent constructor

  
  public String getSourceName()
  { return name; }

  
  public String getTargetName()
  { return targetName; }

  
  public Point2f getSourcePos()
  { return new Point2f(x0, y0); }

  
  public Point2f getTargetPos()
  { return new Point2f(x1, y1); }

  
  public Point2f getSourceDimen()
  { return new Point2f(w0, h0); }

  
  public Point2f getTargetDimen()
  { return new Point2f(w1, h1); }

  
  public float getSourceAngle()
  { return angle; }
  
  
  public float getTargetAngle()
  { return tAngle; }

  
  public int getTime()
  { return time; }

  
  public Step[] getSourceStep()
  { return sourceSteps; }

  
  public Step[] getTargetStep()
  { return targetSteps; }

  
  public Step getSourceStepAt(int time)
  { return sourceSteps[time]; }

  
  public Step getTargetStepAt(int time)
  { return targetSteps[time]; }

}//end of CollisionEvent
