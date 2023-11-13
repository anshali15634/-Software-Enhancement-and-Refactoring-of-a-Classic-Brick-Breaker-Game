# COMP2042_CW_hcyam5
REMINDER: Main.java line 157 -> change last level to 10 afterwards
Game Instructions:
- choco block gives +3 score
- heart block gives lives
- gold star block -> gold ball -> freezes lives for _ seconds
- Capslock + s -> saves game

Compilation Instructions:

Implemented and Working Properly:
1. Game Icon added
2. "New Game" Image for button added
3. Window size is now fixed (does not extend to full screen)
4. Incorporating a "How To Play" button - switches scene to a guide on how to play the game,
    and then a back button switches the scene back to game scene.
5. After game is won, label "You win :)" is shown. Add a back button and an exit button to end program
Implemented but Not Working Properly:

Features Not Implemented(Yet):
1. New bonus power - laser shooting to bricks to break them
2. New bonus power - paddle elongates (change paddle image + increase size of paddle)
3. Blocks changing color when they are hit by ball, disappears after third hit?

New Java Classes:

Modified Java Classes:
- Main Class 
    - new variables were added, for the How To Play (about) button, and new scene for the "how to play" page was introduced.
    - start function was modified to add a new scene for How To Play, to restrict window size (window should not enlarge),
      to add a game icon, to add an extra button to the first scene of the game and to add images to the buttons an to add
      image as a background to the How To Play scene.
    - functions setSavePaths() and checkforDDrive() added

Unexpected Problems:
1. java.lang.UnsupportedOperationException - happened after level 1, the blocks keep forming,
in an endless loop, does not configure the next level. 
   - How the problem was solved:
     Line 82 - 84 in the stop() function of the Game Engine class previously used .stop() to terminate the threads.
     This method is deprecated in Java as it may leave the application in an inconsistent state.
     I used .interrupt() to make the respective threads' run() functions (Lines 96,52,30) to throw an
     Interrupted Exception, and then returning from the functions to exit them.
2. Exception in thread "JavaFX Application Thread" java.lang.NullPointerException: Cannot invoke "javafx.scene.Node.getScene()" because "<local2>" is null
Game freezes and an endless loop of errors occur, all at javafx.graphics@21.0.1.
   - How the problem was solved:
     Not solved yet. Ask how to find where the error line is using intellij?
3. Exception in thread "Thread-3" java.util.ConcurrentModificationException
   at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1095)
   at java.base/java.util.ArrayList$Itr.next(ArrayList.java:1049)
   at brickGame/brickGame.Main.onUpdate(Main.java:713)
   at brickGame/brickGame.GameEngine$1.run(GameEngine.java:33)
   at java.base/java.lang.Thread.run(Thread.java:1583)
4. Game moves to the next level even though all the bricks are not broken yet.
5. When saving the game using (Capslock+S), there was a FileNotFound Exception. This was due to the filepath storing in 
   the file path "D:/..." but not all laptops own a D drive.
   - How the problem was solved:
     In the main() function in the class Main, right before the start() function is called, I call a function named 
     setSavePaths(). It checks if the device has a D drive and alters the variables savePath and savePathDir accordingly.
     If the device does not have a D drive, it changes the filepath to the device's C drive.
