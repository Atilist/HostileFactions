package io.github.atilist.hostilefactions.util.strategy.construction;

import io.github.atilist.hostilefactions.util.strategy.Age;
import org.jetbrains.annotations.NotNull;

public class FactionBuildingTemplate {
    private final FactionStructureTemplate[] levelStructures;
    private final int maxLevel;

    public FactionBuildingTemplate(@NotNull FactionStructureTemplate[] levelStructures) {
        this.levelStructures = levelStructures;
        this.maxLevel = levelStructures.length - 1;
    }

    public int getMaxLevel(Age currentAge) {
        return Math.min(currentAge.ordinal(), maxLevel);
    }

    public FactionStructureTemplate getCurrentStructureTemplate(int currentLevel) {
        return levelStructures[currentLevel];
    }
}
