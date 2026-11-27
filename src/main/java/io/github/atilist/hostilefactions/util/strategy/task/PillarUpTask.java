package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.MissionBoard;
import io.github.atilist.hostilefactions.util.strategy.Task;

public class PillarUpTask implements Task {
    @Override
    public void assignBackupMission(DroneEntity drone) {
        drone.setMission(MissionBoard.HUB_RETURN);
    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        return drone.hasPathCorrectionBlocks();
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        return drone.y + 2 >= drone.world.getTopSolidBlockY((int) Math.floor(drone.x), (int) Math.floor(drone.z));
    }

    @Override
    public void execute(DroneEntity drone) {
        if (drone.hasLocationTarget) {
            drone.removeLocationTarget();
        }
        drone.forceJumping();
        drone.pillarUp();
    }
}
