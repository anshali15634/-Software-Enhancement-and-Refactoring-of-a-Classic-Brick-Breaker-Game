NOTE: then fix concurr error

# COMP2042_CW_hcyam5
NOTE: ask later about what ss is needed for git - show an example?
REMINDER: change final level to 5 afterwards.
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
6. Penalty power - inverted movement of paddle till level finishes

New Java Classes:

Modified Java Classes:
Note: Any Runnable() functions and EventHandler<ActionEvent>() in all classes were replaced with lambda expressions.
- Main Class 
    - new variables were added (LIST LATER) and new scene for the "how to play" page was introduced.
    - start function was modified to add a new scene for How To Play, to restrict window size (window should not enlarge),
      to add a game icon, to add an extra button to the first scene of the game and to add images to the buttons an to add
      image as a background to the How To Play scene.
  
    - methods setSavePaths() and checkforDDrive() added to set the file path for game file saving the game progress.
      checkforDDrive() method checks if the device has a DDrive.
      setSavePaths() uses the function checkforDDrive() to set the file path for save.mdd. If D drive does not exist,
      it saves the game file in the device's C drive.

    - ballRadius's scope was changed from private to public final static, as the altered method checkHitToBlock() in 
      the Block class uses ballRadius to calculate more accurate ball-block collisions

    - two methods setVisibleGameObjects() and setNotVisibleGameObjects() made to replace repetitive blocks of code
      for setting visibility of game objects. 

    - According to Bob's Concise Coding Conventions, it should be possible to see the whole method from start to finish
      without scrolling. This was not the case for methods setPhysicsToBall(), saveGame(), loadGame() and onUpdate(). 
    - Therefore they were refactored, and have helper methods introduced to make the methods more modular and easier to
      understand.
      - method setPhysicsToBall() has helper methods moveBall(), handleBallYBoundaries(), handleBallPaddleCollision(), 
        handleBallXBoundaries(), handleBallWallCollisions() and handleBallBlockCollision(). They are used to make the 
        method setPhysicsToBall() more maintainable and easy to read. Each helper method is responsible for a specific
        aspect of the ball's behavior.

      - method saveGame() has helper methods saveGameInfo(), saveBlockInfo() and closeOutputStream().
      - method loadGame() has helper methods copyGameInfo() and copyBlockInfo().
      - method onUpdate() has helper methods handleBlockHit() and handleBlockType().

  
- Block Class
  - After doubling the speed of the ball, checkHitToBlock() was changed to increase accuracy of ball-block collisions.
    The old checkHitToBlock() method checked for exact positions of the ball relative to the block, and the ball
    sometimes moved behind the blocks. It was not robust enough to handle higher speeds. The new altered version of the
    method allows for a range of positions to be considered as hits and adjusts well to the new speed of the ball.

- LoadSave Class
  - According to Bob's Concise Coding Conventions, it should be possible to see the whole method from start to finish,
    without scrolling. The read() method is too big to fit in a screen, therefore helper methods loadGameStats(),
    loadGameObjs() and loadGameFlags() were introduced to make the loading process more modular and understandable.
    
  
Unexpected Problems:
1. java.lang.UnsupportedOperationException - happened after level 1, the blocks keep forming,
   in an endless loop, does not configure the next level. 
   - How the problem was solved:
     In the stop() function of the Game Engine class previously used .stop() to terminate the threads.
     This method is deprecated in Java as it may leave the application in an inconsistent state.
     I used .interrupt() to make the respective threads' (updateThread, timeThread and PhysicsThread)
     run() functions to throw an Interrupted Exception, and then returning from the functions to exit them.

2. When saving the game using (S), there was a FileNotFound Exception. This was due to the filepath storing in 
   the file path "D:/..." but not all devices own a D drive.
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
     There is game progress saved only if the save.mdd file exists. Therefore, I altered the setOnAction function for the 
     load button to check if the save.mdd file exists. If it does, the file is read to resume that game, else
     the label "No previous games saved :<" appears.

5. Labels keep freezing on screen.
   - How the problem was solved:
     Thread.sleep(15) for animations is not the most efficient way to achieve animations on JavaFX, therefore I used 
     a built-in animation framework for smoother animations. Instead of manually updating the scale/position of the 
     label with a for loop to make animations with threading, I utilized TranslateTransition for movement and 
     FadeTransition for fading.

6. Load game after game is over leads to loading a game with no bricks?

7. [Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException - occurs when iterating
   over blocks array and making changes to the array during iteration. This is done multiple times in the Main class's
   methods.]
   - [How the problem was solved:
     Iterations over the block array were modified to only make changes to the original array after the iterations were
     over. This was done to the loadGame() function.
     NEED TO DO FOR THE onUpdate() FUNCTION TOO]


- check if each function only has one task, if have more than one
    then should refactor and make separate functions.
- no method should have more than 5 levels of indentation.
  - if this is the case then make new helper methods.
- each line should not have more than 80 chars (should fit inside your screen)
- Class Variable Names
- all class variables have get set methods.
- all class variables should be private
- methods should not have more than 5 parameters. The more parameters, less reusable
    then you need to group all the parameters in a new class
- use lambda expressions to simplify the syntax for anonymous inner classes.


