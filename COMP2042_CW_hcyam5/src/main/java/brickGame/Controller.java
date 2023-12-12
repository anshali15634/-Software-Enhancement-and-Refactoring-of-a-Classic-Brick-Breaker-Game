package brickGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  This class serves as the main controller for the Brick Breaker game.
 *  It handles game initialization, gameplay logic which depends on interactions with other classes and
 *  also handles interactions with the user interface.
 */
public class Controller extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private boolean          startGame = false;
    public boolean           gameBG = false;

    public static String     savePath;
    public static String     savePathDir;
    public static final int  LEFT  = 1;
    public static final int  RIGHT = 2;
    public  Pane             root;
    public Pane              root2;
    private View view = View.getInstance();
    private Model model = Model.getInstance();
    private boolean downPress = false; // the gun can only shoot if the down button is pressed (so gun is activated.)


    Stage  primaryStage;

    /**
     * This function checks if the device contains a D Drive. The original game code assumes the existence of a D Drive for saved game files without checking if it's actually present.
     * @return True if the D Drive exists, False if otherwise.
     */
    private static boolean checkForDDrive() {
        File[] drives = File.listRoots();
        for (File drive : drives) {
            if (drive.getAbsolutePath().equals("D:\\")) {
                return true;
            }
        }
        return false;
    }
    /**
     * This function uses the checkForDDrive() function and alters the pre-defined save path file directory accordingly.
     */
    public static void setSavePaths() {
        boolean hasDDrive = checkForDDrive();
        if (hasDDrive) {
            savePath = "D:/save/save.mdds";
            savePathDir = "D:/save/";
        } else {
            savePath = "./save/save.mdds";
            savePathDir = "./save/";
        }
    }


/**
 * This method initializes the game window, loads resources, and starts the game engine.
 * @param primaryStage It is the main window on which the game will be displayed.
 **/
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        View.setGameIcon(primaryStage, "game_icon.png" );
        if (!model.getLoadFromSave()) {
            model.incLevel();
            if (model.getLevel() >1 && model.getLevel()<Model.final_level){
                new Score().showMessage("Level Up (๑˃ᴗ˂)ﻭ", this);
            }
            if (model.getLevel()==Model.final_level) {
                new Score().showWin(this);
                return;
            }
            initBoard();
            initPaddle();
            initBall();
            initMeter();
        }
        root = new Pane();
        root2 = new Pane();
        View.changeGameBG(gameBG, root);
        view.initLabels(model.getScore(), model.getLevel(), model.getHeart(), Model.SCENE_HEIGHT, Model.SCENE_WIDTH);
        if (!model.getLoadFromSave()) {
            root.getChildren().addAll(model.getBall(),model.getMeter(), view.scoreLabel, view.heartLabel, view.levelLabel,
                    view.newGame, view.about, view.exit, view.loadGame,view.loadLabel,view.pauseLabel,model.getPaddle());
        } else {
            root.getChildren().addAll(model.getBall(), model.getMeter(), view.scoreLabel, view.heartLabel,
                    view.levelLabel, view.pauseLabel,model.getPaddle());
        }
        for (Block block : model.blocks) {
            root.getChildren().add(block.rect);
        }

        // set up the main scene for gameplay
        Scene scene = new Scene(root, Model.SCENE_WIDTH, Model.SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        // set up scene for showing game instructions
        Scene scene2 = new Scene(root2, Model.SCENE_WIDTH, Model.SCENE_HEIGHT);
        root2.getChildren().add(view.back);
        root2.setStyle("-fx-background-image: url('how_To_play_bg.png')");

        view.back.setOnAction(e -> primaryStage.setScene(scene));
        view.about.setOnAction(e -> primaryStage.setScene(scene2));
        view.exit.setOnAction(e -> {
            Stage stage = (Stage) view.exit.getScene().getWindow();
            stage.close();
        });
        for (Block block : model.blocks) {
            block.rect.setVisible(false);
        }
        if (!startGame){
            View.setNotVisibleGameObjects(model.blocks, model.getBall(), model.getPaddle(), model.getMeter(), view);
        }
        primaryStage.setTitle("BrickBreaker");
        primaryStage.setScene(scene);
        primaryStage.show();
        if (!model.getLoadFromSave()) {
            if (model.getLevel() > 1 && model.getLevel() < Model.final_level) {
                View.setVisibleGameObjects(model.blocks, model.getBall(), model.getPaddle(), model.getMeter(), view);;
                startEngine();
            }
            view.loadGame.setOnAction(event -> {
                File file = new File(savePath);
                if (file.exists()){
                    loadGame();
                    model.updatePaddleWidth(model.isShortPaddle());
                    View.updateUIPaddleWidth(model.getPaddle(), model.getPaddleWidth());
                    view.updateUIMeter(model.getGunMeter(),model.getMeter());
                    View.setVisibleGameObjects(model.blocks, model.getBall(), model.getPaddle(), model.getMeter(), view);
                }else{
                    view.loadLabel.setVisible(true);
                    View.fadeTransition.play();
                }
            });
            view.newGame.setOnAction(event -> {
                root.setStyle("-fx-background-image: url('bg2.jpg');");
                //engine = GameEngine.getInstance();
                startEngine();
                View.setVisibleGameObjects(model.blocks, model.getBall(), model.getPaddle(), model.getMeter(), view);
            });
        } else {
            startEngine();
            model.setLoadFromSave(false);
        }
    }

    /**
     * This method starts the game engine, sets its frame rate, and registers the controller as an action listener to receive updates.
     */
    private void startEngine(){
        model.getGameEngine().setOnAction(this);
        model.getGameEngine().setFps(120);
        model.getGameEngine().start();
    }


