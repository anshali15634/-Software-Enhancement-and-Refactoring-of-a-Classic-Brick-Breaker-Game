# COMP2042_CW_hcyam5

>> checkHitToBlock() moved from Block class to Model because Block was in charge of initializing the blocks and
block-ball collision detection which violates the Single Class Single Responsibility Principle. Therefore,
the function was moved to Model as it comes under game logic.
>> goldBlock and heartBlock before get hit and then effect. Now they are power ups.
>> new Powers added: goldPower and heartPower

GOOD POINTS TO MENTION:
CopyOnWriteArrayList for blocks, bullets, and powerArray a allows concurrent modification and iteration without synchronization issues.

design pattern stuff done:
singleton pattern on classes game engine and model and view
template method pattern on power class and block class
simple factory design pattern implemented BlockFactory pattern.

## **Game Instructions:**
**CONTROLS:**
- Move paddle left: Left Arrow Key
- Move paddle right: Right Arrow Key
- Activate gun paddle: Down Arrow Key
- Shoot: Up Arrow Key

**GAMEPLAY:**
1. This game has 5 levels.
2. Break as many blocks as you can by bouncing the ball off the paddle.
3. Each block you break scores you points.
4. Watch out for powers that drop when certain blocks get destroyed:
   - _Star block_ releases a power that freezes your lives for 5 seconds.
   - _Heart block_ releases a power that increases your lives by one.
   - _Purple Pixel Block_ releases a power that shortens your paddle. If you want to remove this power,
     try to break another _Purple Pixel Block_. Catching the same power again changes
     your paddle size back to normal.
   - _Blue Pixel Block_ releases a power that flips your paddle controls. Left becomes right, and vice versa.
     But like the short paddle power, catching the same power again will reverse your controls back to normal.
   - _Choco Block_ releases a power that increases your score by 3.
5. The player can use their gun activation to shoot blocks. But the paddle will only shoot thrice throughout the whole
    game. This is kept track of by a Gun Meter, on the top of the screen.

## **Compilation Instructions:**

## **Features Implemented and Working Properly:**
1. Game Icon added
2. Window size is now fixed (does not extend to full screen).
3. Exit button added - closes window and exits program.
4. About button - has how to play instructions.
5. Separate start screen with game menu implemented.
6. Invert bonus - (A feature from the original Brick Breaker game) 
   If ball touches a dark-blue pixel block, it releases an invert bonus. If the paddle touches this bonus, 
   the paddle's controls reverse. If paddle's controls were already reversed, catching the bonus 
   again will reverse the paddle controls back to normal.
7. Short Paddle bonus - (the original Brick Breaker game had a feature that elongates the paddle, I have decided to
   implement the opposite to make the game harder)
   if the ball touches the dark-purple pixel block, it releases a short paddle bonus. 
   If paddle touches this bonus, the paddle shortens. If the paddle is already shortened catching the bonus again
   will reverse the bonus (paddle becomes old size again)
8. Pause feature implemented - press space bar to pause the game, and press space bar again to resume the game.
9. Gun feature - (a feature from the original BrickBreaker game) shoot blocks by first pressing the down key to 
   load ammunition, and press up key to shoot.

## **Features Implemented but Not Working Properly:**
- Gun feature: I wanted the bullet to strike only one block and disappear, but the bullet shoots all the blocks
  in its path before disappearing off-screen. Therefore, I implemented a limit so that it is only useable three
  times.

## **Features Not Implemented:**
1. Difficulty option (Easy, Medium, Hard)

## **New Java Classes:**

- Bullet Class

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
      the Main class, and the buttons in showWin and showGameOver().
    - Provides a convenient way to create buttons with associated images, coordinates, and styles for the application. 
      It encapsulates the common functionality required for creating game buttons in this game, reducing redundancy 
      and promoting code reuseability.

- View Class
  - It holds all the functions which were in the Main class, that were related to modifying the UI.
  - Its purpose is to:
    - 
  - Reasons why this class was created:
    - The Main class violates the Single-Class-Single-Responsibility Principle. This is because the modification of the
      UI, game logic, and storage of data was all executed in a single class. As a solution, I implemented the MVC
      pattern and therefore the View class was created.
    - This class handles the changes in the UI, like changing teh game background, initializing the labels for display,
      setting visibility of the objects, and filling in images for the objects.
    - This class was made as a singleton class as only one View object is required for the game, and this centralizes
      the configuration of the View class and prevents duplication of resources.
