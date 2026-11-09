package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.Task;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructure;

public class FactionStructureAssignmentTask implements Task {
    private final FactionStructure factionStructure;

    public FactionStructureAssignmentTask(FactionStructure factionStructure) {
        this.factionStructure = factionStructure;
    }

    @Override
    public void assignBackupMission(DroneEntity drone) {

    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        return true;
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        return factionStructure == drone.currentStructure;
    }

    @Override
    public void execute(DroneEntity drone) {
        drone.currentStructure = factionStructure;
    }
}
