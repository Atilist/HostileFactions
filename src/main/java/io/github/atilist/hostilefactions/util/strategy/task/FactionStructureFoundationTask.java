package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.MissionBoard;
import io.github.atilist.hostilefactions.util.strategy.Task;
import io.github.atilist.hostilefactions.util.strategy.construction.BlockPosition;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructure;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FactionStructureFoundationTask implements Task {
    private final ItemStack DIRT_STACK = new ItemStack(Block.DIRT, 1);

    @Override
    public void assignBackupMission(DroneEntity drone) {
        if (drone.currentStructure == null) {
            return;
        }
        drone.neededItem = Block.DIRT.asItem();
        drone.setMission(MissionBoard.LOGISTICS);
    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        return drone.hasItems(DIRT_STACK);
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        FactionStructure currentStructure = drone.currentStructure;
        if (currentStructure == null) {
            return true;
        }
        return currentStructure.hasFoundation(drone);
    }

    @Override
    public void execute(DroneEntity drone) {
        FactionStructure currentStructure = drone.currentStructure;
        if (currentStructure == null) {
            return;
        }
        if (!drone.isAtLocation(currentStructure.getX(), currentStructure.getY(), currentStructure.getZ())) {
            drone.setLocationTarget(currentStructure.getX(), currentStructure.getY(), currentStructure.getZ());
        }
        List<BlockPosition> foundationGaps = currentStructure.getFoundationGaps();
        if (foundationGaps.isEmpty()) {
            return;
        }
        BlockPosition position = foundationGaps.get(foundationGaps.size() - 1);
        drone.placeBlock(position.x(), position.y(), position.z(), Block.DIRT);
    }
}