- Model Class
  - A singleton class which holds all the variables and functions relating to the game state and logic.
  - Its purpose is to:
    - 
  - Reasons why this class was created:
    -  single point of access to the game state from various parts of your application
    -  centralizes the game state, making it easier to manage and modify. Particularly useful since multiple components
       need to interact with or modify the game state.
    - ensures that there is only one instance of the game state, preventing issues related to multiple instances 
      having different states.


## **Modified Java Classes:**
_Note: Any Runnable() functions and EventHandler<ActionEvent>() in all classes were replaced with lambda expressions._
### **Main Class (renamed as Controller class since the MVC pattern was implemented)**
- Magic numbers avoided by adding NORMAL_PADDLE_WIDTH and SHORT_PADDLE_WIDTH static final class variables.
- isGoldStauts renamed as isGoldStats, sceneHeigt renamed as sceneHeight, and variables and functions with the word
colide in it was renamed to collide for clarity.
- Variables relating to paddle (breakWidth, breakHeight, halfBreakWidth, xBreak, yBreak, etc) renamed to paddleWidth,
paddleHeight, halfPaddleWidth, xPaddle, yPaddle,etc because the word "break" was ambiguous, it was hard to identify
that these variables were related to the paddle's game state.
- chocos array renamed as bonusArray - the name chocos can easily be mistaken as storing the choco blocks.
- choco was also renamed as bonus1, as new bonuses are to be introduced.
- Unused variables like v and oldXBreak were removed.
- New buttons (Exit, Load, About) were introduced and their actions were defined in the start() function.
- vX and vY control the speed of the ball (both stored 1). The speed value was increased.
- New scenes were added to the start() function for the new page that displayed the game instructions.
- New flags were introduced to change game background for the game start menu page and for the new game page.
- in method handle(), the sleepTime was set to 0, because when invert bonus is caught by paddle, it causes a lag in
inverting the controls.
- ballRadius's scope was changed from private to public final static, as the altered method checkHitToBlock() in
the Block class uses ballRadius to calculate more accurate ball-block collisions.
- According to Bob's Concise Coding Conventions, it should be possible to see the whole method from start to finish
without scrolling. This was not the case for methods setPhysicsToBall(), saveGame(), loadGame() and onUpdate().
- Therefore they were refactored, and have helper methods introduced to make the methods more modular and easier to
understand.- method setPhysicsToBall() has helper methods moveBall(), handleBallYBoundaries(), handleBallPaddleCollision(),
handleBallXBoundaries(), handleBallWallCollisions() and handleBallBlockCollision(). They are used to make the
method setPhysicsToBall() more maintainable and easy to read. Each helper method is responsible for a specific
aspect of the ball's behavior.
  - method saveGame() has helper methods saveGameInfo(), saveBlockInfo() and closeOutputStream().
  - method loadGame() has helper methods copyGameInfo() and copyBlockInfo().
  - method onUpdate() has helper methods handleBlockHit() and handleBlockType().
- As there were multiple duplicate blocks of code for starting the game engine, a function startEngine() was introduced
and utilised throughout the start() function.
- functions setSavePaths() and checkForDDrive() was introduced to save the game state into directory as per user's storage facilities.
- new logic for the bullet-block collisions from the gun paddle was added into the onUpdate() function.
- function handle() modified to include new functionalities of the paddle (down arrow key to activate gun paddle, up
arrow key to shoot), and the pause feature (spacebar).
- function togglePause() added to pause game engine and display pause label "Game Paused :)".
- initPaddle() function was modified to include the changing of the paddle width
(if the paddle caught the short power)
- initBoard() was shortened considerably by modularising it into two functions: generateBlockType() which would generate
a random block type and using the BlockFactory's createBlock() method to create the block.
- new function initMeter() was introduced to initialize the meter which keeps track of how many gun shots the player has left.
- initBullet() was introduced to initialize bullets whenever the gun paddle was activated.
- functions nextLevel() and restartGame() had similar code, so the duplicate code was modularised into a function called
resetFlags() and called into both functions. As resetFlags dealt with altering the game state, it was moved to the Model class.

