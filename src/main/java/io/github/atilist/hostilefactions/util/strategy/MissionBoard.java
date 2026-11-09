package io.github.atilist.hostilefactions.util.strategy;

import io.github.atilist.hostilefactions.util.strategy.mission.BlockGatheringMissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.mission.SingleTaskMissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.task.InventoryCraftingTask;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class MissionBoard {
    public static final MissionTemplate
            LOG_GATHERING = new BlockGatheringMissionTemplate(new ItemStack(Block.LOG, 16), new Block[]{Block.LEAVES, Block.LOG}),
            PLANKS_CRAFTING = new SingleTaskMissionTemplate(new InventoryCraftingTask(new ItemStack[]{new ItemStack(Block.LOG, 1)}, new MissionTemplate[]{LOG_GATHERING}, new ItemStack(Block.PLANKS, 4))),
            CRAFTING_TABLE_CRAFTING = new SingleTaskMissionTemplate(new InventoryCraftingTask(new ItemStack[]{new ItemStack(Block.PLANKS, 4)}, new MissionTemplate[]{PLANKS_CRAFTING}, new ItemStack(Block.CRAFTING_TABLE, 1)));
}
