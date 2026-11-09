package io.github.atilist.hostilefactions.util.strategy;

import io.github.atilist.hostilefactions.util.strategy.mission.BlockGatheringMissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.mission.SingleTaskMissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.task.HubReturnTask;
import io.github.atilist.hostilefactions.util.strategy.task.InventoryCraftingTask;
import io.github.atilist.hostilefactions.util.strategy.task.LogisticsTask;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MissionBoard {
    private static boolean unprepared = true;

    record ItemMission(Item item, MissionTemplate missionTemplate) {}

    private static final List<ItemMission> ALTERNATIVE_ITEM_MISSIONS = new ArrayList<>();

    public static final MissionTemplate
            LOG_GATHERING = new BlockGatheringMissionTemplate(new ItemStack(Block.LOG, 16), new Block[]{Block.LEAVES, Block.LOG}),
            DIRT_GATHERING = new BlockGatheringMissionTemplate(new ItemStack(Block.DIRT, 16), new Block[]{Block.DIRT, Block.GRASS_BLOCK}),
            PLANKS_CRAFTING = new SingleTaskMissionTemplate(new InventoryCraftingTask(new ItemStack[]{new ItemStack(Block.LOG, 1)}, new MissionTemplate[]{LOG_GATHERING}, new ItemStack(Block.PLANKS, 4))),
            CRAFTING_TABLE_CRAFTING = new SingleTaskMissionTemplate(new InventoryCraftingTask(new ItemStack[]{new ItemStack(Block.PLANKS, 4)}, new MissionTemplate[]{PLANKS_CRAFTING}, new ItemStack(Block.CRAFTING_TABLE, 1))),
            LOGISTICS = new MissionTemplate().addTask(new HubReturnTask()).addTask(new LogisticsTask());

    public static MissionTemplate getAlternativeResourceMission(Item resource) {
        if (unprepared) {
            initialize();
        }
        MissionTemplate alternativeMission = null;
        for (ItemMission itemMission : ALTERNATIVE_ITEM_MISSIONS) {
            if (itemMission.item.id == resource.id) {
                alternativeMission = itemMission.missionTemplate;
                break;
            }
        }
        if (alternativeMission == null) {
            System.err.println("No mission found for item \"" + resource.getTranslatedName() + "\"");
        }
        return alternativeMission;
    }

    private static void initialize() {
        unprepared = false;
        ALTERNATIVE_ITEM_MISSIONS.add(new ItemMission(Block.LOG.asItem(), LOG_GATHERING));
        ALTERNATIVE_ITEM_MISSIONS.add(new ItemMission(Block.PLANKS.asItem(), PLANKS_CRAFTING));
        ALTERNATIVE_ITEM_MISSIONS.add(new ItemMission(Block.CRAFTING_TABLE.asItem(), CRAFTING_TABLE_CRAFTING));
        ALTERNATIVE_ITEM_MISSIONS.add(new ItemMission(Block.DIRT.asItem(), DIRT_GATHERING));
    }
}
