package io.github.atilist.hostilefactions.util.strategy;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MissionTemplate {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }
}
