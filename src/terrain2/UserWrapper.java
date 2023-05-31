/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain2;

import java.io.IOException;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

/**
 *
 * @author chon
 */
public class UserWrapper extends User{
   
 /*   public boolean addModel(String name, Point3f position, double rotation, double scale) throws IOException {
        user.loadModel(name, position, rotation, scale);
        return true;
    }
*/
    public UserWrapper(String file, Point2f position, int offsetZ, float scale, float rotation ){
         super(file, position, offsetZ, scale, rotation);
    }
    public UserWrapper(String file, Point2f position, float scale, float rotation ){
         super(file, position, 0, scale, rotation);
    }
}