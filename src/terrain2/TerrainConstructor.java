
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain2;

import java.io.IOException;

/**
 *
 * @author chon
 */
public class TerrainConstructor extends Terrain{

    public TerrainConstructor(int depth, int size, int plgSize) throws IOException{
        super(depth, size, plgSize);
    }
    
    public TerrainConstructor(String file, int plgSize) throws IOException{
        super(file, plgSize);
    }
    
    public TerrainConstructor(String file, int depth, int plgSize) throws IOException{
        super(file, depth, plgSize);
    }
}
