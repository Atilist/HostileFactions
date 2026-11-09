package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.Task;
import io.github.atilist.hostilefactions.util.strategy.construction.BlockRemovalPosition;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructure;

import java.util.List;

public class FactionStructureClearingTask implements Task {
    @Override
    public void assignBackupMission(DroneEntity drone) {
    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        return true;
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        if (drone.currentStructure == null) {
            return true;
        }
        drone.currentStructure.checkStructure(drone);
        return drone.currentStructure.getBlockRemovalPositions().isEmpty();
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
        List<BlockRemovalPosition> blocksToRemove = currentStructure.getBlockRemovalPositions();
        if (blocksToRemove.isEmpty()) {
            return;
        }
        BlockRemovalPosition position = blocksToRemove.get(blocksToRemove.size() - 1);
        drone.breakBlock(position.x(), position.y(), position.z());
    }
}