**MVC pattern refactoring in the Main Class:** 
- Variables relating to game state like level, collide flags, sceneWidth, sceneHeight, ball (and its related variables),
isGoldStats, isExistHeartBlock, ballRadius, destroyedBlockCount, speed variables (vX and vY), heart, score, time-related
variables, engine and arrays for  blocks, bonuses (powers) were shifted to the Model class as they were related to the
game state. Private variables were encapsulated using get set functions if they were to be used in other classes.
- Class variables like LEFT, RIGHT remained in the Main (now Controller) class as they related to user input.
- Class variables like NO_HIT, HIT_RIGHT, etc were moved to the Block class as they are related to the block's 
game state.
- for the buttons, a class GameButton was introduced to reduce the amount of duplicated code for initializing
the buttons (setting positions, images, dimensions each of the five buttons).
The initialized buttons were moved to the View class as they dealt with the user interface.
- As there were multiple blocks of code for setVisible() for all the game objects in the start screen 
(game buttons and gameplay objects like ball, paddle, blocks), two functions were implemented: 
setVisibleGameObjects() and setNotVisibleGameObjects() to modularise the code. The functions were then 
moved to the View class as they dealt with the user interface.
- original move() function modularised by introducing a movePaddle() function which dealt with the altering the paddle's
variables. This movePaddle() function was then moved to the Model class as it dealt with paddle game logic and also
included the paddle invert power logic.
- onUpdate() function had multiple responsibilities, updating the score and heart labels, checking for collisions and
handling any block hits. Therefore helper methods were introduced to make the function more readable  
and modular: handleBlockType(), checkHitToBlock() and handleBlockHit().
  -  checkHitToBlock() dealt with game logic so it was moved to the Model class
  - handleBlockHit() function dealt with the Score class, since it had to interact with another class (Score) it was
  left in the Controller class.
  - handleBlockType() was interacting with the Power class (previously called Bonus class) so it was left in
  the Controller class as well.
  - onPhysicsUpdate() was modified to include the new powers (previously called bonuses). Although this is game logic
  and has to be in the Model class, it is interacting with multiple classes (View and Score) therefore it was not moved
  out of the Controller class.

### **GameEngine Class:**
  - Since GameEngine is only used once in the Main class, it was converted to a _singleton_ class. The getInstance method 
    is synchronized, which is good for ensuring that only one instance of GameEngine class is created even in a 
    multithreaded environment.
  - flag variable 'paused' introduced. Used by most threads in the GameEngine class to ensure the game is not
    paused before executing their respective instructions.
  - methods pause() and resume() added to alter the paused variable. This is later called in the Main class to 
    pause the game.

  
### **Block Class**
  - After doubling the speed of the ball, checkHitToBlock() was changed to increase accuracy of ball-block collisions.
    The old checkHitToBlock() method checked for exact positions of the ball relative to the block, and the ball
    sometimes moved behind the blocks. It was not robust enough to handle higher speeds. The new altered version of the
    method allows for a range of positions to be considered as hits and adjusts well to the new speed of the ball.
  - new block type called BLOCK_INVERT introduced - when hit by ball, gives the bonus of reversing the controls of 
    the paddle (when left arrow pressed, moves right, and vice versa.)
  - new block type called BLOCK_SHORT was introduced - when hit by the ball, gives the bonus of shortening the paddle.

### **LoadSave Class**
  - According to Bob's Concise Coding Conventions, it should be possible to see the whole method from start to finish,
    without scrolling. The read() method is too big to fit in a screen, therefore helper methods loadGameStats(),
    loadGameObjs() and loadGameFlags() were introduced to make the loading process more modular and easy-to-read.

### **Power Class (previously called the Bonus class)** 
  - (there are negative and positive powers, therefore the class name bonus was not appropriate)
  - Different powers have different images so the class had to include multiple if statements to decide which
    image was for each power type. This violated the _Open-Closed principle_. The Power class's draw() function
    has to be modified everytime a new bonus is added. Therefore, the _Template Method Design Pattern_ was used to modify
    the class. 
    - The Power class was modified to become an abstract class. The function chooseImage() is an abstract method to be
       implemented by the new concrete classes (scorePlusPower, invertPower, shortPaddlePower).
    - Now if new powers are to be added, the code is open for extension but closed for modification.
    
### **Score Class**
  - showWin() method altered to include a back button to navigate back to start menu after game is won.

  
## **Unexpected Problems:**
1. **java.lang.UnsupportedOperationException** - happened after level 1, the blocks keep forming,
   in an endless loop, does not configure the next level. 
   **- How the problem was solved:**
     In the stop() function of the Game Engine class previously used .stop() to terminate the threads.
     This method is deprecated in Java as it may leave the application in an inconsistent state.
     I used .interrupt() to make the respective threads' (updateThread, timeThread and PhysicsThread)
     run() functions to throw an Interrupted Exception, and then returning from the functions to exit them.

2. **When saving the game using (S), there was a FileNotFound Exception.** This was due to the filepath storing in 
   the file path "D:/..." but not all devices own a D drive.
   **- How the problem was solved:**
     In the main() function in the class Main, right before the start() function is called, I call a function named 
     setSavePaths(). It checks if the device has a D drive and alters the variables savePath and savePathDir accordingly.
     If the device does not have a D drive, it changes the filepath to the game file's relative directory.

