package io.github.atilist.hostilefactions.util.strategy.construction;

import lombok.Getter;
import net.minecraft.block.Block;

public class RequiredBlocks {
    @Getter
    private final Block block;

    public int requiredAmount;

    public RequiredBlocks(Block block) {
        this.block = block;
    }
}
