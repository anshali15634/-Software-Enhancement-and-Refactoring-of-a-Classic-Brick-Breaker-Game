package brickGame;
import javafx.scene.shape.Rectangle;

public class Bullet extends Rectangle {

    private boolean isDestroyed = false;
    public static final int width = 30;
    public static final int height = 30;
    public Bullet(double x, double y){
        this.setWidth(width);
        this.setHeight(height);
        this.setX(x);
        this.setY(y);
        View.gameObjectImageFill(this, "bullet.png");
    }
    public boolean getIsDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }

}
