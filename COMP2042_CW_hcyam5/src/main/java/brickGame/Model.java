package brickGame;

import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.CopyOnWriteArrayList;

public class Model {
    private static Model instance;
    private Model() {
        // private constructor to prevent instantiation
    }
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

    private int destroyedBlockCount = 0;
    private boolean isPaused=false;
    private boolean loadFromSave = false;
    private GameEngine engine = GameEngine.getInstance();
    private Rectangle paddle = new Rectangle();

    private Rectangle meter = new Rectangle();
    private int gunMeter = 3;

    public int getGunMeter() {
        return gunMeter;
    }
    public void decGunMeter() {
        this.gunMeter--;
    }

    public void setGunMeter(int gm) {
        this.gunMeter = gm;
    }


    public final CopyOnWriteArrayList<Block> blocks = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<Power> powerArray = new CopyOnWriteArrayList<Power>(); // stores bonuses for all blocks

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

    public double getvX() {
        return vX;
    }

    public void setvX(double v) {
        vX = v;
    }

    public static double getvY() {
        return vY;
    }
    public boolean isGoDownBall() {
        return goDownBall;
    }

    public void setGoDownBall(boolean gdb) {
        goDownBall = gdb;
    }

    public boolean isGoRightBall() {
        return goRightBall;
    }

    public void setGoRightBall(boolean grb) {
        goRightBall = grb;
    }

    public boolean isCollideToPaddle() {
        return collideToPaddle;
    }

    public void setCollideToPaddle(boolean ctp) {
        collideToPaddle = ctp;
    }

    public boolean isCollideToPaddleAndMoveToRight() {
        return collideToPaddleAndMoveToRight;
    }

    public void setCollideToPaddleAndMoveToRight(boolean x) {
        collideToPaddleAndMoveToRight = x;
    }

    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    public void setCollideToRightWall(boolean j) {
        collideToRightWall = j;
    }

    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    public void setCollideToLeftWall(boolean f) {
        collideToLeftWall = f;
    }

    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    public void setCollideToRightBlock(boolean c) {
        collideToRightBlock = c;
    }

    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    public void setCollideToBottomBlock(boolean c) {
        collideToBottomBlock = c;
    }

    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    public void setCollideToLeftBlock(boolean v) {
        collideToLeftBlock = v;
    }

    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    public void setCollideToTopBlock(boolean p) {
        collideToTopBlock = p;
    }

    public boolean isIsExistHeartBlock() {
        return isExistHeartBlock;
    }

    public void setIsExistHeartBlock(boolean iehb) {
        isExistHeartBlock = iehb;
    }

    private boolean isExistHeartBlock = false;

    public long getHitTime() {
        return hitTime;
    }

    public void setHitTime(long ht) {
        hitTime = ht;
    }

    public long getGoldTime() {
        return goldTime;
    }

    public void setGoldTime(long gt) {
        goldTime = gt;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long t) {
        time = t;
    }

    public Rectangle getPaddle(){
        return paddle;
    }
    public Circle getBall(){
        return ball;
    }
    public Rectangle getMeter() {
        return meter;
    }
    public int getHalfPaddleWidth() {
        return halfPaddleWidth;
    }
    public double getPaddleWidth(){
        return paddleWidth;
    }
    public void setPaddleWidth(int pw){
        paddleWidth=pw;
    }

    public double getCenterPaddleX(){
        return centerPaddleX;
    }
    public void setCenterPaddleX(double cpx){
        centerPaddleX=cpx;
    }
    public double getXPaddle(){
        return xPaddle;
    }
    public void setXPaddle(double xp){
        xPaddle=xp;
    }
    public double getYPaddle(){
        return yPaddle;
    }
    public void setYPaddle(double yp){
        yPaddle=yp;
    }
    public int getDestroyedBlockCount(){return destroyedBlockCount;}
    public void setDestroyedBlockCount(int dbc){
        destroyedBlockCount=dbc;
    }
    public boolean getLoadFromSave(){return loadFromSave;}
    public void setLoadFromSave(boolean lfs){
        loadFromSave=lfs;
    }
    public boolean getIsPaused(){return isPaused;}
    public void setIsPaused(boolean p){
        isPaused=p;
    }
    public double getXBall(){
        return xBall;
    }
    public double getYBall(){
        return yBall;
    }
    public void setXBall(double xb){
        xBall=xb;
    }
    public void setYBall(double yb){
        yBall=yb;
    }
    public GameEngine getGameEngine(){
        return engine;
    }

