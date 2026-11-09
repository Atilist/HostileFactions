package io.github.atilist.hostilefactions.util.strategy.construction;

import lombok.Getter;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class RequiredBlocks {
    @Getter
    private final Block block;
    public final List<BlockPosition> positions = new ArrayList<>();

    public int requiredAmount;

    public RequiredBlocks(Block block) {
        this.block = block;
    }
}
