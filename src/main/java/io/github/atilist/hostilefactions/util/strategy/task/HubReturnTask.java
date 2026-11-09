package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.Task;

public class HubReturnTask implements Task {
    @Override
    public void assignBackupMission(DroneEntity drone) {

    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        return true;
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        return drone.isAtHub();
    }

    @Override
    public void execute(DroneEntity drone) {
        if (drone.isStationary()) {
            drone.returnToHub();
        }
    }
}
