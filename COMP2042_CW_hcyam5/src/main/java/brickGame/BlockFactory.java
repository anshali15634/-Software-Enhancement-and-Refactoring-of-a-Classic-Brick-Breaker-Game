package brickGame;

import javafx.scene.paint.Color;

import java.util.Random;

public class BlockFactory {
    private static final int r = new Random().nextInt(200);
    public static Block createBlock(int blockType, int row, int col){
        return switch (blockType) {
            case Block.BLOCK_CHOCO -> new BlockChoco(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_HEART -> new BlockHeart(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_NORMAL -> new BlockPlain(row, col, View.colors[r % View.colors.length], blockType);
            case Block.BLOCK_SHORT -> new BlockShort(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_STAR -> new BlockStar(row, col, Color.TRANSPARENT, blockType);
            case Block.BLOCK_INVERT -> new BlockInvert(row, col, Color.TRANSPARENT, blockType);
            default -> new BlockPlain(row, col, View.colors[r % View.colors.length], blockType);
        };
    }
}
