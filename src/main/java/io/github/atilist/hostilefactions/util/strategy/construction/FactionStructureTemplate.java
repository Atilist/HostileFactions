package io.github.atilist.hostilefactions.util.strategy.construction;

import lombok.Getter;
import net.minecraft.block.Block;

public class FactionStructureTemplate {
    @Getter
    private final Block[][][] blocks;
    @Getter
    private final int xLength, yLength, zLength;

    public FactionStructureTemplate(Block[][][] blocks) {
        xLength = blocks.length;
        yLength = blocks[0].length;
        zLength = blocks[0][0].length;
        this.blocks = blocks;
    }

    public FactionStructureTemplate(String[][] pattern, FactionStructureBlockEntry[] entries) {
        xLength = pattern.length;
        yLength = pattern[0].length;
        zLength = pattern[0][0].length();
        Block[][][] generatedBlocks = new Block[xLength][yLength][zLength];
        for (int xEntry = 0; xEntry < xLength; xEntry++) {
            for (int yEntry = 0; yEntry < yLength; yEntry++) {
                String zSlice = pattern[xEntry][yEntry];
                for (int zEntry = 0; zEntry < zLength; zEntry++) {
                    char zKey = zSlice.charAt(zEntry);
                    if (zKey == ' ') {
                        generatedBlocks[xEntry][yEntry][zEntry] = Block.LOCKED_CHEST; // Locked chest = air placeholder
                    } else {
                        for (FactionStructureBlockEntry entry : entries) {
                            if (entry.key() == zKey) {
                                generatedBlocks[xEntry][yEntry][zEntry] = entry.block();
                                break;
                            }
                        }
                    }
                }
            }
        }
        this.blocks = generatedBlocks;
    }
}