    public void setIsGoldStats(boolean b){
        isGoldStats=b;
    }
    public boolean getIsGoldStats(){
        return isGoldStats;
    }

    public int getLevel(){
        return level;
    }
    public void incLevel(){
        level++;
    }
    public  void setLevel(int lv){
        level = lv;
    }
    public int getHeart(){
        return heart;
    }
    public void setHeart(int h){
        heart = h;
    }
    public void decHeart(){
        heart--;
    }
    public void incHeart(){
        heart++;
    }
    public int getScore(){
        return score;
    }
    public void incScore(){
        score++;
    }
    public void setScore(int sc){
        score=sc;
    }
    public boolean isInvert() {
        return invert;
    }
    public void setInvert(boolean in) {
        invert = in;
    }
    public boolean isShortPaddle() {
        return shortPaddle;
    }
    public void setShortPaddle(boolean sp) {
        shortPaddle = sp;
    }
    public void updatePaddleWidth(boolean shortPaddle) {
        if (shortPaddle) { // changes the variable's value
            paddleWidth=SHORT_PADDLE_WIDTH;
            halfPaddleWidth=SHORT_PADDLE_WIDTH/2;
        } else {
            paddleWidth=NORMAL_PADDLE_WIDTH; // Use the initial paddle width when not short
            halfPaddleWidth=SHORT_PADDLE_WIDTH/2;
        }

    }
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
    protected void setHitFlags(int hitCode, Block block){
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
    protected void setPhysicsToBall(Main mainInstance) {
            moveBall();
            handleBallYBoundaries(mainInstance);
            handleBallPaddleCollision();
            handleBallXBoundaries();
            handleBallWallCollisions();
            handleBallBlockCollision();
    }
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

    protected void handleBallPaddleCollision(){
        if (yBall >= yPaddle - ballRadius) {
            if (xBall >= xPaddle && xBall <= xPaddle + paddleWidth) {
                //hitTime = time;
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
    protected void handleBallYBoundaries(Main mainInstance){
        if (yBall <= 0) {
            resetCollideFlags();
            goDownBall=true;
            return;
        }

        if (yBall + (2*ballRadius) >= Model.sceneHeight) {
            goDownBall=false;
            resetCollideFlags();
            System.out.printf("\nIS OUT OF BOUNDARY" +goDownBall+ yBall);
            if (!isGoldStats) {
                heart--;
                System.out.println("\nHEART DEDUCT AND NOT GOLD");
                new Score().show(Model.sceneWidth / 2,Model.sceneHeight / 2, -1, mainInstance);
                if (heart == 0) {
                    mainInstance.onUpdate();
                    new Score().showGameOver(mainInstance);
                    engine.stop();
                }
            }
        }
    }
    protected void handleBallWallCollisions(){
        if (collideToRightWall) {
            goRightBall=false;
        }
        if (collideToLeftWall) {
            goRightBall=true;
        }
    }
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
    protected void movePaddle(final int direction){
        if (xPaddle == (sceneWidth - paddleWidth) && direction == Main.RIGHT) {
            return;
        }
        if (xPaddle == 0 && direction == Main.LEFT) {
            return;
        }
        if (direction == Main.RIGHT) {
            xPaddle++;
        } else {
            xPaddle--;
        }
        centerPaddleX=xPaddle + halfPaddleWidth;
    }
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