3. **Load button was present in the game code but did not appear in the game screen.**
   **- How the problem was solved:**
     load button added to root for the first scene added to primaryStage. The load button functioned as expected.

4. **If there was no saved game progress and "load game" button is pressed, the paddle moves to the top left of the screen
   and ball moves abnormally.**
   **- How the problem was solved:**
     There is game progress saved only if the save.mdd file exists. Therefore, I altered the setOnAction function for the 
     load button to check if the save.mdd file exists. If it does, the file is read to resume that game, else
     the label "No previous games saved :<" appears.

5. **Labels keep freezing on screen.**
   **- How the problem was solved:**
     Thread.sleep(15) for animations is not the most efficient way to achieve animations on JavaFX, therefore I used 
     a built-in animation framework for smoother animations. Instead of manually updating the scale/position of the 
     label with a for loop to make animations with threading, I utilized TranslateTransition for movement and 
     FadeTransition for fading.

6. **Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException -** occurs when iterating
   over the collections blocks and bonusArray and making changes to the collections during iteration.
   **- How the problem was solved:**
     The ArrayLists blocks and bonusArray are declared as normal collections, which are not thread-safe and can
     cause problems if they are modified while they are being iterated over. Therefore, I declared both arrays as
     concurrent collections of type CopyOnWriteArrayList. This means that it is safe to modify the collections
     while you are iterating over it, so you will not get a ConcurrentModificationException.

7. **when the player loses all their lives, when Game Over is displayed, the hearts label is not updated to 0, remains at
   old value.**
   **- How problem was solved:**
     - In function handleBallYBoundaries(), in the if statement that confirms that the game is over, the program was
       altered to call the function onUpdate() for the last time to update the game screen before the game engine
       halts.

8. **When playing a loaded game, after all bricks are broken, sometimes the game continues without moving to the next 
   level.** 
   **- How the problem was solved:**
      - A logical error was present. This is because, in the read file we read the destroyedBlockCount value and the 
      blocks array stores only the blocks which have not been destroyed. If for example the player starts with 12 bricks, 
      and he breaks 7 bricks and saves the game, destroyedBlockCount stores 7, but when he reloads the game, the blocks 
      array size is now 5, as destroyed bricks are not loaded. We compare destroyedBlockCount and blocks array size (if they are equal) 
      to call nextLevel, but these values will never be the same.
      - in the function loadGameStats(), I overwrite destroyedBlockCount to 0. If destroyed blocks are not loaded into
      the game, there is no need to store the player's previous destroyedBlockCount.

9. **After incorporating the short paddle bonus, if there were many short paddle powers being caught by the paddle,
   the paddle glitches.**
   **- How the problem was solved:**
     The code responsible for changing the paddle width was updated to use `Platform.runLater()` each time the width was
     modified. This ensured that the UI updates occurred in a thread-safe manner. After applying the solution, the game 
     was tested, and the glitch associated with frequent paddle resizing was no longer observed. The movement of the 
     paddle remained smooth and glitch-free.

10. **If the game was saved while the ball was in gold form, the loaded game does not show the ball with gold ball image.**
    **- How the problem was solved:**
      The function which frequently updated the ball's features was the moveBall() function. Therefore, I modified
      this function to check the isGoldStats flag and change the image of the ball appropriately.

## **Implementation of the MVC pattern: Explanation of the separation of the original Main class code**
The original Main class code had the following purposes:
- sets up game menu
- sets up events for each button's press
- loads, saves, levels up and restarts the game
- initialize paddle, ball, board
- handles threads for ball physics and game logic
- stores the current game state variables (isGoldStats, heart, score, etc)
- handles different types of collisions (block-ball, block-wall, block-paddle)

After MVC pattern was implemented, the classes Model and View extracted their respective resposibilities
from the Main class.

> View class carried out the user interface related responsibilities, providing a clear separation of concerns.
It initialises the game buttons, sets the visibility of game objects, adds images to objects, changes the game 
background, updates the UI in real time for objects like the paddle, initializes the labels and updates them.

> Model class encapsulates the game state variables like heart, score, etc. This class also contains final class 
variables that store the measurements to be used consistently by all classes that require them, like sceneHeight,
ballRadius, etc. It also stores the game logic for moving the ball and paddle, handling collisions and boundaries
for the ball. 

> However, game logic that depends on user input and interaction with other classes like Model 
and View were retained in the Main class. For example, the function
that initializes the paddle depends on the bonus caught by the player, therefore needs to remain in the Main class as
it is dependent on user input and requires interaction with the View class. The other initialization functions 
(for the paddle and the ball) also remain in the Main class making it easier for developers (including future 
maintainers) to follow the sequence of events during game initialization.

