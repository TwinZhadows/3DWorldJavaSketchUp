package terrain2;


// AUUtils.java

import java.io.*;



public class AUUtils 
{

  public static String chars2String(char[] msg)
  {
    int numChanges = 0;
    for (int i=0; i < msg.length; i++)
       if (msg[i] > 127) {    // ASCII is in range 0 to 127
         msg[i] = ' ';
         numChanges++;
       }
     // System.out.println("Num changes: " + numChanges);
     String msgStr = new String(msg).trim();

     return msgStr;
  }  // end of chars2String()


  // ==================================================================

  public static String getAutoItDLL()
  {  
    if (is64bit())
      return  "C:/Program Files/AutoIt3/AutoItX/AutoItX3_x64.dll";
    else
      return "C:/Program Files/AutoIt3/AutoItX/AutoItX3.dll";
   }


  private static boolean is64bit()
  {
    if (System.getProperty("os.name").contains("Windows"))
      return (System.getenv("ProgramFiles(x86)") != null);
    else
      return (System.getProperty("os.arch").indexOf("64") != -1);
  }


}  // end of AUUtils class
