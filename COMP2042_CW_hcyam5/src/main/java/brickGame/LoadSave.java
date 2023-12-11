package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
/**
 * This class handles loading and saving game data.
 */

public class LoadSave {
    public boolean          isExistHeartBlock;
    public boolean isGoldStats;
    public boolean          goDownBall;
    public boolean          goRightBall;
    public boolean collideToBreak;
    public boolean collideToBreakAndMoveToRight;
    public boolean collideToRightWall;
    public boolean collideToLeftWall;
    public boolean collideToRightBlock;
    public boolean collideToBottomBlock;
    public boolean collideToLeftBlock;
    public boolean collideToTopBlock;
    public int              level;
    public int              score;
    public int              heart;
    public int              destroyedBlockCount;
    public double           xBall;
    public double           yBall;
    public double xPaddle;
    public double yPaddle;
    public double centerPaddleX;
    public long             time;
    public long             goldTime;
    public double           vX;
    public boolean invert;
    public boolean is_short;
    public int gunMeter;
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();

    /**
     * Reads saved game data from a file.
     */
    public void read() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(Controller.savePath)));
            loadGameStats(inputStream);
            loadGameObjs(inputStream);
            loadGameFlags(inputStream);
            System.out.printf("\n IS IT GOLDEN TIME??"+isGoldStats);
            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) { // if catch block executed, no games were saved. save.mdd doesnt exist
            e.printStackTrace();
        }
    }


    /**
     * Loads game statistics from the input stream.
     * @param inputStream The object input stream.
     * @throws IOException If an error occurs while reading data.
     */
    private void loadGameStats(ObjectInputStream inputStream) throws IOException{
        try {
            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();
            destroyedBlockCount=0;
            gunMeter=inputStream.readInt();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Loads game state variables from the input stream.
     * @param inputStream The object input stream.
     * @throws IOException If an error occurs while reading data.
     */
    private void loadGameObjs(ObjectInputStream inputStream) throws IOException{
        try {
            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xPaddle = inputStream.readDouble();
            yPaddle = inputStream.readDouble();
            centerPaddleX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    /**
     * Loads game flags from the input stream.
     * @param inputStream The object input stream.
     * @throws IOException If an error occurs while reading data.
     */
    private void loadGameFlags(ObjectInputStream inputStream) throws IOException{
        try {
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStats = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            collideToBreak = inputStream.readBoolean();
            collideToBreakAndMoveToRight = inputStream.readBoolean();
            collideToRightWall = inputStream.readBoolean();
            collideToLeftWall = inputStream.readBoolean();
            collideToRightBlock = inputStream.readBoolean();
            collideToBottomBlock = inputStream.readBoolean();
            collideToLeftBlock = inputStream.readBoolean();
            collideToTopBlock = inputStream.readBoolean();
            invert = inputStream.readBoolean();
            is_short = inputStream.readBoolean();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
