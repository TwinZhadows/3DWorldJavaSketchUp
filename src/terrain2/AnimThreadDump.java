package terrain2;


import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;


/**
 * @author chon
 * Monitor for creation and modification of a file in project directory,
 *create event object based on the file name, and call event handling method 
 * for handling the event
 * 
 */
public class AnimThreadDump implements Runnable 
{
  private Thread runner;
  private boolean start = false;   
  private int time;
  private Path path;
  private boolean isAnimationStop = false;
  private boolean isCollision = false;
  private AnimationListener lis;
    
 
  public AnimThreadDump(Path path, int time) 
  {
    this.path = path;
    this.time = time;
  }

  public AnimThreadDump(String threadName) 
  {
    //create and start new thread
    runner = new Thread(this, threadName); 
    System.out.println(runner.getName());
    runner.start(); 
  }

  public void run() 
  { //create monitoring loop which reads the new file name,and triggers event 
    start = true;
    System.out.println("Watching path: " + path);
    // Obtain the file system of the Path
    FileSystem fs = path.getFileSystem();
    // Create the new WatchService 
    try (WatchService service = fs.newWatchService()) 
    {
      // Register the path to the service
      // Watch for creation events
      path.register(service, ENTRY_CREATE);
      // Start the infinite polling loop
      WatchKey key = null;
        while (true) {
          key = service.take();
          // Dequeueing events
          Kind<?> kind = null;
          for (WatchEvent<?> watchEvent : key.pollEvents()) {
            // Get the type of the event
            kind = watchEvent.kind();
            if (ENTRY_CREATE == kind) {
               // A new Path was created 
               Path evPath = ((WatchEvent<Path>) watchEvent).context();
               // Output
               String file = evPath.toFile().getName();
               if (file.contains("Collision")) {
                   isCollision = true;
                   lis.collision(new CollisionEvent(this, file));
               }
               if (file.equals("finish.txt")) {
                   lis.finish(new FinishEvent(this));
                   isAnimationStop = true;
               }
              }
              if (!key.reset()) {
                   break; 
              }
           }
        }
        } catch (IOException | InterruptedException ex) {
        }
    }//end of run()

  public boolean isFinished() 
  { return isAnimationStop; }

  public boolean getCollisionStat()
  { return isCollision; }

  public void setListener(AnimationListener listener) 
  { lis = listener; }

  public boolean isStarted() 
  { return start; }
}//end of AnimThreadDump
