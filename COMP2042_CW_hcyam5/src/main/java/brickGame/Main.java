package brickGame;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {


    private int level = 0;
    public int final_level = 4; // game finishes after level 3

    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 640.0f;

    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;

    private int sceneWidth = 500;
    private int sceneHeigt = 700;

    private static int LEFT  = 1;
    private static int RIGHT = 2;

    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean isGoldStauts      = false;
    private boolean isExistHeartBlock = false;

    private Rectangle paddle;
    public static final int       ballRadius = 10;

    private int destroyedBlockCount = 0;

    private double v = 1.000;

    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long hitTime  = 0;
    private long goldTime = 0;

    public boolean gameBG = false;

    private GameEngine engine;
    public static String savePath;
    public static String savePathDir;

    private Label loadLabel;

    public FadeTransition fadeTransition;

    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();

    private Color[] colors = new Color[] {
            Color.valueOf("#B81DC2"),
            Color.valueOf("#EC6360"),
            Color.valueOf("#EA4574"),
            Color.valueOf("#C60A9E"),
            Color.valueOf("#DB0463"),
            Color.valueOf("#DB43AD"),
            Color.valueOf("#FF835D")
    };

    public  Pane             root;

    public Pane              root2;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;

    Stage  primaryStage;
    Button load    = null;
    Button newGame = null;
    Button exit = null; // exit the program

    Button about = null; // navigates user to how to play scene

    Button back = null;
    // the two following functions are to check if the computer has a d drive and setting the save paths accordingly
    private static boolean checkForDDrive() {
        File[] drives = File.listRoots();
        for (File drive : drives) {
            if (drive.getAbsolutePath().equals("D:\\")) {
                return true;
            }
        }
        return false;
    }
    public static void setSavePaths() {
        // Check if the computer has a D drive for saving game progress
        boolean hasDDrive = checkForDDrive();

        // Set save paths based on the presence of D drive
        if (hasDDrive) {
            savePath = "D:/save/save.mdds";
            savePathDir = "D:/save/";
        } else {
            savePath = "C:/save/save.mdds";
            savePathDir = "C:/save/";
        }
    }

    public void setNotVisibleGameObjects(){
        scoreLabel.setVisible(false);
        heartLabel.setVisible(false);
        levelLabel.setVisible(false);
        ball.setVisible(false);
        paddle.setVisible(false);
        for (Block block : blocks) {
            block.rect.setVisible(false);
        }
    }
    public void setVisibleGameObjects(){
        scoreLabel.setVisible(true);
        heartLabel.setVisible(true);
        levelLabel.setVisible(true);
        ball.setVisible(true);
        paddle.setVisible(true);
        for (Block block : blocks) {
            block.rect.setVisible(true);
        }
        load.setVisible(false);
        newGame.setVisible(false);
        about.setVisible(false);
        exit.setVisible(false);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);

        // adding game icon
        Image icon = new Image("game_icon.png");
        primaryStage.getIcons().add(icon);

        // "new game" button's image
        Image new_game_button_img = new Image("new_game_button.png");
        ImageView imageView = new ImageView(new_game_button_img); // converting button img for display - line 123

        //"about" button's image
        Image about_button_img = new Image("how_to_play.png");
        ImageView imageView1 = new ImageView(about_button_img);

        //"exit" button's image
        Image exit_button_img = new Image("exit_button.png");
        ImageView imageView2 = new ImageView(exit_button_img);

        //"load" button's image
        Image load_button = new Image("load_button.png");
        ImageView imageView3 = new ImageView(load_button);

        //"back" button's image
        Image back_button = new Image("back.png");
        ImageView imageView4 = new ImageView(back_button);

        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        imageView1.setFitHeight(50);
        imageView1.setPreserveRatio(true);

        imageView2.setFitHeight(50);
        imageView2.setPreserveRatio(true);

        imageView3.setFitHeight(50);
        imageView3.setPreserveRatio(true);

        imageView4.setFitHeight(50);
        imageView4.setPreserveRatio(true);

        if (!loadFromSave) {
            level++;
            if (level >1 && level<final_level){
                new Score().showMessage("Level Up :)", this);
            }
            if (level==final_level){
                new Score().showWin(this);
                return;
            }

            initBall();
            initBreak(); // initializes the paddle
            initBoard();

            load = new Button("Load Game");
            newGame = new Button("Start New Game");
            about = new Button("About");
            exit = new Button("Exit");

            newGame.setPrefSize(30, 10); //(width,height)
            newGame.setStyle("-fx-background-color: #291741;");
            newGame.setGraphic(imageView);

            about.setPrefSize(30, 10); //(width,height)
            about.setStyle("-fx-background-color: #291741;");
            about.setGraphic(imageView1);

            load.setPrefSize(30, 10); //(width,height)
            load.setStyle("-fx-background-color: #291741;");
            load.setGraphic(imageView3);

            exit.setPrefSize(30, 10); //(width,height)
            exit.setStyle("-fx-background-color: #291741;");
            exit.setGraphic(imageView2);

            newGame.setTranslateX(130);
            newGame.setTranslateY(290);

            load.setTranslateX(130);
            load.setTranslateY(380);

            about.setTranslateX(130);
            about.setTranslateY(470);

            exit.setTranslateX(130);
            exit.setTranslateY(560);

            // if no previous game saved, when load button pressed should display this message
            loadLabel = new Label("No previous games saved :<");
            loadLabel.setLayoutX(135);
            loadLabel.setLayoutY(240);
            loadLabel.setVisible(false);
            fadeTransition = new FadeTransition(Duration.seconds(2), loadLabel);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);


        }


        root = new Pane();
        if (gameBG){ // if nextLevel() calls this start() function, should change bg to game bg
            root.setStyle("-fx-background-image: url('bg2.jpg');");
        }else{
            root.setStyle("-fx-background-image: url('bg.jpg');");
        }
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 90);
        if (!loadFromSave) {
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel, newGame, about, exit,load,loadLabel);
        } else {
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }

        // setting up main scene
        Scene scene = new Scene(root, sceneWidth, sceneHeigt);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        // setting up "how to play" scene2
        root2 = new StackPane();

        // button back will navigate back to first scene
        back = new Button("Back");
        StackPane.setAlignment(back, Pos.BOTTOM_LEFT);

        back.setPrefSize(30, 10); //(width,height)
        back.setStyle("-fx-background-color: #291741;");
        back.setGraphic(imageView4);

        back.setOnAction(e -> primaryStage.setScene(scene));

        root2.getChildren().add(back);
        Scene scene2 = new Scene(root2, sceneWidth, sceneHeigt);
        root2.setStyle("-fx-background-image: url('how_To_play_bg.png')");
        scene2.setFill(Color.BLACK);

        // about button should navigate to the "How To Play" scene
        about.setOnAction(e -> primaryStage.setScene(scene2));
        exit.setOnAction(e -> {
            // Close the window when the button is clicked
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });
        setNotVisibleGameObjects();
        primaryStage.setTitle("BrickBreaker");
        primaryStage.setScene(scene);
        primaryStage.show();


        if (!loadFromSave) {
            if (level > 1 && level < final_level) {
                setVisibleGameObjects();
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }

            load.setOnAction(event -> {
                // check if the save file exists first - else no game to load
                File file = new File(savePath);
                if (file.exists()){
                    loadGame();
                    setVisibleGameObjects();
                }else{
                    // Label
                    loadLabel.setVisible(true);
                    fadeTransition.play();
                }
            });

            newGame.setOnAction(event -> {
                root.setStyle("-fx-background-image: url('bg2.jpg');");
                engine = new GameEngine();
                engine.setOnAction(Main.this);
                engine.setFps(120);
                engine.start();

                setVisibleGameObjects();
            });
        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }


    }

    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));

            }
        }
    }


    public static void main(String[] args) {
        setSavePaths();
        launch(args);
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:

                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
        }
    }

    float oldXBreak;

    private void move(final int direction) {
        new Thread(() -> {
            int sleepTime = 4;
            for (int i = 0; i < 30; i++) {
                if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                    return;
                }
                if (xBreak == 0 && direction == LEFT) {
                    return;
                }
                if (direction == RIGHT) {
                    xBreak++;
                } else {
                    xBreak--;
                }
                centerBreakX = xBreak + halfBreakWidth;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();


    }


    private void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        yBall = random.nextInt(sceneHeigt - 200) + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    private void initBreak() {
        paddle = new Rectangle();
        paddle.setWidth(breakWidth);
        paddle.setHeight(breakHeight);
        paddle.setX(xBreak);
        paddle.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("paddle.png"));

        paddle.setFill(pattern);
    }


    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean colideToBreak               = false;
    private boolean colideToBreakAndMoveToRight = true;
    private boolean colideToRightWall           = false;
    private boolean colideToLeftWall            = false;
    private boolean colideToRightBlock          = false;
    private boolean colideToBottomBlock         = false;
    private boolean colideToLeftBlock           = false;
    private boolean colideToTopBlock            = false;

    private double vX = 2.000;
    private double vY = 2.000;


    private void resetColideFlags() {

        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;

        colideToRightBlock = false;
        colideToBottomBlock = false;
        colideToLeftBlock = false;
        colideToTopBlock = false;
    }

    private void setPhysicsToBall() {
        //v = ((time - hitTime) / 1000.000) + 1.000;

        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (yBall <= 0) {
            //vX = 1.000;
            resetColideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeigt) {
            goDownBall = false;
            if (!isGoldStauts) {
                //TODO gameover
                heart--;
                new Score().show(sceneWidth / 2, sceneHeigt / 2, -1, this);

                if (heart == 0) {
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }
            //return;
        }

        if (yBall >= yBreak - ballRadius) {
            //System.out.println("Colide1");
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                hitTime = time;
                resetColideFlags();
                colideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                    //System.out.println("vX " + vX);
                }

                if (xBall - centerBreakX > 0) {
                    colideToBreakAndMoveToRight = true;
                } else {
                    colideToBreakAndMoveToRight = false;
                }
                //System.out.println("Colide2");
            }
        }

        if (xBall >= sceneWidth) {
            resetColideFlags();
            //vX = 1.000;
            colideToRightWall = true;
        }

        if (xBall <= 0) {
            resetColideFlags();
            //vX = 1.000;
            colideToLeftWall = true;
        }

        if (colideToBreak) {
            if (colideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
        }

        //Wall Colide

        if (colideToRightWall) {
            goRightBall = false;
        }

        if (colideToLeftWall) {
            goRightBall = true;
        }

        //Block Colide

        if (colideToRightBlock) {
            goRightBall = true;
        }

        if (colideToLeftBlock) {
            goRightBall = true;
        }

        if (colideToTopBlock) {
            goDownBall = false;
        }

        if (colideToBottomBlock) {
            goDownBall = true;
        }


    }


    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...

            nextLevel();
        }
    }

    private void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(destroyedBlockCount);


                outputStream.writeDouble(xBall);
                outputStream.writeDouble(yBall);
                outputStream.writeDouble(xBreak);
                outputStream.writeDouble(yBreak);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(vX);


                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStauts);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(colideToBreak);
                outputStream.writeBoolean(colideToBreakAndMoveToRight);
                outputStream.writeBoolean(colideToRightWall);
                outputStream.writeBoolean(colideToLeftWall);
                outputStream.writeBoolean(colideToRightBlock);
                outputStream.writeBoolean(colideToBottomBlock);
                outputStream.writeBoolean(colideToLeftBlock);
                outputStream.writeBoolean(colideToTopBlock);

                ArrayList<BlockSerializable> blockSerializables = new ArrayList<BlockSerializable>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                }

                outputStream.writeObject(blockSerializables);

                new Score().showMessage("Game Saved", Main.this);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // prevents NullPointerException in some cases
                    assert outputStream != null;
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void loadGame() {
        LoadSave loadSave = new LoadSave();
        loadSave.read();


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStauts = loadSave.isGoldStauts;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        colideToBreak = loadSave.colideToBreak;
        colideToBreakAndMoveToRight = loadSave.colideToBreakAndMoveToRight;
        colideToRightWall = loadSave.colideToRightWall;
        colideToLeftWall = loadSave.colideToLeftWall;
        colideToRightBlock = loadSave.colideToRightBlock;
        colideToBottomBlock = loadSave.colideToBottomBlock;
        colideToLeftBlock = loadSave.colideToLeftBlock;
        colideToTopBlock = loadSave.colideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            loadFromSave = true;
            gameBG=true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                vX = 2.000;

                engine.stop();
                resetColideFlags();
                goDownBall = true;

                isGoldStauts = false;
                isExistHeartBlock = false;


                hitTime = 0;
                time = 0;
                goldTime = 0;

                engine.stop();
                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;
                gameBG=true;
                start(primaryStage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void restartGame() {

        try {
            level = 0;
            heart = 3;
            score = 0;
            vX = 2.000;
            destroyedBlockCount = 0;
            resetColideFlags();
            goDownBall = true;

            isGoldStauts = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();
            gameBG=false;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdate() {
        Platform.runLater(() -> {

            scoreLabel.setText("Score: " + score);
            heartLabel.setText("Heart : " + heart);

            paddle.setX(xBreak);
            paddle.setY(yBreak);
            ball.setCenterX(xBall);
            ball.setCenterY(yBall);

            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }
        });


        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(xBall, yBall);
                if (hitCode != Block.NO_HIT) {
                    score += 1;

                    new Score().show(block.x, block.y, 1, this);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;

                    //System.out.println("size is " + blocks.size());
                    resetColideFlags();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(() -> root.getChildren().add(choco.choco));
                        chocos.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("goldball.png")));
                        System.out.println("gold ball");
                        root.getStyleClass().add("goldRoot");
                        isGoldStauts = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        heart++;
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        colideToRightBlock = true;
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        colideToBottomBlock = true;
                    } else if (hitCode == Block.HIT_LEFT) {
                        colideToLeftBlock = true;
                    } else if (hitCode == Block.HIT_TOP) {
                        colideToTopBlock = true;
                    }

                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }


    @Override
    public void onInit() {

    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();


        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
            isGoldStauts = false;
        }

        for (Bonus choco : chocos) {
            if (choco.y > sceneHeigt || choco.taken) {
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

        //System.out.println("time is:" + time + " goldTime is " + goldTime);

    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
