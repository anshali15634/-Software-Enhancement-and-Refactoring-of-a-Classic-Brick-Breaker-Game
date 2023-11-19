package brickGame;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
//import sun.plugin2.message.Message;

public class Score {
    private static final int ANIMATION_DURATION = 500; // animation duration in ms
    public void show(final double x, final double y, int score, final Main main) {
        String sign = (score >=0)? "+":"";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() ->main.root.getChildren().add(label));

        // translation animation (moving up after +score appears)
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_DURATION),label);
        translateTransition.setToY(y - 50); // Move up
        translateTransition.setCycleCount(1); // animation only happens once
        translateTransition.play();

        // fade transition
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), label);
        fadeTransition.setToValue(0); // Fade out
        fadeTransition.setCycleCount(1);
        fadeTransition.setOnFinished(event -> Platform.runLater(() -> main.root.getChildren().remove(label)));
        fadeTransition.play();
    }

    // animation for the messages like "Level up :)"
    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> main.root.getChildren().add(label));

        // translation animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_DURATION), label);
        translateTransition.setToY(290); // Move up
        translateTransition.setCycleCount(1);
        translateTransition.play();

        // fade Animation
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(ANIMATION_DURATION), label);
        fadeTransition.setToValue(0); // Fade out
        fadeTransition.setCycleCount(1);
        // removes label from scene after animation is over
        fadeTransition.setOnFinished(event -> Platform.runLater(() -> main.root.getChildren().remove(label)));
        fadeTransition.play();
    }

    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Back");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> main.restartGame());

            main.root.getChildren().addAll(label, restart);

        });
    }

    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);


            main.root.getChildren().addAll(label);

        });
    }
}
