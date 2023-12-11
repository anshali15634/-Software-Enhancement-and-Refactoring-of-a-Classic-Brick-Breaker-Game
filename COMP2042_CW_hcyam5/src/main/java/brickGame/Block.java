package brickGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
/***
 * Abstract class representing a block in a game.
 */

public abstract class Block implements Serializable {
    public static final Block block = new BlockPlain(-1,-1, Color.TRANSPARENT,99);
    Power pow = null;
    public static final int NO_HIT = -1;
    public static final int HIT_RIGHT = 0;
    public static final int HIT_BOTTOM = 1;
    public static final int HIT_LEFT = 2;
    public static final int HIT_TOP = 3;

    public static final int BLOCK_NORMAL = 99;
    public static final int BLOCK_CHOCO = 100;
    public static final int BLOCK_STAR = 101;
    public static final int BLOCK_HEART = 102;
    public static final int BLOCK_INVERT = 103;
    public static final int BLOCK_SHORT = 104;

    protected int row;
    protected int column;
    protected boolean isDestroyed;
    protected Color color;
    protected int type;
    protected int x;
    protected int y;
    protected int width = 100;
    protected int height = 30;
    protected int paddingTop = height * 2;
    protected int paddingH = 50;
    protected Rectangle rect;
/**
 * Constructs a new Block object
 * @param row The row of the block in the grid.
 * @param column The column of the block in the grid.
 * @param color The color of the block.
 * @param type The type of block (normal, choco, heart, etc)
 */
    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;
        build();
        draw();
    }

    /**
     * Calculates and sets the x and y coordinates of the Block object on the game screen.
     * Sets the width and height of the block.
     */
    protected void build() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;
        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);
    }

    /**
     * Checks if the block has been destroyed.
     * @return True if the block is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Returns the padding space above the block.
     * @return The padding space above the block.
     */
    public static int getPaddingTop() {
        return block.paddingTop;
    }

    /**
     * Returns the horizontal padding space around the block.
     * @return The horizontal padding space around the block.
     */

    public static int getPaddingH() {
        return block.paddingH;
    }

    /**
     * Returns the height of the block.
     * @return The height of the block.
     */

    public static int getHeight() {
        return block.height;
    }

    /**
     * Returns the width of the block.
     * @return The width of the block.
     */

    public static int getWidth() {
        return block.width;
    }

    /**
     * Draws the block on the screen and sets an image pattern associated to the block type.
     * This method is abstract and must be implemented by subclasses.
     */

    protected abstract void draw();

    /**
     * Initializes the Power object associated with the block type.
     * This method is abstract and must be implemented by subclasses.
     */

    protected abstract void initPower();
}

/**
 * Represents a chocolate block that releases a +3 increment to the score if the power is caught by the paddle.
 */
class BlockChoco extends Block {

    public BlockChoco(int row, int column, Color color, int type) {
        super(row, column, color , BLOCK_CHOCO);
    }

    @Override
    protected void draw() {
        View.gameObjectImageFill(rect, "choco.jpg");
    }
    @Override
    protected void initPower(){
        pow = new scorePlusPower(row, column);
    }
}

/**
 * Represents a heart block that increases lives by 1 if the heart power is caught by the paddle.
 */
class BlockHeart extends Block {

    public BlockHeart(int row, int column, Color color, int type) {
        super(row, column, color, BLOCK_HEART);
    }

    @Override
    protected void draw() {
        View.gameObjectImageFill(rect, "heart.jpg");
    }
    @Override
    protected void initPower(){
        pow = new heartPower(row, column);
    }
}

/**
 * Represents a block that releases the invert power that reverses the controls of the player's paddle
 */
class BlockInvert extends Block {

    public BlockInvert(int row, int column, Color color, int type) {
        super(row, column, color, BLOCK_INVERT);
    }

    @Override
    protected void draw() {
        View.gameObjectImageFill(rect, "darker_blue_final_brick.png");
    }
    @Override
    protected void initPower(){
        pow = new invertPower(row, column);
    }
}

/**
 * Represents a normal block that gives a +1 to the score when hit.
 */
class BlockPlain extends Block {

    public BlockPlain(int row, int column, Color color, int i) {
        super(row, column, color, BLOCK_NORMAL);
    }

    @Override
    protected void draw() {
        rect.setFill(color);
    }
    @Override
    protected void initPower(){
        pow = null;
    }
}

/**
 * Represents a block that releases a power that shortens the player's paddle.
 */
class BlockShort extends Block {

    public BlockShort(int row, int column, Color color, int type) {
        super(row, column, color, BLOCK_SHORT);
    }

    @Override
    protected void draw() {
        View.gameObjectImageFill(rect, "dark_purple_final_brick.png");
    }
    @Override
    protected void initPower(){
        pow = new shortPaddlePower(row, column);
    }
}


/**
 * Represents a star block that releases a gold power which freezes the player's lives for 5 seconds.
 */
class BlockStar extends Block {

    public BlockStar(int row, int column, Color color, int type) {
        super(row, column, color, BLOCK_STAR);
    }

    @Override
    protected void draw() {
        View.gameObjectImageFill(rect, "star.jpg");
    }
    @Override
    protected void initPower(){
        pow = new goldPower(row, column);
    }
}
