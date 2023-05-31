package terrain2;


// JAutoIt.java
// Andrew Davison, ad@fivedots.coe.psu.ac.th, December 2013

/* Based on autoitx.java in the JWinAuto download
   (http://sourceforge.net/projects/jwinauto/) by
   Alan Richardson (2007) (user: ajr_compdev)

   and the C# examples at
   http://www.autoitscript.com/forum/topic/72905-c-use-of-the-dll-some-idears-for-you/

   Uses JNA (https://jna.dev.java.net/) to create a Java
   wrapper around the AutoItX DLL (AutoItX3.dll), available
   as part of AutoIt from http://www.autoitscript.com/site/autoit/

   The functions are described in the AutoIt function 
   documentation at:
     http://www.autoitscript.com/autoit3/docs/functions.htm
   and in Help files that are included in the AutoIt download:
     C:\Program Files\AutoIt3\AutoItX\AutoItX.chm  
     (in the "COM Interface" section
   and
     C:\Program Files\AutoIt3\AutoIt.chm

   Lots of advice on using the functions can be found at 
   the AutoIt scripting forum: 
     http://www.autoitscript.com/forum/

   Information on Window handles, title captions, etc can be 
   found using AutoIt WindowInfo (Au3Info.exe or Au3Info_x64.exe)
   which is a tool in the AutoIt download 

   =================================
   Another approach: 
      * AutoItX4Java uses JACOB to access AutoItX through COM
        https://code.google.com/p/autoitx4java/
*/

import java.util.*;

import com.sun.jna.*;
import com.sun.jna.win32.*;


public interface JAutoIt extends com.sun.jna.Library
{

  public static int AU3_INTDEFAULT = -2147483647;
       // used by some functions as a default int parameter (largest negative number)

  // command values for Win32 ShowWindow()
  // from http://msdn.microsoft.com/en-us/library/windows/desktop/ms633548(v=vs.85).aspx
  public static final int SW_FORCEMINIMIZE = 11;
  public static final int SW_HIDE = 0;
  public static final int SW_MAXIMIZE = 3;
  public static final int SW_MINIMIZE = 6;
  public static final int SW_RESTORE = 9;
  public static final int SW_SHOW = 5;
  public static final int SW_SHOWDEFAULT = 10;
  public static final int SW_SHOWMAXIMIZED = 3;
  public static final int SW_SHOWMINIMIZED = 2;
  public static final int SW_SHOWMINNOACTIVE = 7;
  public static final int SW_SHOWNA = 8;
  public static final int SW_SHOWNOACTIVATE = 4;
  public static final int SW_SHOWNORMAL = 1;


  // button labels
  public static final String BUT_LEFT = "left";
  public static final String BUT_RIGHT = "right";
  public static final String BUT_MIDDLE = "middle";
  public static final String BUT_MAIN = "main";
  public static final String BUT_MENU = "menu";
  public static final String BUT_PRIMARY = "primary";
  public static final String BUT_SECONDARY = "secondary";



