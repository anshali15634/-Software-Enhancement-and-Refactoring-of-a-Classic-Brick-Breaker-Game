package brickGame;

import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represents the model of the game.
 * It manages all the game data, including:
 * - Game objects (ball, paddle, blocks, etc.)
 * - Game state (level, score, heart, time, etc.)
 * - Game flags (collision detection, bonuses, etc.)
 * - And provides methods to update the game state.
 */
public class Model {
    private static Model instance;
    /**
     * Private constructor to prevent instantiation
     */
    private Model() {
        // private constructor to prevent instantiation
    }
    /**
     * Gets the singleton instance of the Model class.
     *
     * @return The Model instance.
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    public static final int sceneWidth = 500; // static
    public static final int sceneHeight = 700;// static
    public static final int SHORT_PADDLE_WIDTH = 90;// static
    public static final int NORMAL_PADDLE_WIDTH = 130;// static
    public static final int LAST_BLOCK_ROW=2; // decides how many rows of blocks per level // static
    public static final int ballRadius = 10;// static
    private int level=0;// static
    public final static int final_level = 6; // game finishes at level 5// static
    private static int paddleWidth = NORMAL_PADDLE_WIDTH;
    private static int halfPaddleWidth = paddleWidth / 2;
    public static final int paddleHeight = 30;


    private Circle ball = new Circle();
    private int  heart    = 3;
    private int  score    = 0;
    private boolean invert = false; // to check if invert bonus is used
    private boolean shortPaddle = false; // to check if short paddle bonus is used
    private boolean isGoldStats=false;
    private double xBall;
    private double yBall;
    private double xPaddle = 0.0f;
    private double yPaddle = 640.0f;

    private double vX = 2.000;
    private static final double vY = 5.000;
    private double centerPaddleX;
    private boolean isExistHeartBlock = false;
    private int destroyedBlockCount = 0;
    private boolean isPaused=false;
    private boolean loadFromSave = false;
    private GameEngine engine = GameEngine.getInstance();
    private Rectangle paddle = new Rectangle();

    private Rectangle meter = new Rectangle();
    private int gunMeter = 3;
    /**
     * Returns the value of gunMeter.
     * @return An integer from 0-3.
     */
    public int getGunMeter() {
        return gunMeter;
    }
    /**
     * Decrements the value of gunMeter.
     */
    public void decGunMeter() {
        this.gunMeter--;
    }

    /**
     * Sets the value of gunMeter.
     * @param gm An integer from 0-3.
     */
    public void setGunMeter(int gm) {
        this.gunMeter = gm;
    }


    public final CopyOnWriteArrayList<Block> blocks = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<Power> powerArray = new CopyOnWriteArrayList<>(); // stores bonuses for all blocks

    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean collideToPaddle = false;
    private boolean collideToPaddleAndMoveToRight = true;
    private boolean collideToRightWall = false;
    private boolean collideToLeftWall = false;
    private boolean collideToRightBlock = false;
    private boolean collideToBottomBlock = false;
    private boolean collideToLeftBlock = false;
    private boolean collideToTopBlock = false;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;

    /**
     * Gets the current horizontal velocity of the ball.
     * @return The horizontal velocity of the ball.
     */
    public double getvX() {
        return vX;
    }

    /**
     * Sets the horizontal velocity of the ball.
     * @param v The new horizontal velocity of the ball.
     */
    public void setvX(double v) {
        vX = v;
    }

    /**
     * Checks if the ball is moving downward.
     * @return True if the ball is moving downward, false otherwise.
     */
    public boolean isGoDownBall() {
        return goDownBall;
    }

    /**
     * Sets the direction of the ball to move downward or upward.
     * @param gdb True to move the ball downward, false to move it upward.
     */
    public void setGoDownBall(boolean gdb) {
        goDownBall = gdb;
    }

    /**
     * Checks if the ball is moving to the right.
     * @return True if the ball is moving to the right, false otherwise.
     */
    public boolean isGoRightBall() {
        return goRightBall;
    }

    /**
     * Sets the direction of the ball to move to the right or left.
     * @param grb True to move the ball to the right, false to move it to the left.
     */
    public void setGoRightBall(boolean grb) {
        goRightBall = grb;
    }

