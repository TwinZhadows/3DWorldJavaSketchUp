
import terrain2.AnimationListener;
import terrain2.CollisionEvent;
import terrain2.FinishEvent;
import terrain2.HouseScene;
import terrain2.SketchUp;
import terrain2.TerrainConstructor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Roonnapak
 */
public class House_main implements AnimationListener {
    static TerrainConstructor terrain;
    public static void main(String args[]) {
            //Initiate SketchUp
            SketchUp skp = new SketchUp();
            skp.setListener(new HouseScene());
            skp.setDirectory("C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/");
            skp.startSketchUp();
    }

    @Override
    public void finish(FinishEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void collision(CollisionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
