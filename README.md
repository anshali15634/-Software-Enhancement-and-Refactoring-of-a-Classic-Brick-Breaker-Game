# COMP2042_CW_hcyam5

## **Game Instructions:**
**CONTROLS:**
- Move paddle left: **Left Arrow Key**
- Move paddle right: **Right Arrow Key**
- Activate gun paddle: **Down Arrow Key**
- Shoot: **Up Arrow Key**

**GAMEPLAY:**
1. This game has **5 levels**.
2. Break as many blocks as you can by **bouncing the ball off the paddle**.
3. Each block you break scores you **points**.
4. Watch out for powers that drop when certain blocks get destroyed:
   - _Star block_ releases a power that freezes your lives for **5 seconds**.
   - _Heart block_ releases a power that increases your lives by **one**.
   - _Purple Pixel Block_ releases a power that shortens your paddle. If you want to remove this power,
     try to break another _Purple Pixel Block_. Catching the same power **again** changes
     your paddle size back to **normal**.
   - _Blue Pixel Block_ releases a power that **flips** your paddle controls. Left becomes right, and vice versa.
     But like the short paddle power, catching the same power **again** will reverse your controls back to **normal**.
   - _Choco Block_ releases a power that increases your score by 3.
5. The player can use their **gun activation** to shoot blocks. But the paddle will only shoot **thrice** throughout the whole
    game. This is kept track of by a **Gun Meter**, on the top of the screen.
6. The player has to first activate the gun by **pressing the down arrow key** and then shoot using the **up arrow key**.

## **Compilation Instructions:**
Import project into your IDE.
Set the Main Class in Run Configurations to brickGame.Controller
Compile and run the game directly.

## **Features Implemented and Working Properly:**
1. Game Icon added to Window - top left of window and application icon displays the BrickBreaker logo.
2. Window size is now fixed (does not extend to full screen). 
3. Separate start screen with game menu implemented.
4. About button added - displays how to play instructions.
5. Exit button added - closes window and exits program.
6. Invert power - (A feature from the original Brick Breaker game) 
   If ball breaks a dark-blue pixel block, it releases an invert power. If the paddle catches this power, 
   the paddle's controls reverse. If paddle's controls were already reversed, catching the bonus 
   again will reverse the paddle controls back to normal.
7. Short Paddle power - (the original Brick Breaker game had a power that elongates the paddle, I have decided to
   implement the opposite to make the game harder)
   if the ball breaks the dark-purple pixel block, it releases a short paddle power. 
   If paddle catches this power, the paddle shortens. If the paddle is already shortened by catching that power previously,
   catching the power again will reverse the power (paddle becomes normal size).
8. Pause feature implemented - press space bar to pause the game, and press space bar again to resume the game.
9. Gun feature - (a feature from the original BrickBreaker game) shoot blocks by first pressing the down key to 
   activate the gun paddle, and press up key to shoot. This is only allowed three times throughout the gameplay.
10. A Gun Meter is located on top of the screen to keep track of how many more gun shots are left for the player.
11. In the original version of this game, a star block freezes lives, while hitting a heart block increases lives by 1.
In this version, players need to catch these power-ups with their paddle to activate their effects.

## **Features Implemented but Not Working Properly:**
- Gun feature: The bullet should strike only one block and disappear, but the bullet shoots all the blocks
  in its path before disappearing off-screen.
- Therefore, I implemented a limit so that it is only useable three times.

## **Features Not Implemented:**
I wanted to implement a surprise feature where the bricks suddenly fall during gameplay, which the player has to dodge 
or destroy with their gun paddle activation. If the brick falls on the paddle, it is game over.
However, since there were multiple powers already in the game which make the paddle hard to control (short paddle and 
inverting paddle controls), it would disrupt the game's balance and make it too difficult, therefore this feature was
left out.

