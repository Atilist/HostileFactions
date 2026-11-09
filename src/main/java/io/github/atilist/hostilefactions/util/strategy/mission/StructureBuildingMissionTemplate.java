package io.github.atilist.hostilefactions.util.strategy.mission;

import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructure;
import io.github.atilist.hostilefactions.util.strategy.task.FactionStructureAssignmentTask;
import io.github.atilist.hostilefactions.util.strategy.task.FactionStructureClearingTask;

public class StructureBuildingMissionTemplate extends MissionTemplate {
    public StructureBuildingMissionTemplate(FactionStructure factionStructure) {
        addTask(new FactionStructureAssignmentTask(factionStructure));
        addTask(new FactionStructureClearingTask());
    }
}