    /**
     * Checks if the ball is colliding with the paddle.
     * @return True if the ball is colliding with the paddle, false otherwise.
     */
    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    /**
     * Sets the collision status of the ball with the paddle.
     * @param ctp True if the ball is colliding with the paddle, false otherwise.
     */
    public void setCollideToPaddle(boolean ctp) {
        collideToPaddle = ctp;
    }

    /**
     * Checks if the ball is colliding with the paddle and moving to the right.
     * @return True if the ball is colliding with the paddle and moving to the right, false otherwise.
     */
    public boolean isCollideToPaddleAndMoveToRight() {
        return collideToPaddleAndMoveToRight;
    }

    /**
     * Sets the collision status of the ball with the paddle and its movement direction.
     * @param x True if the ball is colliding with the paddle and moving to the right, false otherwise.
     */
    public void setCollideToPaddleAndMoveToRight(boolean x) {
        collideToPaddleAndMoveToRight = x;
    }

    /**
     * Checks if the ball is colliding with the right wall.
     * @return True if the ball is colliding with the right wall, false otherwise.
     */
    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    /**
     * Sets the collision status of the ball with the right wall.
     * @param j True if the ball is colliding with the right wall, false otherwise.
     */
    public void setCollideToRightWall(boolean j) {
        collideToRightWall = j;
    }

    /**
     * Checks if the ball is colliding with the left wall.
     * @return True if the ball is colliding with the left wall, false otherwise.
     */
    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    /**
     * Sets the collision status of the ball with the left wall.
     * @param f True if the ball is colliding with the left wall, false otherwise.
     */
    public void setCollideToLeftWall(boolean f) {
        collideToLeftWall = f;
    }

    /**
     * Checks if the ball is colliding with a block on the right.
     * @return True if the ball is colliding with a block on the right, false otherwise.
     */
    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    /**
     * Sets the collision status of the ball with a block on the right.
     * @param c True if the ball is colliding with a block on the right, false otherwise.
     */
    public void setCollideToRightBlock(boolean c) {
        collideToRightBlock = c;
    }

    /**
     * Checks if the ball is colliding with the bottom of a block.
     * @return True if the ball is colliding with the bottom of a block, false otherwise.
     */
    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    /**
     * Sets the collision status of the ball with the bottom of a block.
     * @param c True if the ball is colliding with the bottom of a block, false otherwise.
     */
    public void setCollideToBottomBlock(boolean c) {
        collideToBottomBlock = c;
    }

    /**
     * Checks if the ball is colliding with a block on the left.
     * @return True if the ball is colliding with a block on the left, false otherwise.
     */
    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    /**
     * Sets the collision status of the ball with a block on the left.
     * @param v True if the ball is colliding with a block on the left, false otherwise.
     */
    public void setCollideToLeftBlock(boolean v) {
        collideToLeftBlock = v;
    }

    /**
     * Checks if the ball is colliding with the top of a block.
     * @return True if the ball is colliding with the top of a block, false otherwise.
     */
    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    /**
     * Sets the collision status of the ball with the top of a block.
     * @param p True if the ball is colliding with the top of a block, false otherwise.
     */
    public void setCollideToTopBlock(boolean p) {
        collideToTopBlock = p;
    }

    /**
     * Checks if an extra heart block exists.
     * @return True if an extra heart block exists, false otherwise.
     */
    public boolean isIsExistHeartBlock() {
        return isExistHeartBlock;
    }

    /**
     * Sets the existence status of an extra heart block.
     * @param iehb True if an extra heart block exists, false otherwise.
     */
    public void setIsExistHeartBlock(boolean iehb) {
        isExistHeartBlock = iehb;
    }

    /**
     * Gets the time when the gold status was activated.
     * @return The time when the gold status was activated.
     */
    public long getGoldTime() {
        return goldTime;
    }

    /**
     * Sets the time when the gold status was activated.
     * @param gt The time when the gold status was activated.
     */
    public void setGoldTime(long gt) {
        goldTime = gt;
    }

    /**
     * Gets the current time in the game.
     * @return The current time in the game.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the current time in the game.
     * @param t The new time value to set.
     */
    public void setTime(long t) {
        time = t;
    }

