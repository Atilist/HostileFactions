package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.Task;

import java.util.Random;

public class HubAvoidanceTask implements Task {
    @Override
    public void assignBackupMission(DroneEntity drone) {

    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        return true;
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        return drone.isFarEnoughFromHub();
    }

    @Override
    public void execute(DroneEntity drone) {
        if (drone.isStationary()) {
            Random random = drone.world.random;
            drone.setLocationTarget(drone.hubX + bidirectionalRandom(random), drone.hubZ + bidirectionalRandom(random));
        }
    }

    private int bidirectionalRandom(Random random) {
        int value = random.nextInt(16) + 70;
        if (random.nextBoolean()) {
            value = -value;
        }
        return value;
    }
}
