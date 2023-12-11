package brickGame;

import javafx.scene.paint.Color;

import java.util.Random;
/**
 * This class's responsibility is to help in creating an object in any of the following types: BlockChoco, BlockHeart, BlockShort, BlockStar, BlockInvert or BlockPlain.
 */

public class BlockFactory {
    private static int r;
    /**This function creates a Block object of a specified type. In the original game code, this is done by multiple if-else statements throughout the game code.
     * @param blockType Informs the function what type of block is required.
     * @param row Specifies which row to place the block on the game screen amongst the other blocks.
     * @param col Specifies which column to place the block on the game screen amongst the other blocks.
     * @return A Block object of a specific block type with initialized position and color.
     */
    public static Block createBlock(int blockType, int row, int col){
        switch (blockType) {
            case Block.BLOCK_CHOCO:
                return new BlockChoco(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_HEART:
                return new BlockHeart(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_NORMAL:
                r=new Random().nextInt(200);
                return new BlockPlain(row, col, View.colors[r % View.colors.length], blockType);
            case Block.BLOCK_SHORT:
                return new BlockShort(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_STAR:
                return new BlockStar(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_INVERT:
                return new BlockInvert(row, col, Color.TRANSPARENT, blockType);
            default:
                return new BlockPlain(row, col, View.colors[r % View.colors.length], blockType);
        }
    }
}
