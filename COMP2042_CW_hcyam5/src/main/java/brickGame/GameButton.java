package brickGame;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameButton extends Button {

    public GameButton(String buttonText, String imagePath, double x, double y) {
        super(buttonText);

        // Load the button's image
        Image buttonImage = new Image(imagePath);
        ImageView imageView = new ImageView(buttonImage);

        // Set properties for the image
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        // Set the graphic (image) for the button
        this.setGraphic(imageView);

        // Set additional styles or properties if needed
        this.setPrefSize(30, 10);
        this.setStyle("-fx-background-color: #291741;");

        // Set the translation (X and Y coordinates) for the button
        this.setTranslateX(x);
        this.setTranslateY(y);
    }
}

