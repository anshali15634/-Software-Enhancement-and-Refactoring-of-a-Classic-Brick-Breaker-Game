package brickGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

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

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;
        build();
        draw();
    }

    protected void build() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;
        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }
    protected abstract void draw();
    protected abstract void initPower();
}

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