## **New Java Classes:**
### BlockFactory Class
(is located in the BlockFactory.java file)
  - Implements the **Factory Method Design Pattern**
  - Its purpose is to:
    - Centralize block creation logic: Instead of having individual classes responsible for creating their own instances, 
    the BlockFactory acts as a centralized location for this logic. This promotes code reuse and simplifies maintenance.
    - Dynamic block creation: The factory pattern allows for creating different types of blocks based on a single method
      call. This makes the code more flexible and adaptable to different game scenarios.
  - Reasons why this class was created:
    - Improved code organization.
    - Flexibility. Adding new block types only requires updating the factory method without modifying existing code.
    - To reduce duplication of code for block initialization in the Controller class. 
    - Maintainability. Any changes to the block creation process can be made in one place, simplifying future updates.

### Bullet Class
(is located in the Bullet.java file)
  - inherits from the Rectangle class.
  - Its purpose is to:
    - Manage the state of the bullet, including whether it has been destroyed, which is crucial for game logic 
      and collision handling.
  - Reasons why this class was created:
    - Improve code clarity and make it easier to understand and maintain the code related to bullets specifically by 
      separating bullets into their own class.

### GameButton Class
(is located in the GameButton.java file)
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

### View Class
(is located in the View.java file)
  - It holds all the functions which were in the Main class, which were related to modifying the UI.
  - This class was made as **a singleton class** as only one View object is required for the game, and this centralizes
    the configuration of the View class and prevents duplication of resources.
  - Its purpose is to:
    - Manage user interface elements like buttons, labels, and images.
    - Initialize and update UI components with game state information (score, level, heart).
    - Set and update visual appearance of game objects (e.g., fill images for bullet, gun meter).
    - Control visibility of game objects depending on game state (e.g., hide buttons during gameplay).
    - Access and modify game elements based on user interactions (e.g., updating gun meter).
    - Add a game icon to the top left corner of the game window.
    - Ensure smooth UI updates on the JavaFX thread.
    
  - Reasons why this class was created:
    - The Main class violates the **Single-Class-Single-Responsibility Principle**. This is because the modification of the
      UI, game logic, and storage of data was all executed in the Main class. As a solution, I implemented the **MVC
      pattern** and therefore the View class was created to separate the responsibilities.
    - To create reusable functions for UI, like the gameObjectImageFill() function which prevents duplication of code
      everytime a new object has to have an image pattern.
    - Centralized UI management: Manage all UI elements in one place, simplifying code organization and modification.


### Model Class
(is located in the Model.java file)
  - A **singleton** class which holds all the variables and functions relating to the game state and logic.
  - Its purpose is to:
    - separate the data and logic from the user interface (View) and the user input handling (Controller)
    - store and manage all game-related data (ball position and movement parameters, score and heart count, etc).
    - implements core game mechanics including:
      - Ball movement and collision detection 
      - Paddle movement and collision handling 
      - Block destruction from ball or bullet and scoring 
      - Power-up activation and effects 
      - Level progression and game state changes
  - Reasons why this class was created:
    - single point of access to the game state from various parts of your application
    - centralizes the game state, making it easier to manage and modify. Particularly useful since multiple components
       need to interact with or modify the game state.
    - ensures that there is only one instance of the game state, preventing issues related to multiple instances 
      having different states.
  
### BlockChoco Subclass
(is located in the Block.java file, **lines 78-92**)
- inherits from abstract class Block
- This subclass was created to define the block's own specific power (scorePlusPower) and its block image file.

### BlockHeart Subclass
(is located in the Block.java file, **lines 93-107**)
- inherits from abstract class Block
- This subclass was created to define the block's own specific power (heartPower) and its block image file.

### BlockInvert Subclass
(is located in the Block.java file, **lines 108-122**)
- inherits from abstract class Block
- This subclass was created to define the block's own specific power (invertPower) and its block image file.

### BlockShort Subclass
(is located in the Block.java file, **lines 138-152**)
- inherits from abstract class Block
- This subclass was created to define the block's own specific power (shortPaddlePower) and its block image file.

### BlockStar Subclass
(is located in the Block.java file, **lines 154-168**)
- inherits from abstract class Block
- This subclass was created to define the block's own specific power (goldPower) and its block image file.

