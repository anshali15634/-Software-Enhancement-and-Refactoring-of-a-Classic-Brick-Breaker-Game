# COMP2042_CW_hcyam5
NOTE: ask later about what ss is needed for git - show an example?
REMINDER: Main.java line 157 -> change last level to 10 afterwards
Game Instructions:
- choco block gives +3 score
- heart block gives lives
- gold star block -> gold ball -> freezes lives for _ seconds
- s -> saves game

Compilation Instructions:

Features Implemented and Working Properly:
1. Game Icon added
2. Window size is now fixed (does not extend to full screen).
3. Exit button added - closes window and exits program.
4. Incorporated an "About" button - has how to play instructions.
5. Load button is functioning - allows saved game progress to be resumed.
6. Separate start screen with game menu implemented.

DO THESE COUNT AS FEATURES:
7. Speed of the ball doubled
8. Better ball-block collision detection (previous code allowed ball to move behind blocks)


Features Implemented but Not Working Properly:

Features Not Implemented(Yet):
1. New bonus power - laser shooting to bricks to break them
2. New bonus power - paddle elongates (change paddle image + increase size of paddle)
3. Blocks changing color when they are hit by ball, disappears after third hit?
4. After game is won, label "You win :)" is shown. Add a back button and an exit button to end program
5. pause button?

New Java Classes:

Modified Java Classes:
Note: Any Runnable() functions and EventHandler<ActionEvent>() in all classes were replaced with lambda expressions.
- Main Class 
    - new variables were added, for the How To Play (about) button, and new scene for the "how to play" page was introduced.
    - start function was modified to add a new scene for How To Play, to restrict window size (window should not enlarge),
      to add a game icon, to add an extra button to the first scene of the game and to add images to the buttons an to add
      image as a background to the How To Play scene.
    - functions setSavePaths() and checkforDDrive() added
    - ballRadius's scope was changed from private to public final static, as the altered function checkHitToBlock() in 
      the Block class uses ballRadius to calculate more accurate ball-block collisions
    - two functions setVisibleGameObjects() and setNotVisibleGameObjects() made to replace repetitive blocks of code
      for setting visibility of game objects. 

- Game Engine Class
    - instead of .stop() to stop the threads (updateThread, timeThread and PhysicsThread) interrupt was used.
    - at the run functions for each thread, the catch section of the try-catch block is changed
      to return from the function instead of printing the stack trace.
  
- Block Class
  - After doubling the speed of the ball, checkHitToBlock() was changed to increase accuracy of ball-block collisions.
    The old checkHitToBlock() method is checking for exact positions of the ball relative to the block, and it was
    not robust enough to handle higher speeds. The new function allows for a range of positions to be considered
    as hits and adjusts well to the new speed of the ball.
  
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

5. Labels keep freezing on screen. Thread.sleep(15) for animations is not the most efficient way to achieve animations
   on JavaFX, therefore I used a built-in animation framework for smoother animations.
      - How the problem was solved:
      Instead of manually updating the scale/position of the label with a for loop to make animations with threading,
      I utilized TranslateTransition for movement and FadeTransition for fading.

6. Load game after game is over leads to loading a game with no bricks?
7. Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException - occurs when iterating
    over blocks array and making changes to the array during iteration?


- check if each function only has one task, if have more than one
    then should refactor and make separate functions. 
  - For example if we have a function moveBall() which checks boundaries of the walls of the game, changes ball's x and
    y coordinates, and checks for collisions with the blocks, then the function moveBall should be refactored 
    to only do one task (change x and y coordinates) and should call functions checkBoundary()and collisionBlocks()
    to complete its task
- no method should have more than 5 levels of indentation.
  - if this is the case then make new helper methods.
- each line should not have more than 80 chars (should fit inside your screen)
- Class Variable Names
- all class variables have get set methods.
- all class variables should be private
- methods should not have more than 5 parameters. The more parameters, less reusable
    then you need to group all the parameters in a new class
- use lambda expressions to simplify the syntax for anonymous inner classes.

