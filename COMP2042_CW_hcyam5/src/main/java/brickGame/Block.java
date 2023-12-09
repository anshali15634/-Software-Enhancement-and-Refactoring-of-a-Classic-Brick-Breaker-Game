package brickGame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);
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

    public int row;
    public int column;


    public boolean isDestroyed = false;

    private Color color;
    public int type;

    public int x;
    public int y;


    private int width = 100;
    private int height = 30;
    private int paddingTop = height * 2;
    private int paddingH = 50;
    public Rectangle rect;


    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
    public static int BLOCK_HEART = 102;
    public static int BLOCK_INVERT = 103;
    public static int BLOCK_SHORT = 104;


    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;
        build();
        draw();
    }
    private void build(){
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);
    }

    private void draw() {
        if (type == BLOCK_CHOCO) {
            View.gameObjectImageFill(rect, "choco.jpg");
        } else if (type == BLOCK_HEART) {
            View.gameObjectImageFill(rect, "heart.jpg");
        } else if (type == BLOCK_STAR) {
            View.gameObjectImageFill(rect, "star.jpg");
        }else if (type == BLOCK_INVERT) {
            View.gameObjectImageFill(rect,"darker_blue_final_brick.png");
        }else if (type == BLOCK_SHORT){
            View.gameObjectImageFill(rect, "dark_purple_final_brick.png");
        } else {
            rect.setFill(color);
        }
    }

}
