package brickGame;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;
import java.util.Random;

/**
 * Abstract class for representing power-ups in the Brick Breaker game.
 * Subclasses must implement the {@code chooseImage()} and {@code powerMessage()} methods.
 */
public abstract class Power implements Serializable {
    protected Rectangle newPowerBlock;
    protected double x;
    protected double y;
    protected long timeCreated;
    protected boolean taken = false;

    /**
     * Constructor for a Power object.
     * @param row    The row position of the power-up. (Depends on the its block's location)
     * @param column The column position of the power-up.(Depends on the its block's location)
     */
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

        String url = chooseImage();
        View.gameObjectImageFill(newPowerBlock, url);
    }

    /**
     * Abstract method to be implemented by concrete subclasses.
     * Chooses the image URL for the power-up.
     * @return The URL of the image representing the power-up.
     */
    protected abstract String chooseImage();

    /**
     * Abstract method to be implemented by concrete subclasses.
     * Displays a power-up message using the controller to display the message.
     * @param controllerInstance The instance of the game controller.
     */
    protected abstract void powerMessage(Controller controllerInstance);
}

/**
 * Represents a power-up that shortens the paddle in the Brick Breaker game.
 */
class shortPaddlePower extends Power {
    /**
     * Constructor for a shortPaddlePower object.
     * @param row    The row position of the power-up.
     * @param column The column position of the power-up.
     */
    public shortPaddlePower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url = "short_bonus.png";
        return url;
    }

    @Override
    protected void powerMessage(Controller controllerInstance) {
        new Score().showMessage("CAREFUL! PADDLE CHANGE!", controllerInstance);
    }
}

/**
 * Represents a power-up that increases the player's score in the Brick Breaker game.
 */
class scorePlusPower extends Power {
    /**
     * Constructor for a scorePlusPower object.
     * @param row    The row position of the power-up.
     * @param column The column position of the power-up.
     */
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
    protected void powerMessage(Controller controllerInstance) {
        new Score().show(x, y, 3, controllerInstance);
    }
}

/**
 * Represents a power-up that inverts the paddle controls in the Brick Breaker game.
 */
class invertPower extends Power {
    /**
     * Constructor for an invertPower object.
     * @param row    The row position of the power-up.
     * @param column The column position of the power-up.
     */
    public invertPower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url = "invertbonus.png";
        return url;
    }

    @Override
    protected void powerMessage(Controller controllerInstance) {
        new Score().showMessage("INVERTED PADDLE CONTROLS :>", controllerInstance);
    }
}

/**
 * Represents a power-up that grants the player an additional life in the Brick Breaker game.
 */
class heartPower extends Power {
    /**
     * Constructor for a heartPower object.
     * @param row    The row position of the power-up.
     * @param column The column position of the power-up.
     */
    public heartPower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url = "pink_heart.png";
        return url;
    }

    @Override
    protected void powerMessage(Controller controllerInstance) {
        new Score().showMessage("ONE MORE LIFE!", controllerInstance);
    }
}

/**
 * Represents a power-up that turns the ball golden and temporarily freezes the player's lives.
 */
class goldPower extends Power {
    /**
     * Constructor for a goldPower object.
     * @param row    The row position of the power-up.
     * @param column The column position of the power-up.
     */
    public goldPower(int row, int column) {
        super(row, column);
    }

    @Override
    protected String chooseImage() {
        String url = "gold_star.png";
        return url;
    }

    @Override
    protected void powerMessage(Controller controllerInstance) {
        new Score().showMessage("GOLD BALL - FREEZE LIVES :>", controllerInstance);
    }
}
