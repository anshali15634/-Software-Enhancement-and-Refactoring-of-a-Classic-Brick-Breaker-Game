package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

public class Bonus implements Serializable {
    public Rectangle choco;
    public int bonusType; // 1 if it is the +3 bonus, 2 if it is the invert bonus

    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false; // paddle hits the bonus

    public Bonus(int row, int column, int bonType) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;
        bonusType = bonType;

        draw();
    }

    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        String url;
        if (bonusType == 1) {
            if (new Random().nextInt(20) % 2 == 0) {
                url = "bonus1.png";
            } else {
                url = "bonus2.png";
            }
        }else{
            url = "invertbonus.png";
        }

        choco.setFill(new ImagePattern(new Image(url)));
    }



}
