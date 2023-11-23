package brickGame;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
import java.util.List;
import java.util.Random;


public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {


    private int level = 0;
    public int final_level = 6; // game finishes after level 5
    public boolean invert = false; // to check if invert bonus is used
    public boolean shortPaddle = false; // to check if short paddle bonus is used

    private double xPaddle = 0.0f;
    private double centerPaddleX;
    private double yPaddle = 640.0f;

    public static final int SHORT_PADDLE_WIDTH = 90;
    public static final int NORMAL_PADDLE_WIDTH = 130;
    private int paddleWidth = NORMAL_PADDLE_WIDTH;


    private final int paddleHeight = 30;
    private final int halfBreakWidth = paddleWidth / 2;

    private final int sceneWidth = 500;

    private final int sceneHeight = 700;

    private static final int LEFT  = 1;
    private static final int RIGHT = 2;

    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;

    private Rectangle paddle;
    public static final int       ballRadius = 10;

    private int destroyedBlockCount = 0;

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

    private final CopyOnWriteArrayList<Block> blocks = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Bonus> bonusArray = new CopyOnWriteArrayList<>(); // stores bonuses for choco blocks
    public void setPaddleWidth(int pW){
        paddleWidth = pW;
    }
    public int getSceneHeight(){
        return sceneHeight;
    }
    public int getSceneWidth(){
        return sceneWidth;
    }
    private final Color[] colors = new Color[] {
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
    GameButton newGame = new GameButton("Start New Game", "new_game_button.png",130,290);
    GameButton loadGame = new GameButton("Load Game", "load_button.png", 130, 470);
    GameButton about = new GameButton("About", "how_to_play.png", 130, 380);
    GameButton exit = new GameButton("Exit", "exit_button.png", 130, 560);
    GameButton back = new GameButton("Back", "back.png",0,0);

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
        loadGame.setVisible(true);
        newGame.setVisible(true);
        about.setVisible(true);
        exit.setVisible(true);
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
        loadGame.setVisible(false);
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

        if (!loadFromSave) {
            level++;
            if (level >1 && level<final_level){
                new Score().showMessage("Level Up (๑˃ᴗ˂)ﻭ", this);
            }
            if (level==final_level){
                new Score().showWin(this);
                return;
            }

            initBall();
            initPaddle(); // initializes the paddle
            initBoard();

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
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel, newGame, about, exit, loadGame,loadLabel);
        } else {
            root.getChildren().addAll(paddle, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }

        // setting up main scene
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        // setting up "how to play" scene2
        root2 = new StackPane();

        Scene scene2 = new Scene(root2, sceneWidth, sceneHeight);
        root2.getChildren().add(back);
        root2.setStyle("-fx-background-image: url('how_To_play_bg.png')");

        back.setOnAction(e -> primaryStage.setScene(scene));
        about.setOnAction(e -> primaryStage.setScene(scene2));
        exit.setOnAction(e -> {
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

            loadGame.setOnAction(event -> {
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
                }else if (r % 10 == 4){
                    type = Block.BLOCK_INVERT;
                }else if (r % 10 == 6){
                    type = Block.BLOCK_SHORT;
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
                if (invert) {
                    move(RIGHT);
                }else{
                    move(LEFT);
                }
                break;
            case RIGHT:
                if (invert) {
                    move(LEFT);
                }else{
                    move(RIGHT);
                }
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
        }
    }

    private void move(final int direction) {
        new Thread(() -> {
            int sleepTime = 0;
            for (int i = 0; i < 30; i++) {
                if (xPaddle == (sceneWidth - paddleWidth) && direction == RIGHT) {
                    return;
                }
                if (xPaddle == 0 && direction == LEFT) {
                    return;
                }
                if (direction == RIGHT) {
                    xPaddle++;
                } else {
                    xPaddle--;
                }
                centerPaddleX = xPaddle + halfBreakWidth;
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
        yBall = random.nextInt(sceneHeight - 200) + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    private void initPaddle() {
        paddle = new Rectangle();
        paddle.setWidth(paddleWidth);
        paddle.setHeight(paddleHeight);
        paddle.setX(xPaddle);
        paddle.setY(yPaddle);

        ImagePattern pattern = new ImagePattern(new Image("paddle.png"));

        paddle.setFill(pattern);
    }


    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean collideToPaddle = false;
    private boolean collideToPaddleAndMoveToRight = true;
    private boolean collideToRightWall = false;
    private boolean collideToLeftWall = false;
    private boolean collideToRightBlock = false;
    private boolean collideToBottomBlock = false;
    private boolean collideToLeftBlock = false;
    private boolean collideToTopBlock = false;

    private double vX = 2.000;
    private final double vY = 2.500;


    private void resetCollideFlags() {

        collideToPaddle = false;
        collideToPaddleAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    private void setPhysicsToBall() {
        moveBall();
        handleBallYBoundaries();
        handleBallPaddleCollision();
        handleBallXBoundaries();
        handleBallWallCollisions();
        handleBallBlockCollision();
    }

    private void moveBall(){
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
    }

    private void handleBallYBoundaries(){
        if (yBall <= 0) {
            resetCollideFlags();
            goDownBall = true;
            return;
        }
        if (yBall >= sceneHeight) {
            goDownBall = false;
            if (!isGoldStatus) {
                heart--;
                System.out.println("\nHEART DEDUCT");
                new Score().show((int)((double) sceneWidth / 2), (int)((double) sceneHeight / 2), -1, this);

                if (heart == 0) {
                    onUpdate();
                    new Score().showGameOver(this);
                    engine.stop();
                }

            }
        }
    }

    private void handleBallPaddleCollision(){
        if (yBall >= yPaddle - ballRadius) {
            if (xBall >= xPaddle && xBall <= xPaddle + paddleWidth) {
                hitTime = time;
                resetCollideFlags();
                collideToPaddle = true;
                goDownBall = false;
                double relation = (xBall - centerPaddleX) / (int)((double)paddleWidth / 2);
                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                }
                collideToPaddleAndMoveToRight = xBall - centerPaddleX > 0;
            }
        }
        if (collideToPaddle) {
            goRightBall = collideToPaddleAndMoveToRight;
        }
    }



    private void handleBallXBoundaries(){
        if (xBall >= sceneWidth) {
            resetCollideFlags();
            collideToRightWall = true;
        }

        if (xBall <= 0) {
            resetCollideFlags();
            collideToLeftWall = true;
        }
    }

    private void handleBallWallCollisions(){
        if (collideToRightWall) {
            goRightBall = false;
        }
        if (collideToLeftWall) {
            goRightBall = true;
        }
    }


    private void handleBallBlockCollision(){
        if (collideToRightBlock) {
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            goRightBall = true;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
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
                saveGameInfo(outputStream);
                saveBlockInfo(outputStream);

                new Score().showMessage("Game Saved", Main.this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeOutputStream(outputStream);
            }
        }).start();
    }

    private void saveGameInfo(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeInt(level);
        outputStream.writeInt(score);
        outputStream.writeInt(heart);
        outputStream.writeInt(destroyedBlockCount);

        outputStream.writeDouble(xBall);
        outputStream.writeDouble(yBall);
        outputStream.writeDouble(xPaddle);
        outputStream.writeDouble(yPaddle);
        outputStream.writeDouble(centerPaddleX);
        outputStream.writeLong(time);
        outputStream.writeLong(goldTime);
        outputStream.writeDouble(vX);

        outputStream.writeBoolean(isExistHeartBlock);
        outputStream.writeBoolean(isGoldStatus);
        outputStream.writeBoolean(goDownBall);
        outputStream.writeBoolean(goRightBall);
        outputStream.writeBoolean(collideToPaddle);
        outputStream.writeBoolean(collideToPaddleAndMoveToRight);
        outputStream.writeBoolean(collideToRightWall);
        outputStream.writeBoolean(collideToLeftWall);
        outputStream.writeBoolean(collideToRightBlock);
        outputStream.writeBoolean(collideToBottomBlock);
        outputStream.writeBoolean(collideToLeftBlock);
        outputStream.writeBoolean(collideToTopBlock);
        outputStream.writeBoolean(invert);
    }

    private void saveBlockInfo(ObjectOutputStream outputStream) throws IOException {
        ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
        for (Block block : blocks) {
            if (block.isDestroyed) {
                continue;
            }
            blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
        }
        outputStream.writeObject(blockSerializables);
    }

    private void closeOutputStream(ObjectOutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGame() {
        LoadSave loadSave = new LoadSave();
        loadSave.read();

        copyGameInfo(loadSave);
        copyBlockInfo(loadSave);

        blocks.clear();
        bonusArray.clear();

        List<Block> newBlocks = new ArrayList<>();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            newBlocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }

        blocks.addAll(newBlocks);

        try {
            loadFromSave = true;
            gameBG = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyGameInfo(LoadSave loadSave) {
        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStauts;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToPaddle = loadSave.colideToBreak;
        collideToPaddleAndMoveToRight = loadSave.colideToBreakAndMoveToRight;
        collideToRightWall = loadSave.colideToRightWall;
        collideToLeftWall = loadSave.colideToLeftWall;
        collideToRightBlock = loadSave.colideToRightBlock;
        collideToBottomBlock = loadSave.colideToBottomBlock;
        collideToLeftBlock = loadSave.colideToLeftBlock;
        collideToTopBlock = loadSave.colideToTopBlock;
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xPaddle = loadSave.xPaddle;
        yPaddle = loadSave.YPaddle;
        centerPaddleX = loadSave.centerPaddleX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;
        invert = loadSave.invert;
    }

    private void copyBlockInfo(LoadSave loadSave) {
        blocks.clear();
        bonusArray.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }
    }

    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                System.out.println("Number of bricks and number destroyed: "+blocks.size()+" "+destroyedBlockCount);
                invert = false;
                shortPaddle=false;
                setPaddleWidth(NORMAL_PADDLE_WIDTH);
                paddle.setWidth(paddleWidth);
                vX = 2.000;
                engine.stop();
                resetCollideFlags();
                goDownBall = true;
                isGoldStatus = false;
                isExistHeartBlock = false;
                hitTime = 0;
                time = 0;
                goldTime = 0;
                engine.stop();
                blocks.clear();
                bonusArray.clear();
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

            invert = false;
            shortPaddle = false;
            setPaddleWidth(NORMAL_PADDLE_WIDTH);
            paddle.setWidth(paddleWidth);

            destroyedBlockCount = 0;
            resetCollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            blocks.clear();
            bonusArray.clear();
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
            paddle.setX(xPaddle);
            paddle.setY(yPaddle);
            ball.setCenterX(xBall);
            ball.setCenterY(yBall);

            for (Bonus choco : bonusArray) {
                choco.choco.setY(choco.y);
            }
        });

        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(xBall, yBall);
                handleBlockHit(hitCode,block);
            }
        }
    }

    private void handleBlockHit(int hitCode, Block block){
        if (hitCode != Block.NO_HIT) {
            score += 1;

            new Score().show(block.x, block.y, 1, this);

            block.rect.setVisible(false);
            block.isDestroyed = true;
            destroyedBlockCount++;

            resetCollideFlags();
            handleBlockType(block);

            if (hitCode == Block.HIT_RIGHT) {
                collideToRightBlock = true;
            } else if (hitCode == Block.HIT_BOTTOM) {
                collideToBottomBlock = true;
            } else if (hitCode == Block.HIT_LEFT) {
                collideToLeftBlock = true;
            } else if (hitCode == Block.HIT_TOP) {
                collideToTopBlock = true;
            }
        }
    }
    private void handleBlockType(Block block){
        System.out.println("\nBLOCK BONUS ADDED");
        if (block.type == Block.BLOCK_CHOCO) {
            final Bonus bonus1 = new Bonus(block.row, block.column,1);
            bonus1.timeCreated = time;
            Platform.runLater(() -> root.getChildren().add(bonus1.choco));
            bonusArray.add(bonus1);
        }

        if (block.type == Block.BLOCK_INVERT){
            final Bonus bonus1 = new Bonus(block.row, block.column, 2);
            bonus1.timeCreated = time;
            Platform.runLater(() -> root.getChildren().add(bonus1.choco));
            bonusArray.add(bonus1);
        }

        if (block.type == Block.BLOCK_SHORT){
            final Bonus bonus1 = new Bonus(block.row, block.column, 3);
            bonus1.timeCreated = time;
            Platform.runLater(() -> root.getChildren().add(bonus1.choco));
            bonusArray.add(bonus1);
        }

        if (block.type == Block.BLOCK_STAR) {
            goldTime = time;
            ball.setFill(new ImagePattern(new Image("goldball.png")));
            System.out.println("gold ball");
            new Score().showMessage("GOLD BALL - UNLIMITED LIVES :>", Main.this);
            //root.getStyleClass().add("goldRoot");
            isGoldStatus = true;
        }

        if (block.type == Block.BLOCK_HEART) {
            heart++;
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
            //root.getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }
        // all bonuses are run through
        for (Bonus bonus : bonusArray) {
            if (bonus.y > sceneHeight || bonus.taken) {
                continue; // skip this block and go to next choco
            }
            if (bonus.y >= yPaddle && bonus.y <= yPaddle + paddleHeight && bonus.x >= xPaddle && bonus.x <= xPaddle + paddleWidth) {
                if (bonus.bonusType==1){
                    System.out.println("You Got it and +3 score for you");
                    score += 3;
                    new Score().show(bonus.x, bonus.y, 3, this);
                }else if (bonus.bonusType == 2){
                    new Score().showMessage("INVERTED PADDLE CONTROLS :>", Main.this);
                    invert=!invert;
                }else if (bonus.bonusType == 3){
                    new Score().showMessage("CAREFUL! SHORT PADDLE!", Main.this);
                    shortPaddle = !shortPaddle;
                    if (shortPaddle) {
                        setPaddleWidth(SHORT_PADDLE_WIDTH);
                    }else{
                        setPaddleWidth(NORMAL_PADDLE_WIDTH);
                    }
                    paddle.setWidth(paddleWidth);
                }
                bonus.choco.setVisible(false);
                bonus.taken = true;
            }
            bonus.y += ((time - bonus.timeCreated) / 1000.000) + 1.000;
        }
    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
