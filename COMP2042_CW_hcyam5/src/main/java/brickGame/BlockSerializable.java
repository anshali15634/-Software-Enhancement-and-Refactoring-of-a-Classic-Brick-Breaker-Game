package brickGame;

import java.io.Serializable;
/**
 * This class initialises a block object which will store the block state into a file.
 */
public class BlockSerializable implements Serializable {
    public final int row;
    public final int j;
    public final int type;
/**
 * This method stores all the block's game state details required for reloading a saved game.
 * @param row Stores the row in which the block is located in the block grid on the game screen.
 * @param j Stores the column in which the block is located in the block grid on the game screen.
 * @param type Stores the numerical equivalent of the block's block type for future re-initialisation (normal, choco, etc)
 */
    public BlockSerializable(int row , int j , int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