    /**
     * Gets the paddle rectangle object.
     * @return The paddle rectangle object.
     */
    public Rectangle getPaddle(){
        return paddle;
    }

    /**
     * Gets the ball circle object.
     * @return The ball circle object.
     */
    public Circle getBall(){
        return ball;
    }

    /**
     * Gets the meter rectangle object.
     * @return The meter rectangle object.
     */
    public Rectangle getMeter() {
        return meter;
    }

    /**
     * Gets half the width of the paddle.
     * @return Half the width of the paddle.
     */
    public int getHalfPaddleWidth() {
        return halfPaddleWidth;
    }

    /**
     * Gets the width of the paddle.
     * @return The width of the paddle.
     */
    public double getPaddleWidth(){
        return paddleWidth;
    }

    /**
     * Gets the center X-coordinate of the paddle.
     * @return The center X-coordinate of the paddle.
     */
    public double getCenterPaddleX(){
        return centerPaddleX;
    }

    /**
     * Sets the center X-coordinate of the paddle.
     * @param cpx The new center X-coordinate of the paddle.
     */
    public void setCenterPaddleX(double cpx){
        centerPaddleX=cpx;
    }

    /**
     * Gets the X-coordinate of the paddle.
     * @return The X-coordinate of the paddle.
     */
    public double getXPaddle(){
        return xPaddle;
    }

    /**
     * Sets the X-coordinate of the paddle.
     * @param xp The new X-coordinate of the paddle.
     */
    public void setXPaddle(double xp){
        xPaddle=xp;
    }

    /**
     * Gets the Y-coordinate of the paddle.
     * @return The Y-coordinate of the paddle.
     */
    public double getYPaddle(){
        return yPaddle;
    }

    /**
     * Sets the Y-coordinate of the paddle.
     * @param yp The new Y-coordinate of the paddle.
     */
    public void setYPaddle(double yp){
        yPaddle=yp;
    }

    /**
     * Gets the count of destroyed blocks.
     * @return The count of destroyed blocks.
     */
    public int getDestroyedBlockCount(){
        return destroyedBlockCount;
    }

    /**
     * Sets the count of destroyed blocks.
     * @param dbc The new count of destroyed blocks.
     */
    public void setDestroyedBlockCount(int dbc){
        destroyedBlockCount=dbc;
    }

    /**
     * Gets the load from save status.
     * @return True if loading from save, false otherwise.
     */
    public boolean getLoadFromSave(){
        return loadFromSave;
    }

    /**
     * Sets the load from save status.
     * @param lfs True if loading from save, false otherwise.
     */
    public void setLoadFromSave(boolean lfs){
        loadFromSave=lfs;
    }

    /**
     * Gets the pause status of the game.
     * @return True if the game is paused, false otherwise.
     */
    public boolean getIsPaused(){
        return isPaused;
    }

    /**
     * Sets the pause status of the game.
     * @param p True if the game is paused, false otherwise.
     */
    public void setIsPaused(boolean p){
        isPaused=p;
    }

    /**
     * Gets the X-coordinate of the ball.
     * @return The X-coordinate of the ball.
     */
    public double getXBall(){
        return xBall;
    }

    /**
     * Gets the Y-coordinate of the ball.
     * @return The Y-coordinate of the ball.
     */
    public double getYBall(){
        return yBall;
    }

    /**
     * Sets the X-coordinate of the ball.
     * @param xb The new X-coordinate of the ball.
     */
    public void setXBall(double xb){
        xBall=xb;
    }

    /**
     * Sets the Y-coordinate of the ball.
     * @param yb The new Y-coordinate of the ball.
     */
    public void setYBall(double yb){
        yBall=yb;
    }

    /**
     * Gets the game engine object.
     * @return The game engine object.
     */
    public GameEngine getGameEngine(){
        return engine;
    }

    /**
     * Sets the gold status of the game.
     * @param b True if the gold status is active, false otherwise.
     */
    public void setIsGoldStats(boolean b){
        isGoldStats=b;
    }

