# COMP2042_CW_hcyam5
REMINDER: fter no games aved, find a way to go back to restarting the game code so that the paddle doesnt go wierd
REMINDER: Main.java line 157 -> change last level to 10 afterwards
Game Instructions:
- choco block gives +3 score
- heart block gives lives
- gold star block -> gold ball -> freezes lives for _ seconds
- s -> saves game

Compilation Instructions:

Implemented and Working Properly:
1. Game Icon added
2. "New Game" Image for button added
3. Window size is now fixed (does not extend to full screen)
4. Incorporating a "How To Play" button - switches scene to a guide on how to play the game,
    and then a back button switches the scene back to game scene.
5. After game is won, label "You win :)" is shown. Add a back button and an exit button to end program
6. Load button is functioning - allows saved game progress to be resumed.
7. 
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
- Game Engine Class
    - instead of .stop() to stop the threads (updateThread, timeThread and PhysicsThread) interrupt was used.
    - at the run functions for each thread, the catch section of the try-catch block is changed
      to return from the function instead of printing the stack trace.

Unexpected Problems:
1. java.lang.UnsupportedOperationException - happened after level 1, the blocks keep forming,
in an endless loop, does not configure the next level. 
   - How the problem was solved:
     Line 82 - 84 in the stop() function of the Game Engine class previously used .stop() to terminate the threads.
     This method is deprecated in Java as it may leave the application in an inconsistent state.
     I used .interrupt() to make the respective threads' run() functions (Lines 96,52,30) to throw an
     Interrupted Exception, and then returning from the functions to exit them.
2. When saving the game using (S), there was a FileNotFound Exception. This was due to the filepath storing in 
   the file path "D:/..." but not all laptops own a D drive.
   - How the problem was solved:
     In the main() function in the class Main, right before the start() function is called, I call a function named 
     setSavePaths(). It checks if the device has a D drive and alters the variables savePath and savePathDir accordingly.
     If the device does not have a D drive, it changes the filepath to the device's C drive.
3. Load button was present in the game code but did not appear in the game screen.
   - How the problem was solved:
     load button added to root for the first scene added to primaryStage. The load button functioned as expected.
4. If there was no saved game progress and "load game" button is pressed, the paddle moves to the top left of the screen
   and ball moves abnormally.
   - How the problem was solved:
     There is game progress saved only if the save.mdd file exists. When load button is pressed, the load button's
     setOnAction function checks if the save.mdd file exists. If it does, the file is read to resume that game, else
     the label "No previous games saved :<" appears.
5. Load game after game is over leads to loading a game with no bricks?
6. Labels keep freezing on screen