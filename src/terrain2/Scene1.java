/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain2;

import java.io.IOException;
import javax.vecmath.Point2f;
import javax.vecmath.Point3d;

/**
 *
 * @author chon
 */
public class Scene1 extends Scene   {
  public Scene1() throws IOException {
    SketchUp skp = new SketchUp();
    skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
    skp.startSketchUp();
    TerrainConstructor terrain = new TerrainConstructor(1, 3, 50);
    setCam(new Point3d(673, -20, 307), new Point3d(614, 6, 269));
    Model car3 = new Model("redCar.skp", new Point2f(20, 300),0, (float)0.3, 0, true);
    Model car2 = new Model("car.skp", new Point2f(20, 200),0, (float)0.4, 0, true);
    Model car = new Model("redCar.skp", new Point2f(20, 85),0, (float)0.3, 0, true);
    Model tree = new Model("yellowTree.skp", new Point2f(150, 300),0, (float)0.3, 0, false);
    terrain.addModel(car);
    terrain.addModel(car2);
    //terrain.addModel(car3);
    terrain.addModel(tree);
    terrain.setLevel(2);
    terrain.setTexture(1, "blend.jpg");
    terrain.setTexture(2, "blend4.jpg");
    terrain.texture();
    Step[] animation1 = terrain.genStepsArray(new Point2f(20, 85), new Point2f(150, 85), 0, 5);
    Step[] animation2 = terrain.genStepsArray(new Point2f(20, 200), new Point2f(150, 200), 0, 5);
    //Step[] animation3 = terrain.genStepsArray(new Point2f(20, 300), new Point2f(150, 300), 0, 5);
    car.setAnimation(animation1);
    car2.setAnimation(animation2);
    //car3.setAnimation(animation3);
    //terrain.startAnimation();
  }
  public static void main (String args[]) throws IOException{
    new Scene1();
  }

}