### BlockPlain Subclass
(is located in the Block.java file, **lines 123-137**)
- inherits from abstract class Block
- This subclass was created to define the block's own specific power (null) and its block image file.

### goldPower Subclass
(is located in the Power.java file, **lines 105-119**)
- inherits from abstract class Power
- This subclass was created to define the power's image file 
and its display message "GOLD BALL - FREEZE LIVES :>" when the paddle catches this power.

### heartPower Subclass
(is located in the Power.java file, **lines 90-104**)
- inherits from abstract class Power
- This subclass was created to define the power's image file
and its display message "ONE MORE LIFE!" when the paddle catches this power.

### invertPower Subclass
(is located in the Power.java file, **lines 75-89**)
- inherits from abstract class Power
- This subclass was created to define the power's image file
and its display message "INVERTED PADDLE CONTROLS :>" when the paddle catches this power.

### scorePlusPower Subclass
(is located in the Power.java file, **lines 54-73**)
- inherits from abstract class Power
- This subclass was created to define the power's image file
and its display message "+3" when the paddle catches this power.

### shortPaddlePower Subclass
(is located in the Power.java file, **lines 39-53**)
- inherits from abstract class Power
- This subclass was created to define the power's image file
and its display message "CAREFUL! PADDLE CHANGE!" when the paddle catches this power.

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
- choco was also renamed as pow, as new bonuses (now renamed as powers) are to be introduced.
- Unused variables like v and oldXBreak were removed.
- New buttons (Exit, Load, About) were introduced and their actions were defined in the start() function.
- vX and vY control the speed of the ball (both stored 1). The speed value was increased.
- New scene were added to the start() function for the new page that displayed the game instructions.
- New flags were introduced to change game background for the game start menu page and for the new game page.
- in method handle(), the sleepTime was set to 0, because when invert bonus is caught by paddle, it causes a lag in
inverting the controls.
- ballRadius's scope was changed from private to public final static, as the altered method checkHitToBlock() in
the Block class uses ballRadius to calculate more accurate ball-block collisions.
- According to Bob's Concise Coding Conventions, it should be possible to see the whole method from start to finish
without scrolling. This was not the case for methods setPhysicsToBall(), saveGame(), loadGame(), onUpdate() and onPhysicsUpdate.
- Therefore they were refactored, and have helper methods introduced to make the methods more modular and easier to
understand.
    - method setPhysicsToBall() has helper methods moveBall(), handleBallYBoundaries(), handleBallPaddleCollision(),
  handleBallXBoundaries(), handleBallWallCollisions() and handleBallBlockCollision(). They are used to make the
  method setPhysicsToBall() more maintainable and easy to read. Each helper method is responsible for a specific
  aspect of the ball's behavior.
    - method saveGame() has helper methods saveGameInfo(), saveBlockInfo() and closeOutputStream().
    - method loadGame() has helper methods copyGameInfo() and copyBlockInfo().
    - method onUpdate() has helper methods handleBlockHit() and handleBlockType().
    - method onPhysicsUpdate() has the helper method handlePowerType().
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
- Class was renamed to Controller.
- Variables relating to game state like level, collide flags, sceneWidth, sceneHeight, ball (and its related variables),
isGoldStats, isExistHeartBlock, ballRadius, destroyedBlockCount, speed variables (vX and vY), heart, score, time-related
variables, engine and arrays for  blocks, bonuses (powers) were shifted to the Model class as they were related to the
game state. Private variables were encapsulated using get set functions if they were to be used in other classes.
- Class variables like LEFT, RIGHT remained in the Main (now Controller) class as they related to user input.
- Class variables like NO_HIT, HIT_RIGHT, etc were moved to the Block class as they are related to the block's 
game state.
- ArrayLists for blocks, bullets and powers (blocks, bullets and powerArray) were re-defined as type 
CopyOnWriteArrayList as it allows concurrent modification and iteration without synchronization issues. These arrays 
were then moved to the Model class as they are a part of the game state.
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
handling any block hits. Therefore, helper methods were introduced to make the function more readable  
and modular: handleBlockType(), checkHitToBlock() and handleBlockHit().
  -  checkHitToBlock() dealt with game logic, so it was moved to the Model class
  - handleBlockHit() function dealt with the Score class, since it had to interact with another class (Score) it was
  left in the Controller class.
  - handleBlockType() was interacting with the Power class (previously called Bonus class) so it was left in
  the Controller class as well.
