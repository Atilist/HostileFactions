package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.MissionBoard;
import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.Task;

public class LogisticsTask implements Task {
    @Override
    public void assignBackupMission(DroneEntity drone) {
        if (drone.neededItem == null) {
            return;
        }
        MissionTemplate backupMission = MissionBoard.getAlternativeResourceMission(drone.neededItem);
        if (backupMission != null) {
            drone.setMission(backupMission);
        }
        drone.neededItem = null;
    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        return drone.neededItem == null;
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        return drone.neededItem == null;
    }

    @Override
    public void execute(DroneEntity drone) {

    }
}
