package brickGame;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

// Abstract class for Power
public abstract class Power implements Serializable {
    protected Rectangle newPowerBlock;
    protected double x;
    protected double y;
    protected long timeCreated;
    protected boolean taken = false;

    // Constructor with common initialization
    public Power(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }

    // Template method
    private void draw() {
        newPowerBlock = new Rectangle();
        newPowerBlock.setWidth(30);
        newPowerBlock.setHeight(30);
        newPowerBlock.setX(x);
        newPowerBlock.setY(y);

        String url=chooseImage();
        View.gameObjectImageFill(newPowerBlock, url);
    }

    // Abstract method to be implemented by concrete subclasses
    protected abstract String chooseImage();
    protected abstract void powerMessage(Controller controllerInstance);
}
class shortPaddlePower extends Power {
    public shortPaddlePower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url = "short_bonus.png";
        return url;
    }
    @Override
    protected void powerMessage(Controller controllerInstance){
        new Score().showMessage("CAREFUL! PADDLE CHANGE!", controllerInstance);
    }
}
class scorePlusPower extends Power {
    public scorePlusPower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png";
        } else {
            url = "bonus2.png";
        }
        return url;
    }
    @Override
    protected void powerMessage(Controller controllerInstance){
        new Score().show(x, y, 3, controllerInstance);
    }
}

class invertPower extends Power {
    public invertPower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url = "invertbonus.png";
        return url;
    }
    @Override
    protected void powerMessage(Controller controllerInstance){
        new Score().showMessage("INVERTED PADDLE CONTROLS :>", controllerInstance);
    }
}
class heartPower extends Power {
    public heartPower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url="pink_heart.png";
        return url;
    }
    @Override
    protected void powerMessage(Controller controllerInstance){
        new Score().showMessage("ONE MORE LIFE!", controllerInstance);
    }
}
class goldPower extends Power {
    public goldPower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url = "gold_star.png";
        return url;
    }
    @Override
    protected void powerMessage(Controller controllerInstance){
        new Score().showMessage("GOLD BALL - FREEZE LIVES :>", controllerInstance);
    }
}

