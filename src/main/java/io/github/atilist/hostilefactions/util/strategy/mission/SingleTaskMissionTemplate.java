package io.github.atilist.hostilefactions.util.strategy.mission;

import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.Task;

public class SingleTaskMissionTemplate extends MissionTemplate {
    public SingleTaskMissionTemplate(Task task) {
        addTask(task);
    }
}
