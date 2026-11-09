package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.MissionBoard;
import io.github.atilist.hostilefactions.util.strategy.Task;
import io.github.atilist.hostilefactions.util.strategy.construction.BlockPosition;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructure;
import io.github.atilist.hostilefactions.util.strategy.construction.RequiredBlocks;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FactionStructureBuildTask implements Task {
    @Override
    public void assignBackupMission(DroneEntity drone) {
        FactionStructure currentStructure = drone.currentStructure;
        if (currentStructure == null) {
            return;
        }
        List<RequiredBlocks> requiredBlocksList = currentStructure.getRequiredBlocksList();
        if (requiredBlocksList.isEmpty()) {
            return;
        }
        RequiredBlocks firstEntry = requiredBlocksList.get(0);
        drone.neededItem = firstEntry.getBlock().asItem();
        drone.setMission(MissionBoard.LOGISTICS);
    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        FactionStructure currentStructure = drone.currentStructure;
        if (currentStructure == null) {
            return true;
        }
        List<RequiredBlocks> requiredBlocksList = currentStructure.getRequiredBlocksList();
        if (requiredBlocksList.isEmpty()) {
            return true;
        }
        RequiredBlocks firstEntry = requiredBlocksList.get(0);
        ItemStack requiredItems = new ItemStack(firstEntry.getBlock(), 1);
        return drone.hasItems(requiredItems);
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        if (drone.currentStructure == null) {
            return true;
        }
        return drone.currentStructure.checkStructure(drone);
    }

    @Override
    public void execute(DroneEntity drone) {
        FactionStructure currentStructure = drone.currentStructure;
        if (currentStructure == null) {
            return;
        }
        List<RequiredBlocks> requiredBlocksList = currentStructure.getRequiredBlocksList();
        if (requiredBlocksList.isEmpty()) {
            return;
        }
        RequiredBlocks firstEntry = requiredBlocksList.get(0);
        BlockPosition firstPosition = firstEntry.positions.get(0);
        drone.placeBlock(firstPosition.x(), firstPosition.y(), firstPosition.z(), firstEntry.getBlock());
    }
}
