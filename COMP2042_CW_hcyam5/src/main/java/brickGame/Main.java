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


public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
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
            savePath = "./save/save.mdds";
            savePathDir = "./save/";
        }
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        View.setGameIcon(primaryStage, "game_icon.png" );
        if (!model.getLoadFromSave()) {
            System.out.println("current level, level after final: "+model.getLevel()+" "+Model.final_level);
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
            initMeter(model.getGunMeter());
        }
        root = new Pane();
        root2 = new Pane();
        View.changeGameBG(gameBG, root);
        view.initLabels(model.getScore(), model.getLevel(), model.getHeart(), Model.sceneHeight, Model.sceneWidth);
        if (!model.getLoadFromSave()) {
            root.getChildren().addAll(model.getBall(),model.getMeter(), view.scoreLabel, view.heartLabel, view.levelLabel, view.newGame, view.about, view.exit, view.loadGame,view.loadLabel,view.pauseLabel,model.getPaddle());
        } else {
            root.getChildren().addAll(model.getBall(), model.getMeter(), view.scoreLabel, view.heartLabel, view.levelLabel, view.pauseLabel,model.getPaddle());
        }
        for (Block block : model.blocks) {
            root.getChildren().add(block.rect);
        }

        // set up the main scene for gameplay
        Scene scene = new Scene(root, Model.sceneWidth, Model.sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);

        // set up scene for showing game instructions
        Scene scene2 = new Scene(root2, Model.sceneWidth, Model.sceneHeight);
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
                View.setVisibleGameObjects(model.blocks, model.getBall(), model.getPaddle(), model.getMeter(), view);
                //engine = GameEngine.getInstance();
                startEngine();
            }
            view.loadGame.setOnAction(event -> {
                // check if the save file exists first - else no game to load
                File file = new File(savePath);
                if (file.exists()){
                    loadGame();
                    model.updatePaddleWidth(model.isShortPaddle());
                    View.updateUIPaddleWidth(model.getPaddle(), model.getPaddleWidth());
                    updateUIMeter(model.getGunMeter());
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
            //engine = GameEngine.getInstance();
            startEngine();
            model.setLoadFromSave(false);
        }
    }

    private void startEngine(){
        model.getGameEngine().setOnAction(this);
        model.getGameEngine().setFps(120);
        model.getGameEngine().start();
    }



    public static void main(String[] args) {
        setSavePaths();
        launch(args);
    }

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
                        updateUIMeter(model.getGunMeter());
                        downPress=false;
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

    private void initBall() {
        Random random = new Random();
        model.setXBall(random.nextInt(Model.sceneWidth) + 1);
        model.setYBall(random.nextInt(Model.sceneHeight - 200) + ((model.getLevel() + 1) * Block.getHeight()) + 15);
        //ball = new Circle();
        model.getBall().setRadius(Model.ballRadius);
        View.gameObjectImageFill(model.getBall(),"ball.png");
    }

    private void initPaddle() {
        if (model.isShortPaddle()){
            model.getPaddle().setWidth(Model.SHORT_PADDLE_WIDTH);
        }else{
            model.getPaddle().setWidth(Model.NORMAL_PADDLE_WIDTH);
        }
        model.getPaddle().setHeight(Model.paddleHeight);
        model.getPaddle().setX(model.getXPaddle());
        model.getPaddle().setY(model.getYPaddle());
        View.gameObjectImageFill(model.getPaddle(),"paddle.png");
    }
    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < model.getLevel() + Model.LAST_BLOCK_ROW; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!model.isIsExistHeartBlock()) {
                        type = Block.BLOCK_HEART;
                        model.setIsExistHeartBlock(true);
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
                model.blocks.add(new Block(j, i, View.colors[r % (View.colors.length)], type));

            }
        }
    }
    private void initMeter(int gunMeter){
        model.getMeter().setWidth(Model.NORMAL_PADDLE_WIDTH-5);
        model.getMeter().setHeight(Model.paddleHeight-5);
        model.getMeter().setX(190);
        model.getPaddle().setY(0);
        updateUIMeter(model.getGunMeter());
    }

    private void updateUIMeter(int gunMeter){
        if (gunMeter==3){
            View.gameObjectImageFill(model.getMeter(), "meterfull.png");
        }else if(gunMeter==2){
            View.gameObjectImageFill(model.getMeter(), "meterhalf.png");
        }else if (gunMeter==1){
            View.gameObjectImageFill(model.getMeter(), "meterone.png");
        }else{
            View.gameObjectImageFill(model.getMeter(), "meterempty.png");
        }
    }

    private void checkDestroyedCount() {
        if (model.getDestroyedBlockCount() == model.blocks.size()) {
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

        model.blocks.clear();
        model.powerArray.clear();

        List<Block> newBlocks = new ArrayList<>();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            newBlocks.add(new Block(ser.row, ser.j, View.colors[r % View.colors.length], ser.type));
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

        System.out.println("\nFROM FILE SHORTPADDLE: "+ model.isShortPaddle());
    }

    private void copyBlockInfo(LoadSave loadSave) {
        model.blocks.clear();
        model.powerArray.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            model.blocks.add(new Block(ser.row, ser.j, View.colors[r % View.colors.length], ser.type));
        }
    }

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
            //System.out.println("\nCALLED BY ONUPDATE");
            //model.setPhysicsToBall(this);

            if (model.getYBall() >= Block.getPaddingTop() && model.getYBall() <= (Block.getHeight() * (model.getLevel() + Model.LAST_BLOCK_ROW)) + Block.getPaddingTop()) {
                for (final Block block : model.blocks) {
                    int hitCode = model.checkHitToBlock(model.getXBall(), model.getYBall(), block);
                    handleBlockHit(hitCode, block);
                }
            }
        }
    }

    private void handleBlockHit(int hitCode, Block block){
        if (hitCode != Block.NO_HIT) {
            model.incScore();
            new Score().show(block.x, block.y, 1, this);
            block.rect.setVisible(false);
            block.isDestroyed = true;
            model.setDestroyedBlockCount(model.getDestroyedBlockCount()+1);
            model.resetCollideFlags();
            handleBlockType(block);
            model.setHitFlags(hitCode, block);
        }
    }
    private void handleBlockType(Block block) {
        Power pow1;

        if (block.type == Block.BLOCK_CHOCO) {
            pow1 = new scorePlusPower(block.row, block.column);
        } else if (block.type == Block.BLOCK_INVERT) {
            pow1 = new invertPower(block.row, block.column);
        } else if (block.type == Block.BLOCK_SHORT) {
            pow1 = new shortPaddlePower(block.row, block.column);
        } else if (block.type == Block.BLOCK_STAR) {
            pow1 = new goldPower(block.row, block.column);
        } else if (block.type == Block.BLOCK_HEART) {
            pow1 = new heartPower(block.row, block.column);
        } else {
            pow1 = null;
        }

        if (pow1 != null) {
            pow1.timeCreated = model.getTime();
            Platform.runLater(() -> root.getChildren().add(pow1.newPowerBlock));
            model.powerArray.add(pow1);
        }
    }


    @Override
    public void onInit() {

    }

    // remains in the Main class as it involves alot of objects from different classes interacting.
    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        //System.out.println("\nCALLED BY ONPHYSICSUPDATE");
        model.setPhysicsToBall(this);

        if (model.getTime() - model.getGoldTime() > 5000) {
            View.gameObjectImageFill(model.getBall(), "ball.png");
            model.setIsGoldStats(false);
        }
        // all bonuses are run through
        for (Power power : model.powerArray) {
            if (power.y > Model.sceneHeight || power.taken) {
                continue; // skip this block and go to next choco
            }
            if (power.y >= model.getYPaddle() && power.y <= model.getYPaddle() + Model.paddleHeight && power.x >= model.getXPaddle() && power.x <= model.getXPaddle() + model.getPaddleWidth()) {
                if (power instanceof scorePlusPower){
                    System.out.println("You Got it and +3 score for you");
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
                    System.out.println("gold ball");
                    model.setIsGoldStats(true);
                }
                power.powerMessage(this);
                power.newPowerBlock.setVisible(false);
                power.taken = true;
            }
            power.y += ((model.getTime() - power.timeCreated) / 1000.000) + 1.000;
        }
    }


    @Override
    public void onTime(long time) {
        model.setTime(time);
    }

}