  public static class LPPOINT extends Structure
  {
    public int X;
    public int Y;

    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[]{"X", "Y"});
    }
  }  // end of LPPOINT class


  // link to the DLL via JNA
  JAutoIt INSTANCE = (JAutoIt) Native.loadLibrary(
                                      AUUtils.getAutoItDLL(), JAutoIt.class,
                                      W32APIOptions.DEFAULT_OPTIONS);


  // JNA interfaces to AutoItX DLL functions
  public void AU3_Init();

  public int AU3_error();
  // get the error result of the last function call


  public int AU3_AutoItSetOption(String option, int val);
  // Set recommended default AutoIt functions/parameters
  // option: the option to change. See AutoIt help.
  // val: the value to assign to the option. 


  public void AU3_BlockInput(int nFlag);
  /* 1 = disable user input, 0 enable user input 
    (to have it run without interference) */



  public void AU3_CDTray(String szDrive, String szAction);
  // "drive:" and "open"/"closed"


  public void AU3_ClipGet(char[] szClip, int bufSize);

  public void AU3_ClipPut(String szClip);



  public int AU3_ControlClick(String title, String text, String control, String buttonLabel, 
                              int nNumClicks, int x, int y);
  // sends mouse click(s) to a given control
  // button: the button to click, "left", "right", "middle", "main", "menu", "primary", "secondary". Default is the left button.
  // numClicks: the number of times to click the mouse. Default is 1.
  // x: the x position to click within the control. Default is center.
  // y: the y position to click within the control. Default is center.
  //----------------------------------------------------------------------


  public void AU3_ControlCommand(String title, String text, String control, String cmd, 
                                 String extra, char[] result, int bufSize);

  public void AU3_ControlListView(String title, String text, String control, String cmd, 
                                  String extra1, String extra2, char[] result, int bufSize);

  public int AU3_ControlDisable(String title, String text, String control);
  // "grays out" a control

  public int AU3_ControlEnable(String title, String text, String control);

  public int AU3_ControlFocus(String title, String text, String control);
  // sets input focus to a given control on a window

  public void AU3_ControlGetFocus(String title, String text, 
                                  char[] controlWithFocus, int bufSize);

  public void AU3_ControlGetHandle(String title, String text, String control, 
                                   char[] handle, int bufSize);
  // retrieves the internal handle of a control

  public int AU3_ControlGetPosX(String title, String text, String control);
  // returns the x-position of a control relative to its window

  public int AU3_ControlGetPosY(String title, String text, String control);
  // returns the y-position of a control relative to its window

  public int AU3_ControlGetPosHeight(String title, String text, String control);
  // returns the height of a control relative to its window

  public int AU3_ControlGetPosWidth(String title, String text, String control);
  // returns the width of a control relative to its window

  public void AU3_ControlGetText(String title, String text, String control, 
                                 char[] szControlText, int bufSize);

  public int AU3_ControlHide(String title, String text, String control);

  public int AU3_ControlMove(String title, String text, String control, int x, int y, 
                             int width, int height);
  // Moves and resizes a control within a window

  public int AU3_ControlSend(String title, String text, String control, String msg, int mode);
  // sends a String of characters (msg) to a control
  // mode: Changes how the characters in the text are processed:
  // mode == 0 (default), text contains special characters like + to indicate
  //   SHIFT and {LEFT} to indicate left arrow.
  // mode == 1, keys are sent raw.
  //----------------------------------------------------------------------

  public int AU3_ControlSetText(String title, String text, String control, String controlText);

  public int AU3_ControlShow(String title, String text, String control);
  // Shows a control that was hidden



  public void AU3_DriveMapAdd(String device, String szShare, int nFlags, String user, 
                              String pwd, char[] result, int bufSize);

  public int AU3_DriveMapDel(String device);

  public void AU3_DriveMapGet(String device, char[] mapping, int bufSize);



  public int AU3_IniDelete(String szFilename, String section, String key);

  public void AU3_IniRead(String szFilename, String section, String key, 
                          String szDefault, char[] value, int bufSize);

  public int AU3_IniWrite(String szFilename, String section, String key, String value);



  public int AU3_IsAdmin();



  public int AU3_MouseClick(String buttonLabel, int x, int y, int nClicks, int speed);
  // Perform a mouse click
  // buttonLabel: the button to click: "left", "right", "middle", "main", "menu", "primary", "secondary".
   // nClicks: the number of times to click the mouse. Default is 1.
  // speed: the speed to move the mouse in the range 1 (fastest) to 100 (slowest). A speed of 0 will move the mouse instantly. Default speed is 10.
  //----------------------------------------------------------------------

  public int AU3_MouseClickDrag(String buttonLabel, int x1, int y1, int x2, int y2, int speed);
  // Perform a mouse click and drag from (x1,y1) to (x2,y2)

  public void AU3_MouseDown(String buttonLabel);

  public int AU3_MouseGetCursor();

  public int AU3_MouseGetPosX();
  // return the current x-position of the mouse cursor

  public int AU3_MouseGetPosY();
  // return the current y-position of the mouse cursor

  public int AU3_MouseMove(int x, int y, int speed);

  public void AU3_MouseUp(String buttonLabel);

  public void AU3_MouseWheel(String dir, int nClicks);


  public int AU3_Opt(String szOption, int val);



  public int AU3_PixelChecksum(int left, int top, int right, int bottom, int step);
  // Generates a checksum for a region of pixels

  public int AU3_PixelGetColor(int x, int y);
  // Returns a pixel color according to x,y coordinates

  public void AU3_PixelSearch(int left, int top, int right, int bottom, int nCol, 
                              int nVar, int step, LPPOINT pPointResult);



  public int AU3_ProcessClose(String id);
  // Terminates a named process
  // id: : the title or PID of the process to terminate.

  public int AU3_ProcessExists(String id);
  // Checks to see if a specified process exists

  public int AU3_ProcessSetPriority(String id, int priority);

  public int AU3_ProcessWait(String id, int timeOut);
  // Pauses script execution until a given process exists
  // timeout: Specifies how long to wait (in seconds). Default is to wait indefinitely.

  public int AU3_ProcessWaitClose(String id, int timeOut);
  // Pauses script execution until a given process does not exist.



  public int AU3_RegDeleteKey(String key);

  public int AU3_RegDeleteVal(String key, String value);

  public void AU3_RegEnumKey(String key, int instance, char[] result, int bufSize);

  public void AU3_RegEnumVal(String key, int instance, char[] result, int bufSize);

  public void AU3_RegRead(String key, String value, char[] result, int bufSize);

  public int AU3_RegWrite(String key, String value, String type, String typeValue);



  public int AU3_Run(String name, String dir, int showFlags);
  // Runs an external program
  // name: the name of the executable (EXE, BAT, COM, or PIF)
  // dir: the working directory
  // showflags: the "show" flag of the executed program:
  // SW_HIDE = Hidden window (or Default keyword)
  // SW_MINIMIZE = Minimized window; SW_MAXIMIZE = Maximized window
  //----------------------------------------------------------------------

  public int AU3_RunAsSet(String szUser, String szDomain, String szPassword, int nOptions);

  public int AU3_RunWait(String name, String dir, int showFlags);



  public void AU3_Send(String msg, int mode);
  // Sends simulated keystrokes to the active window.
  // mode: Changes how "keys" is processed:
  // mode == 0 (default), Text contains special chars like + and ! 
  // to indicate SHIFT and ALT key-presses.
  // mode == 1, keys are sent raw.
  //----------------------------------------------------------------------

  public int AU3_Shutdown(int nFlags);

  public void AU3_Sleep(int millisecs);
  // Pause script execution

  public void AU3_StatusbarGetText(String title, String text, int nPart, 
                                   char[] statusText, int bufSize);

  public void AU3_ToolTip(String tipText, int x, int y);
  // Creates a tooltip at (x, y) on the screen.



  public int AU3_WinActive(String title, String text);

  public void AU3_WinActivate(String title, String text);
  // Activates (gives focus to) a window

  public int AU3_WinClose(String title, String text);

  public int AU3_WinExists(String title, String text);

  public int AU3_WinGetCaretPosX();

  public int AU3_WinGetCaretPosY();

  public void AU3_WinGetClassList(String title, String text, char[] classList, int bufSize);

  public int AU3_WinGetClientSizeHeight(String title, String text);

  public int AU3_WinGetClientSizeWidth(String title, String text);

  public void AU3_WinGetHandle(String title, String text, char[] handle, int bufSize);
  // retrieves the internal handle of a window

  public int AU3_WinGetPosX(String title, String text);
  // returns the x-position of a window

  public int AU3_WinGetPosY(String title, String text);
  // returns the x-position of a window

  public int AU3_WinGetPosHeight(String title, String text);
  // returns the height of a window

  public int AU3_WinGetPosWidth(String title, String text);
  // returns the width of a window


  public void AU3_WinGetProcess(String title, String text, char[] szRetText, int bufSize);

  public int AU3_WinGetState(String title, String text);

  public void AU3_WinGetText(String title, String text, char[] szRetText, int bufSize);
  // retrieves the text from a window

  public void AU3_WinGetTitle(String title, String text, char[] szRetText, int bufSize);

  public int AU3_WinKill(String title, String text);


  public int AU3_WinMenuSelectItem(String title, String text, 
                                  String item1, String item2, String item3, 
                                  String item4, String item5, String item6, 
                                  String item7, String item8);

  public void AU3_WinMinimizeAll();

  public void AU3_WinMinimizeAllUndo();

  public int AU3_WinMove(String title, String text, int x, int y, int width, int height);
  // Moves and/or resizes a window

  public int AU3_WinSetOtop(String title, String text, int nFlags);

  public int AU3_WinSetState(String title, String text, int nFlags);

  public int AU3_WinSetTitle(String title, String text, String szNewTitle);

  public int AU3_WinSetTrans(String title, String text, int nTrans);

  public int AU3_WinWait(String title, String text, int timeOut); 
  // pauses execution of the script until the requested window exists

  public int AU3_WinWaitActive(String title, String text, int timeOut);
  // pauses execution of the script until the requested window is active


  public int AU3_WinWaitClose(String title, String text, int timeOut);

  public int AU3_WinWaitNotActive(String title, String text, int timeOut);


}  // end of JAutoIt class
