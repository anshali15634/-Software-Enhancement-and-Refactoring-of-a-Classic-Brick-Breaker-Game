package brickGame;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * The Score class manages the display of score-related information and animations.
 */
public class Score {
    private static final int ANIMATION_DURATION = 500; // Animation duration in milliseconds

    /**
     * Displays a score change animation at a specific position on the screen.
     * @param x           The x-coordinate for the score label.
     * @param y           The y-coordinate for the score label.
     * @param score       The score value to be displayed.
     * @param controller  The instance of the game controller.
     */
    public void show(final double x, final double y, int score, final Controller controller) {
        String sign = (score >= 0) ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> controller.root.getChildren().add(label));

        // Translation animation (moving up after +score appears)
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), label);
        translateTransition.setToY(y - 50); // Move up
        translateTransition.setCycleCount(1); // Animation only happens once
        translateTransition.play();

        // Fade transition
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), label);
        fadeTransition.setToValue(0); // Fade out
        fadeTransition.setCycleCount(1);
        fadeTransition.setOnFinished(event -> Platform.runLater(() -> controller.root.getChildren().remove(label)));
        fadeTransition.play();
    }

    /**
     * Displays an animated message on the screen. Used to display powers' activation messages, like "INVERTED PADDLE CONTROLS :>"
     * @param message     The message to be displayed.
     * @param controller  The instance of the game controller.
     */
    public void showMessage(String message, final Controller controller) {
        final Label label = new Label(message);
        label.setScaleX(2);
        label.setScaleY(2);
        // Center the label in the middle of the screen
        label.setTranslateX((double) Model.sceneWidth / 2 - 100);
        label.setTranslateY((double) Model.sceneWidth / 2);
        Platform.runLater(() -> controller.root.getChildren().add(label));
        // Translation animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), label);
        translateTransition.setToY(((double) Model.sceneHeight / 2) - 50); // Move up
        translateTransition.setCycleCount(1);
        translateTransition.play();
        // Fade animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), label);
        fadeTransition.setToValue(0); // Fade out
        fadeTransition.setCycleCount(1);
        // Removes label from the scene after animation is over
        fadeTransition.setOnFinished(event -> Platform.runLater(() -> controller.root.getChildren().remove(label)));
        fadeTransition.play();
    }

    /**
     * Displays the "Game Over" message along with a back to menu (restart) button.
     * @param controller  The instance of the game controller.
     */
    public void showGameOver(final Controller controller) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over (T_T)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            GameButton restart = new GameButton("Back", "back.png", 130, 380);
            restart.setOnAction(event -> controller.restartGame());

            controller.root.getChildren().addAll(label, restart);
        });
    }

    /**
     * Displays the "You Win" message along with a back to menu (restart) button.
     * @param controller  The instance of the game controller.
     */
    public void showWin(final Controller controller) {
        Platform.runLater(() -> {
            Label label = new Label("You Win ٩(◕‿◕｡)۶");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);
            GameButton restart = new GameButton("Back", "back.png", 130, 380);
            restart.setOnAction(event -> {
                label.setVisible(false);
                controller.restartGame();
            });
            controller.root.getChildren().addAll(label, restart);
        });
    }
}