- After doubling the speed of the ball, checkHitToBlock() was changed to increase accuracy of ball-block collisions. 
The old checkHitToBlock() method checked for exact positions of the ball relative to the block, and the ball
sometimes moved behind the blocks. It was not robust enough to handle higher speeds. The new altered version of the
method allows for a range of positions to be considered as hits and adjusts well to the new speed of the ball.
- onPhysicsUpdate() was modified to include the new powers (previously called bonuses). Although this is game logic
and has to be in the Model class, it is interacting with multiple classes (View and Power) therefore it was not moved
out of the Controller class.

### **GameEngine Class:**
  - Since GameEngine is only used once in the Main class, it was converted to a **singleton** class. The getInstance method 
    is synchronized, which is good for ensuring that only one instance of GameEngine class is created even in a 
    multithreaded environment.
  - flag variable 'paused' introduced. Used by most threads in the GameEngine class to ensure the game is not
    paused before executing their respective instructions.
  - methods pause() and resume() added to alter the paused variable. This is later called in the Main class to 
    pause the game.

  
### **Block Class**
  - new block type called BLOCK_INVERT introduced - when hit by ball, gives the bonus of reversing the controls of 
    the paddle (when left arrow pressed, moves right, and vice versa.)
  - new block type called BLOCK_SHORT was introduced - when hit by the ball, gives the bonus of shortening the paddle.
  - Since each block is associated with a power, the Power object was added to the class as a public variable.
  - This class violates the **Open-Closed Principle**. Whenever a new type of block needs to be added, the original
    code has to be modified. The **Template Method Design Pattern** was implemented to solve this.
    - The Block class was modified to become an abstract class. The function draw() and initPower() are abstract 
    methods. 
    - draw() fills in the block with the appropriate image.
    - initPower initialises the Power object pow to a concrete class (goldPower, heartPower, etc) according to the 
      concrete class the block is from (BlockChoco, BlockStar, etc).
  - There are 6 concrete classes which inherit from the abstract class Block, each representing a block type:
    BlockChoco, BlockHeart, BlockInvert, BlockPlain, BlockShort, BlockStar.
  - If new block types need to be added, this class is open for extension and closed for modification.
    
### **LoadSave Class**
  - According to Bob's Concise Coding Conventions, it should be possible to see the whole method from start to finish,
    without scrolling. The read() method is too big to fit in a screen, therefore helper methods loadGameStats(),
    loadGameObjs() and loadGameFlags() were introduced to make the loading process more modular and easy-to-read.

### **Power Class (previously called the Bonus class)** 
  - (there are negative and positive powers, therefore the class name bonus was not appropriate)
  - Different powers have different images so the class had to include multiple if statements to decide which
    image was for each power type. This violated the **Open-Closed principle**. The Power class's draw() function
    has to be modified everytime a new bonus is added. Therefore, the **Template Method Design Pattern** was used to modify
    the class. 
    - The Power class was modified to become an **abstract** class. The function chooseImage() is an abstract method to be
       implemented by the new concrete classes (scorePlusPower, invertPower, shortPaddlePower, heartPower,goldPower).
    - Now if new powers are to be added, the code is open for extension but closed for modification.

    
### **Score Class**
  - showWin() method altered to include a back button to navigate back to start menu after game is won.


## **Summary of the design patterns implemented:**
1. _Singleton Design pattern_ was implemented on the classes GameEngine, Model and View
2. _Template Method Design pattern_ was implemented on the Power class and Block class
3. _Factory Design Pattern_ was implemented on the BlockFactory class.

