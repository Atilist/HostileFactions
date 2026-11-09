package io.github.atilist.hostilefactions.util.strategy.mission;

import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.task.BlockGatheringTask;
import io.github.atilist.hostilefactions.util.strategy.task.HubReturnTask;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlockGatheringMissionTemplate extends MissionTemplate {
    public BlockGatheringMissionTemplate(ItemStack itemsToGather, Block[] validBlocks) {
        addTask(new BlockGatheringTask(itemsToGather, validBlocks));
        addTask(new HubReturnTask());
    }
}
