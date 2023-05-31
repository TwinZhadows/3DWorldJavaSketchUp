package terrain2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * @author chon
 * Monitor for creation and modification of a file in project directory, create
 * event object based on the file name, and call event handling method for
 * handling the event
 *
 */
public class AnimThread implements Runnable {

  private Thread runner;
  private boolean start = false;
  private Path path;
  private boolean isAnimationStop = false;
  private boolean isCollision = false;
  private AnimationListener lis;
  
  public AnimThread(Path path)
  { 
    this.path = path;
  }

  
  public AnimThread(String threadName)
  {   
    //create and start new thread
    runner = new Thread(this, threadName); 
    System.out.println(runner.getName());
    runner.start(); 
  }

  
  public void run()
  {
    System.out.println("Animation thread is started");
    // Obtain the file system of the Path
    FileSystem fs = path.getFileSystem();
    // Create the new WatchService 
    try (WatchService service = fs.newWatchService()) {

      // Register the path to the service
      // Watch for creation events
      path.register(service, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

      // Start the infinite polling loop
      WatchKey key = null;
      while (true) {
        key = service.take();

        // Dequeueing events
        Kind<?> kind = null;
        for (WatchEvent<?> watchEvent : key.pollEvents()) {
          // Get the type of the event
          kind = watchEvent.kind();

          if (ENTRY_MODIFY == kind) {
            Path evPath = ((WatchEvent<Path>) watchEvent).context();
            // Output
            String fileName = evPath.toFile().getName();
            if (fileName.equals("Message.txt")) {
              try {
                String line;
                BufferedReader br = new BufferedReader(new FileReader(evPath.toString()));
                while ((line = br.readLine()) != null) {
                  System.out.println(line);
                }
              } catch (FileNotFoundException ex) {
                System.err.println("FileNotFoundException: " + ex.getMessage());
              }
            }
            if (fileName.contains("Collision")) {
              isCollision = true;
              lis.collision(new CollisionEvent(this, fileName));
            }
          }
          if (ENTRY_CREATE == kind) {
            // A new Path was created 
            Path evPath = ((WatchEvent<Path>) watchEvent).context();
            // Output
            String fileName = evPath.toFile().getName();
            if (fileName.contains("Collision")) {
              isCollision = true;
              lis.collision(new CollisionEvent(this, fileName));
            }
            if (fileName.equals("finish.txt")) {
              lis.finish(new FinishEvent(this));
              isAnimationStop = true;
            }
          }
          if (!key.reset()) {
            break; //loop
          }
        }
      }

    } catch (IOException ex) {
      System.err.println("IOException: " + ex.getMessage());
    } catch (InterruptedException ex) {
      System.err.println("Interruptedxception: " + ex.getMessage());
    }

  }//end of run()

  
  public boolean isFinished()
  {return isAnimationStop;}

  
  public boolean getCollisionStat()
  {return isCollision;}

  
  public void setListener(AnimationListener listener)
  {lis = listener;}

  
  public boolean isStarted()
  {return start;}

}//end of AnimThread class