    /**
     * Gets the gold status of the game.
     * @return True if the gold status is active, false otherwise.
     */
    public boolean getIsGoldStats(){
        return isGoldStats;
    }

    /**
     * Gets the current level in the game.
     * @return The current level.
     */
    public int getLevel(){
        return level;
    }

    /**
     * Increases the current level by one.
     */
    public void incLevel(){
        level++;
    }

    /**
     * Sets the current level to the specified value.
     * @param lv The new level value to set.
     */
    public  void setLevel(int lv){
        level = lv;
    }

    /**
     * Gets the current number of hearts in the game.
     * @return The current number of hearts.
     */
    public int getHeart(){
        return heart;
    }

    /**
     * Sets the number of hearts in the game.
     * @param h The new number of hearts.
     */
    public void setHeart(int h){
        heart = h;
    }

    /**
     * Increases the number of hearts by one.
     */
    public void incHeart(){
        heart++;
    }

    /**
     * Gets the current score in the game.
     * @return The current score.
     */
    public int getScore(){
        return score;
    }

    /**
     * Increases the current score by one.
     */
    public void incScore(){
        score++;
    }
    /**
     * Sets the score for the player.
     * @param sc The new score to be set.
     */
    public void setScore(int sc) {
        score = sc;
    }

    /**
     * Checks if the paddle controls are inverted.
     * @return true if the controls are inverted, false otherwise.
     */
    public boolean isInvert() {
        return invert;
    }

    /**
     * Sets the state of paddle control inversion.
     * @param in true to invert controls, false to keep them normal.
     */
    public void setInvert(boolean in) {
        invert = in;
    }

    /**
     * Checks if the paddle is in a short state.
     * @return true if the paddle is short, false otherwise.
     */
    public boolean isShortPaddle() {
        return shortPaddle;
    }

    /**
     * Sets the state of the paddle length.
     * @param sp true to set the paddle to a short length, false for a normal length.
     */
    public void setShortPaddle(boolean sp) {
        shortPaddle = sp;
    }

    /**
     * Updates the width of the paddle based on the specified boolean value.
     * Used mostly due to changing paddle width due to shortPaddlePower.
     * @param shortPaddle If true, the paddle width is set to SHORT_PADDLE_WIDTH. Otherwise, it is set to NORMAL_PADDLE_WIDTH.
     */
    public void updatePaddleWidth(boolean shortPaddle) {
        if (shortPaddle) {
            paddleWidth=SHORT_PADDLE_WIDTH;
        } else {
            paddleWidth=NORMAL_PADDLE_WIDTH;
        }
        halfPaddleWidth=SHORT_PADDLE_WIDTH/2;

    }
    /**
     * Resets all collision flags to false.
     */
    protected void resetCollideFlags() {
        collideToPaddle=false;
        collideToPaddleAndMoveToRight=false;
        collideToRightWall=false;
        collideToLeftWall=false;
        collideToRightBlock=false;
        collideToBottomBlock=false;
        collideToLeftBlock=false;
        collideToTopBlock=false;
    }

    /**
     * Sets the collision flags based on the specified hit code.
     * @param hitCode The code indicating which side of the block was hit.
     *                 - Block.HIT_RIGHT: right side of the block
     *                 - Block.HIT_BOTTOM: bottom side of the block
     *                 - Block.HIT_LEFT: left side of the block
     *                 - Block.HIT_TOP: top side of the block
     */
    protected void setHitFlags(int hitCode){
        if (hitCode == Block.HIT_RIGHT) {
            collideToRightBlock =true;
        } else if (hitCode == Block.HIT_BOTTOM) {
            collideToBottomBlock=true;
        } else if (hitCode == Block.HIT_LEFT) {
            collideToLeftBlock=true;
        } else if (hitCode == Block.HIT_TOP) {
            collideToTopBlock=true;
        }
    }