## **Unexpected Problems:**
1. **java.lang.UnsupportedOperationException** - happened after level 1, the blocks keep forming,
   in an endless loop, does not configure the next level. 

   **How the problem was solved:**
     - In the stop() function of the Game Engine class previously used .stop() to terminate the threads.
     This method is deprecated in Java as it may leave the application in an inconsistent state.
     I used .interrupt() to make the respective threads' (updateThread, timeThread and PhysicsThread)
     run() functions to throw an Interrupted Exception, and then returning from the functions to exit them.

2. **When saving the game using (S), there was a FileNotFound Exception.** This was due to the filepath storing in 
   the file path "D:/..." but not all devices own a D drive.

   **How the problem was solved:**
     - In the main() function in the class Main, right before the start() function is called, I call a function named 
     setSavePaths(). It checks if the device has a D drive and alters the variables savePath and savePathDir accordingly.
     If the device does not have a D drive, it changes the filepath to the game file's relative directory.

3. **Load button was present in the game code but did not appear in the game screen.**

   **How the problem was solved:**
     - load button added to root for the first scene added to primaryStage. The load button functioned as expected.

4. **If there was no saved game progress and "load game" button is pressed, the paddle moves to the top left of the screen
   and ball moves abnormally.**

   **How the problem was solved:**
     - There is game progress saved only if the save.mdd file exists. Therefore, I altered the setOnAction function for the 
     load button to check if the save.mdd file exists. If it does, the file is read to resume that game, else
     the label "No previous games saved :<" appears.

5. **Labels keep freezing on screen.**

   **How the problem was solved:**
     - Thread.sleep(15) for animations is not the most efficient way to achieve animations on JavaFX, therefore I used 
     a built-in animation framework for smoother animations. Instead of manually updating the scale/position of the 
     label with a for loop to make animations with threading, I utilized TranslateTransition for movement and 
     FadeTransition for fading.

6. **Exception in thread "JavaFX Application Thread" java.util.ConcurrentModificationException -** occurs when iterating
   over the collections blocks and bonusArray and making changes to the collections during iteration.

   **How the problem was solved:**
     - The ArrayLists blocks and bonusArray are declared as normal collections, which are not thread-safe and can
     cause problems if they are modified while they are being iterated over. Therefore, I declared both arrays as
     concurrent collections of type CopyOnWriteArrayList. This means that it is safe to modify the collections
     while you are iterating over it, so you will not get a ConcurrentModificationException.

7. **when the player loses all their lives, when Game Over is displayed, the hearts label is not updated to 0, remains at
   old value.**

   **How problem was solved:**
     - In function handleBallYBoundaries(), in the if statement that confirms that the game is over, the program was
       altered to call the function onUpdate() for the last time to update the game screen before the game engine
       halts.

8. **When playing a loaded game, after all bricks are broken, sometimes the game continues without moving to the next 
   level.** 

   **How the problem was solved:**
      - A logical error was present. This is because, in the read file we read the destroyedBlockCount value and the 
      blocks array stores only the blocks which have not been destroyed. If for example the player starts with 12 bricks, 
      and he breaks 7 bricks and saves the game, destroyedBlockCount stores 7, but when he reloads the game, the blocks 
      array size is now 5, as destroyed bricks are not loaded. We compare destroyedBlockCount and blocks array size (if they are equal) 
      to call nextLevel, but these values will never be the same.
      - in the function loadGameStats(), I overwrite destroyedBlockCount to 0. If destroyed blocks are not loaded into
      the game, there is no need to store the player's previous destroyedBlockCount.

9. **After incorporating the short paddle bonus, if there were many short paddle powers being caught by the paddle,
   the paddle glitches.**

   **How the problem was solved:**
     - The code responsible for changing the paddle width was updated to use `Platform.runLater()` each time the width was
     modified. This ensured that the UI updates occurred in a thread-safe manner. After applying the solution, the game 
     was tested, and the glitch associated with frequent paddle resizing was no longer observed. The movement of the 
     paddle remained smooth and glitch-free.

10. **If the game was saved while the ball was in gold form, the loaded game does not show the ball with gold ball image.**

    **How the problem was solved:**
      - The function which frequently updated the ball's features was the moveBall() function. Therefore, I modified
      this function to check the isGoldStats flag and change the image of the ball appropriately.