/** This method is the entry point for the BrickBreaker application.
 *  Launches the game logic and user interface.
 */
    public static void main(String[] args) {
        setSavePaths();
        launch(args);
    }

    /**
     * This method handles user input events (keyboard presses).
     * Original game code only included LEFT,RIGHT and S key events.
     * This version of the game includes the key events LEFT, RIGHT, S, SPACE, UP and DOWN
     * @param event A KeyEvent object containing information about the user's keyboard press.
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                if (model.isInvert()) {
                    move(RIGHT);
                }else{
                    move(LEFT);
                }
                break;
            case RIGHT:
                if (model.isInvert()) {
                    move(LEFT);
                }else{
                    move(RIGHT);
                }
                break;
            case UP:
                Platform.runLater(() -> {
                    if (model.getGunMeter()>0 && downPress){
                        View.gameObjectImageFill(model.getPaddle(), "paddle.png");
                        model.decGunMeter();
                        view.updateUIMeter(model.getGunMeter(),model.getMeter());
                        downPress=false;
                        initBullet();
                    }
                });

                break;
            case S:
                saveGame();
                break;
            case SPACE:
                togglePause();
                break;
            case DOWN:
                Platform.runLater(() ->{
                        if (model.getGunMeter()>0) {
                            View.gameObjectImageFill(model.getPaddle(), "gunpaddle.png");
                            downPress=true;
                        }
                });
                break;
        }
    }
    /**
     * This method toggles the pause state of the game.
     */
    private void togglePause() {
        model.setIsPaused(!model.getIsPaused());
        if (model.getIsPaused()) {
            view.pauseLabel.setVisible(true);
            model.getGameEngine().pause();
        } else {
            view.pauseLabel.setVisible(false);
            model.getGameEngine().resume();
        }
    }
    /**
     * This method smoothly moves the paddle in the specified direction (LEFT or RIGHT).
     * Original game's code for this function included a sleepTime > 0 to
     * @param direction Stores either LEFT or RIGHT (numeric constants of the Controller class)
     **/
    private void move(final int direction) {
        new Thread(() -> {
            int sleepTime = 0;
            for (int i = 0; i < 30; i++) {
                model.movePaddle(direction);
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
/***
 * This method initializes the ball's position and appearance.
 */
    private void initBall() {
        Random random = new Random();
        model.setXBall(random.nextInt(Model.SCENE_WIDTH) + 1);
        model.setYBall(random.nextInt(Model.SCENE_HEIGHT - 200) + ((model.getLevel() + 1) * Block.getHeight()) + 15);
        model.getBall().setRadius(Model.BALL_RADIUS);
        View.gameObjectImageFill(model.getBall(),"ball.png");
    }
/***
 * This method initializes the paddle's size, position, and appearance, and especially focuses on the paddle width, in case the player has caught a shortPaddlePower.
 */
    private void initPaddle() {
        if (model.isShortPaddle()){
            model.getPaddle().setWidth(Model.SHORT_PADDLE_WIDTH);
        }else{
            model.getPaddle().setWidth(Model.NORMAL_PADDLE_WIDTH);
        }
        model.getPaddle().setHeight(Model.PADDLE_HEIGHT);
        model.getPaddle().setX(model.getXPaddle());
        model.getPaddle().setY(model.getYPaddle());
        View.gameObjectImageFill(model.getPaddle(),"paddle.png");
    }
    /**
     *This method initializes the game's board with blocks of various types.
     * */
    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < model.getLevel() + Model.LAST_BLOCK_ROW; j++) {
                int blockType = generateBlockType();
                if (blockType!=-1){
                    model.blocks.add(BlockFactory.createBlock(blockType,j,i));
                }
            }
        }
    }

    /**
     * This function generates a block of random type (could either be a choco block, a star block, a heart block, etc) by randomly generating an integer and assigning it to a block type.
     * @return A Block object produced by random generation.
     */
    private int generateBlockType(){
        int r = new Random().nextInt(500)%10;
        switch(r){
            case (0):
                return -1;
            case (1):
                return Block.BLOCK_CHOCO;
            case (2):
                if (!model.isIsExistHeartBlock()) {
                    model.setIsExistHeartBlock(true);
                    return Block.BLOCK_HEART;
                } else {
                    return Block.BLOCK_NORMAL;
                }
            case (3):
                return Block.BLOCK_STAR;
            case (4):
                return Block.BLOCK_INVERT;
            case (6):
                return Block.BLOCK_SHORT;
            default:
                return Block.BLOCK_NORMAL;
        }
    }

    /**
     *This method initializes the visual representation of the gun meter.
     */
    private void initMeter(){
        model.getMeter().setWidth(Model.NORMAL_PADDLE_WIDTH-5);
        model.getMeter().setHeight(Model.PADDLE_HEIGHT -5);
        model.getMeter().setX(190);
        model.getPaddle().setY(0);
        view.updateUIMeter(model.getGunMeter(),model.getMeter());
    }
    /**
     *  This method creates a new bullet object and initializes its position to the paddle's center and sets up its visual representation.
     */
    private void initBullet(){
        Bullet bullet = new Bullet(model.getHalfPaddleWidth()+model.getXPaddle(), model.getYPaddle());
        model.bullets.add(bullet);
        root.getChildren().add(bullet);
    }
    /**
     *  This method performs a check if all the blocks are broken, and validates entry to the next level.
     */
    private void checkDestroyedCount() {
        if (model.getDestroyedBlockCount() == model.blocks.size()) {
            nextLevel();
        }

    }
    /**
     * This method saves the current game state to a file in a separate thread.
     */
    private void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(savePath);
            ObjectOutputStream outputStream = null;

            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));
                saveGameInfo(outputStream);
                saveBlockInfo(outputStream);

                new Score().showMessage("Game Saved", Controller.this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeOutputStream(outputStream);
            }
        }).start();
    }
    /**
     *  This method saves various game state information to the specified object output stream, enabling them to be restored later when loading the saved game.
     * @param outputStream Represents the object output stream used to write the game state information to a file.
     */
    private void saveGameInfo(ObjectOutputStream outputStream) throws IOException {
        outputStream.writeInt(model.getLevel());
        outputStream.writeInt(model.getScore());
        outputStream.writeInt(model.getHeart());
        outputStream.writeInt(model.getDestroyedBlockCount());
        outputStream.writeInt(model.getGunMeter());

        outputStream.writeDouble(model.getXBall());
        outputStream.writeDouble(model.getYBall());
        outputStream.writeDouble(model.getXPaddle());
        outputStream.writeDouble(model.getYPaddle());
        outputStream.writeDouble(model.getCenterPaddleX());
        outputStream.writeLong(model.getTime());
        outputStream.writeLong(model.getGoldTime());
        outputStream.writeDouble(model.getvX());

        outputStream.writeBoolean(model.isIsExistHeartBlock());
        outputStream.writeBoolean(model.getIsGoldStats());
        outputStream.writeBoolean(model.isGoDownBall());
        outputStream.writeBoolean(model.isGoRightBall());
        outputStream.writeBoolean(model.isCollideToPaddle());
        outputStream.writeBoolean(model.isCollideToPaddleAndMoveToRight());
        outputStream.writeBoolean(model.isCollideToRightWall());
        outputStream.writeBoolean(model.isCollideToLeftWall());
        outputStream.writeBoolean(model.isCollideToRightBlock());
        outputStream.writeBoolean(model.isCollideToBottomBlock());
        outputStream.writeBoolean(model.isCollideToLeftBlock());
        outputStream.writeBoolean(model.isCollideToTopBlock());
        outputStream.writeBoolean(model.isInvert());
        outputStream.writeBoolean(model.isShortPaddle());
    }
