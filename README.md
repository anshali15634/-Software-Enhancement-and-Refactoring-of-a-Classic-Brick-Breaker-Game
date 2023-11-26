>> choco array stores all the choco blocks, when hit s
# COMP2042_CW_hcyam5
NOTE: ask later about what ss is needed for git - show an example?
REMINDER: change final level to 5 afterwards.
**Game Instructions:**
- choco block gives +3 score
- heart block gives lives
- gold star block -> gold ball -> freezes lives for _ seconds
- s -> saves game

**Compilation Instructions:**

**Features Implemented and Working Properly:**
1. Game Icon added
2. Window size is now fixed (does not extend to full screen).
3. Exit button added - closes window and exits program.
4. About button - has how to play instructions.
6. Separate start screen with game menu implemented.
7. Invert bonus - if ball touches a dark-blue pixel block, it releases an invert bonus.
   if the paddle touches this bonus, the paddle's controls reverse. (feature taken from the original BrickBreaker game)
   If paddle's controls were already reversed, catching the bonus again will reverse the paddle controls back to 
   normal.
8. Short Paddle bonus - if the ball touches the dark-purple pixel block, it releases a short paddle bonus.
   If paddle touches this bonus, the paddle shortens. If the paddle is already shortened catching the bonus again
   will reverse the bonus (paddle becomes old size again)

**Features Implemented but Not Working Properly:**

**Features Not Implemented(Yet):**
1. New bonus power - laser shooting to bricks to break them
2. New bonus power - paddle elongates (change paddle image + increase size of paddle)
3. After game is won, label "You win :)" is shown. Add a back button and an exit button to end program
4. pause button?

**New Java Classes:**

- GameButton Class
  - It inherits from the existing Button class.
  - Its purpose is to:
    - store the image file name for the button
    - set the image to the button
    - set the height of the button
    - preserve the ratio of height and width of image
    - sets the background color of the button
    - sets the X and Y coordinates of the button
  - Reason why class was created:
    - To replace the repetitive blocks of code for each button's graphic settings in the start() function of
      the Main class.

**Modified Java Classes:**
Note: Any Runnable() functions and EventHandler<ActionEvent>() in all classes were replaced with lambda expressions.
- Main Class 
    - isGoldStauts renamed as isGoldStatus, sceneHeigt renamed as sceneHeight, and variables and functions with the word
      colide in it was renamed to collide for clarity.
    - getter methods for sceneWidth and sceneHeight included because in Score class, the labels for messages are to be 
      centered, therefore scene dimensions are needed.
    - paddleWidth is no longer final as it is changed according to the shortPaddle bonus.
      Setter method for paddleWidth added.
    - new scene for the "how to play" page was introduced.
    - new flag variable invert introduced to inform other functions if paddle touches the invert bonus.
    - variables related to the paddle (xBreak, yBreak, breakWidth, etc ) were changed to include the word 'paddle' 
      instead (eg. xPaddle, yPaddle, paddleWidth, etc) as the word 'break' was ambiguous.
    - start function was modified to add a new scene for How To Play, to restrict window size (window should not enlarge),
      to add a game icon.
    - chocos array renamed as bonusArray - the name chocos can easily be mistaken as storing the choco blocks.
    - choco was also renamed as bonus1, as new bonuses are to be introduced.
    - Unused variables like v and oldXBreak were removed.
    - vX and vY control the speed of the ball (both stored 1). The speed value was doubled. (Now they both store 2 and 
      2.5 respectively)
    - for the buttons, a class GameButton was introduced to reduce the amount of duplicated code for adding images
      to the buttons. The code was refactored to adjust to the inclusion of the new class.
  
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
  - new block type called BLOCK_INVERT introduced - when hit by ball, gives the bonus of reversing the controls of 
    the paddle (when left arrow pressed, moves right, and vice versa.)
  - new block type called BLOCK_SHORT was introduced - when hit by the ball, gives the bonus of shortening the paddle.

- LoadSave Class
  - According to Bob's Concise Coding Conventions, it should be possible to see the whole method from start to finish,
    without scrolling. The read() method is too big to fit in a screen, therefore helper methods loadGameStats(),
    loadGameObjs() and loadGameFlags() were introduced to make the loading process more modular and easy-to-read.

- Bonus Class
  - new variable bonusType to identify the type of bonus - eg: +3, invert, short paddle etc.
  - bonusType added as parameter to constructor.
  - using bonusType to identify type of bonus, the image is added to the bonus in the draw() function.
    
- Score Class
  - showWin() method altered to include a back button to navigate back to start menu after game is won.
  
**Unexpected Problems:**
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

6. Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException - occurs when iterating
   over the collections blocks and bonusArray and making changes to the collections during iteration.
   - How the problem was solved:
     The ArrayLists blocks and bonusArray are declared as normal collections, which are not thread-safe and can
     cause problems if they are modified while they are being iterated over. Therefore, I declared both arrays as
     concurrent collections of type CopyOnWriteArrayList. This means that it is safe to modify the collections
     while you are iterating over it, so you will not get a ConcurrentModificationException.

7. when the player loses all their lives, when Game Over is displayed, the hearts label is not updated to 0, remains at
   old value.
   - How problem was solved:
     - In function handleBallYBoundaries(), in the if statement that confirms that the game is over, the program was
       altered to call the function onUpdate() for the last time to update the game screen before the game engine
       halts.

8. When playing a loaded game, after all bricks are broken, sometimes the game continues without moving to the next 
   level. 
   - How the problem was solved:
      - A logical error was present. This is because, in the read file we read the destroyedBlockCount value and the 
      blocks array stores only the blocks which have not been destroyed. If for example the player starts with 12 bricks, 
      and he breaks 7 bricks and saves the game, destroyedBlockCount stores 7, but when he reloads the game, the blocks 
      array size is now 5, as destroyed bricks are not loaded. We compare destroyedBlockCount and blocks array size (if they are equal) 
      to call nextLevel, but these values will never be the same.
      - in the function loadGameStats(), I overwrite destroyedBlockCount to 0. If destroyed blocks are not loaded into
      the game, there is no need to store the player's previous destroyedBlockCount.

9. After incorporating the short paddle bonus, if there were multiple of that same bonus being caught by the paddle
   the paddle glitches.
   - How the problem was solved:
     The code responsible for changing the paddle width was updated to use `Platform.runLater()` each time the width was
     modified. This ensured that the UI updates occurred in a thread-safe manner. After applying the solution, the game 
     was tested, and the glitch associated with frequent paddle resizing was no longer observed. The movement of the 
     paddle remained smooth and glitch-free.



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