    /**
     * Updates the physics of the ball, including its movement, collision detection, and boundaries.
     *
     * @param controllerInstance The Controller instance for handling user input and game events. Used to directly updating the heart, score when ball exceeds y boundaries.
     *
     */
    protected void setPhysicsToBall(Controller controllerInstance) {
            moveBall();
            handleBallYBoundaries(controllerInstance);
            handleBallPaddleCollision();
            handleBallXBoundaries();
            handleBallWallCollisions();
            handleBallBlockCollision();
    }
    /**
     * Moves the ball based on its current velocity and direction.
     * Also updates the ball's image based on the player's gold power status.
     */
    protected void moveBall(){
        Platform.runLater(() -> {
            if (!isGoldStats) {
                //model.getBall().setFill(new ImagePattern(new Image("ball.png")));
                View.gameObjectImageFill(ball,"ball.png");
            } else {
                View.gameObjectImageFill(ball,"goldball.png");
            }
        });

        if (goDownBall) {
            yBall+=vY;
        } else {
            yBall-=vY;
        }
        if (goRightBall) {
            xBall+=vX;
        } else {
            xBall-=vX;
        }
    }

    /**
     * Handles the collision between the ball and the paddle.
     * Updates the ball's velocity and direction based on the collision point.
     */
    protected void handleBallPaddleCollision(){
        if (yBall >= yPaddle - ballRadius) {
            if (xBall >= xPaddle && xBall <= xPaddle + paddleWidth) {
                hitTime=time;
                resetCollideFlags();
                collideToPaddle=true;
                goDownBall=false;
                double relation = (xBall - centerPaddleX) / (int)(paddleWidth / 2);
                if (Math.abs(relation) <= 0.3) {
                    vX=Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX=((Math.abs(relation) * 1.5) + (level / 3.500));
                } else {
                    vX=((Math.abs(relation) * 2) + (level / 3.500));
                }
                collideToPaddleAndMoveToRight=((xBall - centerPaddleX) > 0);
            }
        }
        if (collideToPaddle) {
            goRightBall=collideToPaddleAndMoveToRight;
        }
    }
    /**
     * Handles collisions between the ball and the left and right walls of the game window.
     * Updates the ball's direction and collision flags accordingly.
     */
    protected void handleBallXBoundaries(){
        if (xBall >= sceneWidth) {
            resetCollideFlags();
            collideToRightWall=true;
        }

        if (xBall <= 0) {
            resetCollideFlags();
            collideToLeftWall=true;
        }
    }
    /**
     * Handles collisions between the ball and the top and bottom boundaries of the game window.
     * Updates the ball's direction and collision flags accordingly. Also checks for ball falling out of bounds and handles corresponding logic.
     * @param controllerInstance The Controller instance for updating score.
     */
    protected void handleBallYBoundaries(Controller controllerInstance){
        if (yBall <= 0) {
            resetCollideFlags();
            goDownBall=true;
            return;
        }

        if (yBall + (2*ballRadius) >= Model.sceneHeight) {
            goDownBall=false;
            resetCollideFlags();
            if (!isGoldStats) {
                heart--;
                new Score().show(Model.sceneWidth / 2,Model.sceneHeight / 2, -1, controllerInstance);
                if (heart == 0) {
                    controllerInstance.onUpdate();
                    new Score().showGameOver(controllerInstance);
                    engine.stop();
                }
            }
        }
    }
    /**
     * Handles the aftermath of ball collisions with left and right walls.
     * Specifically, adjusts the ball's direction based on the collision flags.
     */
    protected void handleBallWallCollisions(){
        if (collideToRightWall) {
            goRightBall=false;
        }
        if (collideToLeftWall) {
            goRightBall=true;
        }
    }
    /**
     * Handles the aftermath of ball collisions with the top, bottom, left, and right sides of blocks.
     * Adjusts the ball's direction based on the collision flags.
     */
    protected void handleBallBlockCollision(){
        if (collideToRightBlock) {
            goRightBall=true;
        }

        if (collideToLeftBlock) {
            goRightBall=true;
        }

        if (collideToTopBlock) {
            goDownBall=false;
        }

        if (collideToBottomBlock) {
            goDownBall=true;
        }
    }
    /**
     * Resets all game flags and variables to their initial values, effectively restarting the game.
     */
    protected void resetFlags(){
        vX=2.000;
        invert=false;
        shortPaddle=false;
        updatePaddleWidth(shortPaddle);
        destroyedBlockCount=0;
        resetCollideFlags();
        goDownBall=true;
        isGoldStats=false;
        isExistHeartBlock=false;
        hitTime=0;
        time=0;
        goldTime=0;
        blocks.clear();
        bullets.clear();
        powerArray.clear();

    }
    /**
     * Moves the paddle based on the specified direction, within the game window boundaries.
     * @param direction The direction to move the paddle (Controller.LEFT or Controller.RIGHT)
     */
    protected void movePaddle(final int direction){
        if (xPaddle == (sceneWidth - paddleWidth) && direction == Controller.RIGHT) {
            return;
        }
        if (xPaddle == 0 && direction == Controller.LEFT) {
            return;
        }
        if (direction == Controller.RIGHT) {
            xPaddle++;
        } else {
            xPaddle--;
        }
        centerPaddleX=xPaddle + halfPaddleWidth;
    }
    /**
     * Checks whether the ball has collided with a specific block within a certain boundary.
     * If there is a collision, it also determines which side of the block was hit.
     * @param xBall The x-coordinate of the ball.
     * @param yBall The y-coordinate of the ball.
     * @param block The block to check for collision with.
     * @return An integer indicating the side of the block hit (Block.NO_HIT, Block.HIT_TOP, Block.HIT_BOTTOM, Block.HIT_LEFT, or Block.HIT_RIGHT).
     */
    public int checkHitToBlock(double xBall, double yBall, Block block) {
        if (block.isDestroyed) {
            return Block.NO_HIT;
        }
        double boundary = 5.0; // marks boundary for ball-block collision
        if (xBall + ballRadius >= block.x - boundary && xBall - ballRadius <= block.x + Block.getWidth() + boundary &&
                yBall + ballRadius >= block.y - boundary && yBall - ballRadius <= block.y + Block.getHeight() + boundary) {
            // now just decide which block side was touched by ball
            if (yBall >= block.y && yBall <= block.y + Block.getHeight()) {
                if (xBall >= block.x && xBall <= block.x + Block.getWidth()) {
                    if (yBall <= block.y + boundary) {
                        return Block.HIT_TOP;
                    } else if (yBall >= block.y + Block.getHeight() - boundary) {
                        return Block.HIT_BOTTOM;
                    }
                }
            }
            if (xBall >= block.x && xBall <= block.x + Block.getWidth()) {
                if (xBall <= block.x + boundary) {
                    return Block.HIT_LEFT;
                } else if (xBall >= block.x + Block.getWidth() - boundary) {
                    return Block.HIT_RIGHT;
                }
            }
        }
        return Block.NO_HIT;
    }
    /**
     * Checks whether a bullet has collided with a specific block within a certain boundary.
     * If there is a collision, it also determines which side of the block was hit.
     *
     * @param xBullet The x-coordinate of the bullet.
     * @param yBullet The y-coordinate of the bullet.
     * @param block The block to check for collision with.
     * @return An integer indicating the side of the block hit (Block.NO_HIT, Block.HIT_TOP, Block.HIT_BOTTOM, Block.HIT_LEFT, or Block.HIT_RIGHT).
     */
    public int checkBulletHitToBlock(double xBullet, double yBullet, Block block){
        if (block.isDestroyed) {
            return Block.NO_HIT;
        }
        double boundary = 5.0;
        if (xBullet >= block.x - boundary && xBullet <= block.x + Block.getWidth() + boundary &&
                yBullet>= block.y - boundary && yBullet<= block.y + Block.getHeight() + boundary) {
            if (yBullet >= block.y && yBullet <= block.y + Block.getHeight()) {
                if (xBullet >= block.x && xBullet <= block.x + Block.getWidth()) {
                    if (yBullet <= block.y + boundary) {
                        return Block.HIT_TOP;
                    } else if (yBullet >= block.y + Block.getHeight() - boundary) {
                        return Block.HIT_BOTTOM;
                    }
                }
            }
            if (xBullet >= block.x && xBullet <= block.x + Block.getWidth()) {
                if (xBullet <= block.x + boundary) {
                    return Block.HIT_LEFT;
                } else if (xBullet >= block.x + Block.getWidth() - boundary) {
                    return Block.HIT_RIGHT;
                }
            }
        }
        return Block.NO_HIT;
    }
}
