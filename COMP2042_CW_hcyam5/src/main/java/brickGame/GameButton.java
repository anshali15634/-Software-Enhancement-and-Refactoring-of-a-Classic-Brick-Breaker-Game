package brickGame;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class customizes buttons with an image and specific styling for the game menu.
 * The original game code refers to the Button class to initialize the buttons, but this version initializes all the dimensions, locations and images at once to maintain consistency throughout the user interface.
 */
public class GameButton extends Button {
    /**
     * Constructs a new GameButton object with specified text, image path, and position coordinates.
     * @param buttonText The text displayed on the button.
     * @param imagePath The path to the image displayed on the button.
     * @param x The X coordinate of the button's position.
     * @param y The Y coordinate of the button's position.
     */

    public GameButton(String buttonText, String imagePath, double x, double y) {
        super(buttonText);
        Image buttonImage = new Image(imagePath);
        ImageView imageView = new ImageView(buttonImage);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        this.setGraphic(imageView);
        this.setPrefSize(30, 10);
        this.setStyle("-fx-background-color: #291741;");
        this.setTranslateX(x);
        this.setTranslateY(y);
    }
}

