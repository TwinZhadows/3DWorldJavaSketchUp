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
public class Scene2 extends Scene implements AnimationListener   {
  TerrainConstructor terrain;
  public Scene2() throws IOException {
    SketchUp skp = new SketchUp();
    skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
    skp.setListener(this);
    skp.startSketchUp();
    terrain = new TerrainConstructor(0, 3, 50);
    setCam(new Point3d(673, -20, 307), new Point3d(614, 6, 269));

    Model dynamicCar1 = new Model("car2.skp", new Point2f(50, 250),0, (float)1.0, 0, true);
    Model dynamicCar2 = new Model("car.skp", new Point2f(350, 250),0, (float)0.3, 180, true);
    Block block = new Block(new Point2f(250, 350), 50, 50, 100);
    terrain.addShape(block);
    block.texture("stone.jpg");
    terrain.addModel(dynamicCar2);
    terrain.addModel(dynamicCar1);
    
    //terrain.addModel(car3);
    terrain.setLevel(2);
    terrain.setTexture(1, "sand.jpg");
    terrain.setTexture(2, "sand2.jpg");
    terrain.texture();
    Step[] car1Anim = terrain.genStepsArray(new Point2f(50, 250), new Point2f(250, 250), 0, 5);
    Step[] car2Anim = terrain.genStepsArray(new Point2f(350, 250), new Point2f(250, 250), 0, 5);

    dynamicCar2.setAnimation(car2Anim);
    dynamicCar1.setAnimation(car1Anim);

    terrain.startAnimation();
  }
  public static void main (String args[]) throws IOException{
    new Scene2();
  }
    public void collision(CollisionEvent e) {
        CollisionRecovery.basicRecovery(terrain, e);
    }


  @Override
  public void finish(FinishEvent e)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
