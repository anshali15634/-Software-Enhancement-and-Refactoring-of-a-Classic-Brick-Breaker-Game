package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LoadSave {
    public boolean          isExistHeartBlock;
    public boolean          isGoldStauts;
    public boolean          goDownBall;
    public boolean          goRightBall;
    public boolean          colideToBreak;
    public boolean          colideToBreakAndMoveToRight;
    public boolean          colideToRightWall;
    public boolean          colideToLeftWall;
    public boolean          colideToRightBlock;
    public boolean          colideToBottomBlock;
    public boolean          colideToLeftBlock;
    public boolean          colideToTopBlock;
    public int              level;
    public int              score;
    public int              heart;
    public int              destroyedBlockCount;
    public double           xBall;
    public double           yBall;
    public double xPaddle;
    public double YPaddle;
    public double centerPaddleX;
    public long             time;
    public long             goldTime;
    public double           vX;
    public boolean invert;
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();


    public void read() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(Main.savePath)));
            loadGameStats(inputStream);
            loadGameObjs(inputStream);
            loadGameFlags(inputStream);
            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) { // if catch block executed, no games were saved. save.mdd doesnt exist
            e.printStackTrace();
        }
    }

    private void loadGameStats(ObjectInputStream inputStream) throws IOException{
        try {
            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadGameObjs(ObjectInputStream inputStream) throws IOException{
        try {
            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xPaddle = inputStream.readDouble();
            YPaddle = inputStream.readDouble();
            centerPaddleX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    private void loadGameFlags(ObjectInputStream inputStream) throws IOException{
        try {
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStauts = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            colideToBreak = inputStream.readBoolean();
            colideToBreakAndMoveToRight = inputStream.readBoolean();
            colideToRightWall = inputStream.readBoolean();
            colideToLeftWall = inputStream.readBoolean();
            colideToRightBlock = inputStream.readBoolean();
            colideToBottomBlock = inputStream.readBoolean();
            colideToLeftBlock = inputStream.readBoolean();
            colideToTopBlock = inputStream.readBoolean();
            invert = inputStream.readBoolean();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
