package io.github.atilist.hostilefactions.util.mission;

import io.github.atilist.hostilefactions.entity.DroneEntity;

public interface Task {
    void assignBackupMission(DroneEntity drone);

    boolean requirementsMet(DroneEntity drone);

    boolean isFinished(DroneEntity drone);

    void execute(DroneEntity drone);
}