/**
 * This method saves the information of active blocks in the game state to the specified output stream.
 * @param outputStream Represents the object output stream used to write the game state information to a file.
 */
    private void saveBlockInfo(ObjectOutputStream outputStream) throws IOException {
        ArrayList<BlockSerializable> blockSerializables = new ArrayList<>();
        for (Block block : model.blocks) {
            if (block.isDestroyed) {
                continue;
            }
            blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
        }
        outputStream.writeObject(blockSerializables);
    }
/**
 * This method safely closes the provided ObjectOutputStream.
 * @param outputStream Represents the object output stream used to write the game state information to a file.
 */
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

    /***
     *  This method loads a previously saved game state and resumes the game from that point.
     */
    private void loadGame() {
        LoadSave loadSave = new LoadSave();
        loadSave.read();

        copyGameInfo(loadSave);
        copyBlockInfo(loadSave);

        model.blocks.clear();
        model.powerArray.clear();

        List<Block> newBlocks = new ArrayList<>();

        for (BlockSerializable ser : loadSave.blocks) {
            Block newBlock=BlockFactory.createBlock(ser.type,ser.row,ser.j);
            newBlocks.add(newBlock);
        }

        model.blocks.addAll(newBlocks);

        try {
            model.setLoadFromSave(true);
            gameBG = true;
            startGame=true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method copies various game information from a LoadSave object to the game model.
     * @param loadSave stores the game state of a previously saved game.
     */
    private void copyGameInfo(LoadSave loadSave) {
        model.setIsExistHeartBlock(loadSave.isExistHeartBlock);
        model.setIsGoldStats(loadSave.isGoldStats);
        model.setGoDownBall(loadSave.goDownBall);
        model.setGoRightBall(loadSave.goRightBall);
        model.setCollideToPaddle(loadSave.collideToBreak);
        model.setCollideToPaddleAndMoveToRight(loadSave.collideToBreakAndMoveToRight);
        model.setCollideToRightWall(loadSave.collideToRightWall);
        model.setCollideToLeftWall(loadSave.collideToLeftWall);
        model.setCollideToRightBlock(loadSave.collideToRightBlock);
        model.setCollideToBottomBlock(loadSave.collideToBottomBlock);
        model.setCollideToLeftBlock(loadSave.collideToLeftBlock);
        model.setCollideToTopBlock(loadSave.collideToTopBlock);
        model.setLevel(loadSave.level);
        model.setScore(loadSave.score);
        model.setHeart(loadSave.heart);
        model.setDestroyedBlockCount(loadSave.destroyedBlockCount);
        model.setGunMeter(loadSave.gunMeter);
        model.setXBall(loadSave.xBall);
        model.setYBall(loadSave.yBall);
        model.setXPaddle(loadSave.xPaddle);
        model.setYPaddle(loadSave.yPaddle);
        model.setCenterPaddleX(loadSave.centerPaddleX);
        model.setTime(loadSave.time);
        model.setGoldTime(loadSave.goldTime);
        model.setvX(loadSave.vX);
        model.setInvert(loadSave.invert);
        model.setShortPaddle(loadSave.is_short);
    }

    /**
     * This method clears existing blocks and powers, and copies the information of active blocks from the LoadSave object to the game model.
     * @param loadSave stores the game state of a previously saved game.
     */
    private void copyBlockInfo(LoadSave loadSave) {
        model.blocks.clear();
        model.powerArray.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            Block newBlock = BlockFactory.createBlock(ser.type, ser.row, ser.j);
            model.blocks.add(newBlock);
        }
    }

    /**
     * This method prepares the game for the next level after the current level is completed.
     * Restarts the game loop with the updated level information.
     */
    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                model.resetFlags();
                View.updateUIPaddleWidth(model.getPaddle(), model.getPaddleWidth());
                model.getGameEngine().stop();
                gameBG=true;
                startGame=true;
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    /**
     * This method restarts the game from the beginning, resetting various game parameters and initiating a new gameplay session.
     */
    public void restartGame() {

        try {
            model.setLevel(0);
            model.setHeart(3);
            model.setScore(0);
            model.setGunMeter(3);
            model.resetFlags();
            View.updateUIPaddleWidth(model.getPaddle(), model.getPaddleWidth());
            gameBG=false;
            startGame=false;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method updates the game state variables on display (hearts, score, etc) and performs necessary calculations to track blocks' collisions with bullets or the ball. It also handles the collisions according to the block type.
     * For example if a collided block was of type BlockHeart, a Power object of type heartPower is created and released onto game screen.
     */
    @Override
    public void onUpdate() {
        if (!model.getIsPaused()) {
            Platform.runLater(() -> {

                view.scoreLabel.setText("Score: " + model.getScore());
                view.heartLabel.setText("Heart : " + model.getHeart());
                model.getPaddle().setX(model.getXPaddle());
                model.getPaddle().setY(model.getYPaddle());
                model.getBall().setCenterX(model.getXBall());
                model.getBall().setCenterY(model.getYBall());
                for (Power choco : model.powerArray) {
                    choco.newPowerBlock.setY(choco.y);
                }
            });

            if (model.getYBall() >= Block.getPaddingTop() && model.getYBall() <= (Block.getHeight() * (model.getLevel() + Model.LAST_BLOCK_ROW)) + Block.getPaddingTop()) {
                for (final Block block : model.blocks) {
                    int hitCode = model.checkHitToBlock(model.getXBall(), model.getYBall(), block);
                    handleBlockHit(hitCode, block);
                }
            }
            for (final Block block: model.blocks) {
                for (Bullet bullet : model.bullets) {
                    if (bullet.getIsDestroyed()) {
                        continue;
                    }
                    int hitCode = model.checkBulletHitToBlock(bullet.getX(), bullet.getY(), block);
                    handleBlockHit(hitCode,block);
                }
            }
        }
    }

    /**
     * This method handles the logic when the ball collides with a block or bullet. If collision happened, then game state variables are updated.
     * @param block the Block object to be checked for collisions with a block or bullet.
     */
    private void handleBlockHit(int hitCode, Block block){
        if (hitCode != Block.NO_HIT) {
            model.incScore();
            new Score().show(block.x, block.y, 1, this);
            block.rect.setVisible(false);
            block.isDestroyed = true;
            model.setDestroyedBlockCount(model.getDestroyedBlockCount()+1);
            model.resetCollideFlags();
            handleBlockType(block);
            model.setHitFlags(hitCode);
        }
    }
    /**
     * This method matches the block's type to its respective type of power and creates a Power object.
     * @param block the Block object which receives an initialized matching Power object.
     */
    private void handleBlockType(Block block) {
        block.initPower();
        if (block.pow != null) {
            block.pow.timeCreated = model.getTime();
            Platform.runLater(() -> root.getChildren().add(block.pow.newPowerBlock));
            model.powerArray.add(block.pow);
        }
    }

    @Override
    public void onInit() {

    }

    /**
     * This method is called within the game loop to handle various physics updates and interactions.
     * It checks if all the blocks are destroyed in the given level
     * It halts the gold power effect (unfreezes lives) by checking if over time limit (5 seconds)
     * It performs collision detection on the falling powers with the paddle.
     * It performs collision detection on the bullets with the blocks.
     */
    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        model.setPhysicsToBall(this);

        if (model.getTime() - model.getGoldTime() > 5000) {
            View.gameObjectImageFill(model.getBall(), "ball.png");
            model.setIsGoldStats(false);
        }
        // all bonuses are run through
        for (Power power : model.powerArray) {
            if (power.y > Model.SCENE_HEIGHT || power.taken) {
                continue; // skip this block and go to next choco
            }
            if (power.y >= model.getYPaddle() && power.y <= model.getYPaddle() + Model.PADDLE_HEIGHT && power.x >= model.getXPaddle() && power.x <= model.getXPaddle() + model.getPaddleWidth()) {
                handlePowerType(power);
                power.powerMessage(this);
                power.newPowerBlock.setVisible(false);
                power.taken = true;
            }
            power.y += ((model.getTime() - power.timeCreated) / 1000.000) + 1.000;
        }
        for (Bullet bullet: model.bullets){
            if (bullet.getY()<=0){
                bullet.setVisible(false);
                bullet.setDestroyed(true);
                continue;
            }
            if (bullet.getIsDestroyed()){
                continue;
            }
            Platform.runLater(() -> bullet.setY(bullet.getY() - 5));

        }
    }

/**
 * A helper method for the function onPhysicsUpdate(). It executes the power's effects (if the power was caught by the paddle) by updating game state variables.
 * @param power Informs what type of power was caught by the paddle.
 */
    private void handlePowerType(Power power){
        if (power instanceof scorePlusPower){
            model.setScore(model.getScore()+3);
            new Score().show(power.x, power.y, 3, this);
        }else if (power instanceof invertPower){
            model.setInvert(!model.isInvert());
        }else if (power instanceof shortPaddlePower){
            model.setShortPaddle(!model.isShortPaddle());
            model.updatePaddleWidth(model.isShortPaddle());
            View.updateUIPaddleWidth(model.getPaddle(), model.getPaddleWidth());
        }else if (power instanceof heartPower){
            model.incHeart();
        }else if (power instanceof goldPower){
            model.setGoldTime(model.getTime());
            View.gameObjectImageFill(model.getBall(),"goldball.png");
            model.setIsGoldStats(true);
        }
    }
    /**
     * This function records the current time during the game to the variable time in the Model class.
     * @param time stores the current time during the game.
     */
    @Override
    public void onTime(long time) {
        model.setTime(time);
    }

}

