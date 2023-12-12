package brickGame;
import javafx.scene.shape.Rectangle;

/**
 * This class initialises and stores a bullet's game state details.
 * This is a new class which was not present in the original game classes.
 */
public class Bullet extends Rectangle {

    private boolean isDestroyed = false;
    public static final int width = 30;
    public static final int height = 30;
    /**
     * This method initialises the dimensions, location and image pattern of the bullet.
     * @param x Stores the x coordinate of the bullet.
     * @param y Stores the y coordinate of the bullet.
     */
    public Bullet(double x, double y){
        this.setWidth(width);
        this.setHeight(height);
        this.setX(x);
        this.setY(y);
        View.gameObjectImageFill(this, "bullet.png");
    }
    /**
     * This function informs if a bullet has been destroyed or not. This state is when a bullet goes off-screen.
     * @return True if destroyed, false if otherwise.
     */
    public boolean getIsDestroyed() {
        return isDestroyed;
    }
    /**
     * This function alters the state of the private variable isDestroyed.    */
    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

}
