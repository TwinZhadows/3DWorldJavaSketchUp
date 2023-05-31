
package terrain2;

/**
 * @author chon
 * Animation listener interface
 */
public interface AnimationListener 
{
    void finish(FinishEvent e);
    void collision(CollisionEvent e);
}

