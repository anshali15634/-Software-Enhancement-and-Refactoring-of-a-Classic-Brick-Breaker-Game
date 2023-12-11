package brickGame;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.layout.Pane;

import java.util.concurrent.CopyOnWriteArrayList;
/**
 * The View class manages the visual representation and animations in the Brick Breaker game.
 */
public class View {
    private static View instance;

    protected GameButton newGame = new GameButton("Start New Game", "new_game_button.png",130,290);
    protected GameButton loadGame = new GameButton("Load Game", "load_button.png", 130, 470);
    protected GameButton about = new GameButton("About", "how_to_play.png", 130, 380);
    protected GameButton exit = new GameButton("Exit", "exit_button.png", 130, 560);
    protected GameButton back = new GameButton("Back", "back.png",0,630);

    protected Label loadLabel;
    protected Label pauseLabel;
    protected Label scoreLabel;
    protected Label heartLabel;
    protected Label levelLabel;
    public static FadeTransition fadeTransition;
    public static final Color[] colors = new Color[] {
            Color.valueOf("#B81DC2"),
            Color.valueOf("#EC6360"),
            Color.valueOf("#EA4574"),
            Color.valueOf("#C60A9E"),
            Color.valueOf("#DB0463"),
            Color.valueOf("#DB43AD"),
            Color.valueOf("#FF835D")
    };

    /**
     * Private constructor to prevent instantiation of View class.
     */
    private View() {
        // private constructor to prevent instantiation
    }

    /**
     * Gets the singleton instance of the View class.
     * @return The View instance.
     */
    public static View getInstance() {
        if (instance == null) {
            instance = new View();
        }
        return instance;
    }

    /**
     * Sets the game icon for the specified stage.
     * @param stageName     The stage for which to set the game icon.
     * @param iconFileName  The filename of the game icon image.
     */
    public static void setGameIcon(Stage stageName, String iconFileName) {
        Image icon = new Image(iconFileName);
        stageName.getIcons().add(icon);
    }

    /**
     * Changes the background of the game based on the specified condition.
     *
     * @param gameBG  A boolean indicating whether the game background should be applied, or the start menu background
     * @param root    The root Pane of the game scene.
     */
    public static void changeGameBG(boolean gameBG, Pane root){
        if (gameBG){ // if nextLevel() calls this start() function, should change bg to game bg
            root.setStyle("-fx-background-image: url('bg2.jpg');");
        }else{
            root.setStyle("-fx-background-image: url('bg.jpg');");
        }

    }

    /**
     * Initializes various labels used in the game interface.
     * @param score       The initial score value.
     * @param level       The initial level value.
     * @param heart       The initial heart value.
     * @param sceneheight The height of the game scene.
     * @param scenewidth  The width of the game scene.
     */
    public void initLabels(int score, int level, int heart, double sceneheight, double scenewidth) {
        loadLabel = new Label("No previous games saved :<");
        loadLabel.setLayoutX(135);
        loadLabel.setLayoutY(240);
        loadLabel.setVisible(false);
        fadeTransition = new FadeTransition(Duration.seconds(2), loadLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        pauseLabel = new Label("Game Paused :)");
        pauseLabel.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
        pauseLabel.setLayoutX((double) scenewidth / 2 - 100);
        pauseLabel.setLayoutY((double) sceneheight / 2 - 20);
        pauseLabel.setVisible(false);
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(scenewidth - 90);

    }
    /**
     * Fills the specified shape with the image represented by the given filename. Implemented to remove duplicate blocks of code dedicated to image initialization in the original game code.
     * @param shape   The shape to be filled.
     * @param imgName The filename of the image.
     */
    public static void gameObjectImageFill(Shape shape, String imgName){
        shape.setFill(new ImagePattern(new Image(imgName)));
    }

    /**
     * Sets the visibility of game objects to false and menu buttons to true, making the game objects invisible.
     *
     * @param blks   The list of blocks to hide.
     * @param ball   The ball to hide.
     * @param paddle The paddle to hide.
     * @param meter  The meter to hide.
     * @param view   The instance of the View class.
     */
    public static void setNotVisibleGameObjects(CopyOnWriteArrayList<Block> blks, Circle ball, Rectangle paddle,Rectangle meter, View view){
        view.scoreLabel.setVisible(false);
        view.heartLabel.setVisible(false);
        view.levelLabel.setVisible(false);
        ball.setVisible(false);
        paddle.setVisible(false);
        meter.setVisible(false);
        for (Block block : blks) {
            block.rect.setVisible(false);
        }
        view.loadGame.setVisible(true);
        view.newGame.setVisible(true);
        view.about.setVisible(true);
        view.exit.setVisible(true);
    }

    /**
     * Sets the visibility of game objects to true and menu buttons to false, making the game objects visible.
     *
     * @param blks   The list of blocks to show.
     * @param ball   The ball to show.
     * @param paddle The paddle to show.
     * @param meter  The meter to show.
     * @param view   The instance of the View class.
     */
    public static void setVisibleGameObjects(CopyOnWriteArrayList<Block> blks, Circle ball, Rectangle paddle, Rectangle meter,View view){
        view.scoreLabel.setVisible(true);
        view.heartLabel.setVisible(true);
        view.levelLabel.setVisible(true);
        ball.setVisible(true);
        paddle.setVisible(true);
        meter.setVisible(true);
        for (Block block : blks) {
            block.rect.setVisible(true);
        }
        view.loadGame.setVisible(false);
        view.newGame.setVisible(false);
        view.about.setVisible(false);
        view.exit.setVisible(false);
    }

    /**
     * Updates the visual representation of the paddle width on the UI.
     * @param paddle The rectangle representing the paddle.
     * @param pw     The new width of the paddle.
     */
    public static void updateUIPaddleWidth(Rectangle paddle, double pw){
        Platform.runLater(() -> {
            // Update UI with the new paddle width
            paddle.setWidth(pw);
        });
    }
    /**
     * This method updates the visual representation of the gun meter based on the remaining charges.
     * @param gunMeter stores the number of shots left for the player (the gun can only be used thrice)
     */
    protected void updateUIMeter(int gunMeter, Rectangle meter){
        if (gunMeter==3){
            View.gameObjectImageFill(meter, "meterfull.png");
        }else if(gunMeter==2){
            View.gameObjectImageFill(meter, "meterhalf.png");
        }else if (gunMeter==1){
            View.gameObjectImageFill(meter, "meterone.png");
        }else{
            View.gameObjectImageFill(meter, "meterempty.png");
        }
    }

}
