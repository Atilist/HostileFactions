package io.github.atilist.hostilefactions.util.mission;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import lombok.Getter;

import java.util.List;

public class Mission {
    private final List<Task> tasks;
    private final DroneEntity drone;

    private int currentTask = 0;
    @Getter
    private boolean complete = false;

    public Mission(DroneEntity drone, MissionTemplate template) {
        this.drone = drone;
        tasks = template.getTasks();
    }

    public void attemptTask() {
        Task task = tasks.get(currentTask);
        if (task.requirementsMet(drone)) {
            task.execute(drone);
        } else {
            task.assignBackupMission(drone);
            return;
        }
        if (task.isFinished(drone)) {
            currentTask++;
            if (currentTask >= tasks.size()) {
                complete = true;
            }
        }
    }

}
