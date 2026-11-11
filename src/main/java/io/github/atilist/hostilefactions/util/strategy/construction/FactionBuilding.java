package io.github.atilist.hostilefactions.util.strategy.construction;

import io.github.atilist.hostilefactions.util.strategy.Age;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class FactionBuilding {
    private final FactionBuildingTemplate template;
    @Getter
    private final int x, y, z;

    private int currentLevel;
    private Age currentAge = Age.DARK;

    public FactionBuilding(FactionBuildingTemplate template, int x, int y, int z) {
        this.template = template;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public FactionStructure getCurrentStructure() {
        return new FactionStructure(template.getCurrentStructureTemplate(currentLevel), x, y, z);
    }

    public boolean isAtMaxLevel() {
        int currentMaxLevel = template.getMaxLevel(currentAge);
        return currentLevel >= currentMaxLevel;
    }

    public void setAge(Age newAge) {
        currentAge = newAge;
        currentLevel = template.getMaxLevel(currentAge);
    }

    public boolean isFinished(World world) {
        FactionStructureTemplate currentTemplate = template.getCurrentStructureTemplate(currentLevel);
        Block[][][] structureBlocks = currentTemplate.getBlocks();
        for (int xOffset = 0; xOffset < currentTemplate.getXLength(); xOffset++) {
            int combinedX = x + xOffset;
            for (int yOffset = 0; yOffset < currentTemplate.getYLength(); yOffset++) {
                int combinedY = y + yOffset;
                for (int zOffset = 0; zOffset < currentTemplate.getZLength(); zOffset++) {
                    Block desiredBlock = structureBlocks[xOffset][yOffset][zOffset];
                    if (desiredBlock == null) {
                        continue;
                    }
                    int combinedZ = z + zOffset;
                    int currentId = world.getBlockId(combinedX, combinedY, combinedZ);
                    if (desiredBlock == Block.LOCKED_CHEST) {
                        if (currentId != 0) {
                            return false;
                        }
                        continue;
                    }
                    if (desiredBlock.id != currentId) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
