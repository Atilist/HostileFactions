package io.github.atilist.hostilefactions.util.strategy.construction;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FactionStructure {
    private final FactionStructureTemplate template;
    @Getter
    private final int x, y, z;
    @Getter
    private final List<RequiredBlocks> requiredBlocksList = new ArrayList<>();
    @Getter
    private final List<BlockRemovalPosition> blockRemovalPositions = new ArrayList<>();

    public FactionStructure(FactionStructureTemplate template, int x, int y, int z) {
        this.template = template;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean checkStructure(DroneEntity drone) {
        requiredBlocksList.clear();
        World world = drone.world;
        Block[][][] reference = template.getBlocks();
        boolean intact = true;
        for (int xOffset = 0; xOffset < template.getXLength(); xOffset++) {
            int combinedX = x + xOffset;
            for (int yOffset = 0; yOffset < template.getYLength(); yOffset++) {
                int combinedY = y + yOffset;
                for (int zOffset = 0; zOffset < template.getZLength(); zOffset++) {
                    int combinedZ = z + zOffset;
                    Block desiredBlock = reference[xOffset][yOffset][zOffset];
                    if (desiredBlock == null) {
                        continue;
                    }
                    int currentId = world.getBlockId(combinedX, combinedY, combinedZ);
                    if (desiredBlock == Block.LOCKED_CHEST && currentId == 0) {
                        continue;
                    }
                    if (desiredBlock == Block.LOCKED_CHEST) {
                        blockRemovalPositions.add(new BlockRemovalPosition(combinedX, combinedY, combinedZ));
                        intact = false;
                    } else if (desiredBlock.id != currentId) {
                        if (currentId != 0) {
                            blockRemovalPositions.add(new BlockRemovalPosition(combinedX, combinedY, combinedZ));
                            continue;
                        }
                        intact = false;
                        if (requiredBlocksList.isEmpty()) {
                            createNewRequirementEntry(desiredBlock);
                        } else {
                            boolean needsEntry = true;
                            RequiredBlocks requiredBlocks = null;
                            int requirementIndex = 0;
                            for (; requirementIndex < requiredBlocksList.size(); requirementIndex++) {
                                requiredBlocks = requiredBlocksList.get(requirementIndex);
                                if (requiredBlocks.getBlock() == desiredBlock) {
                                    requiredBlocks.requiredAmount++;
                                    needsEntry = false;
                                }
                            }
                            if (needsEntry) {
                                createNewRequirementEntry(desiredBlock);
                            } else {
                                requiredBlocksList.set(requirementIndex - 1, requiredBlocks);
                            }
                        }
                    }
                }
            }
        }
        return intact;
    }

    private void createNewRequirementEntry(Block desiredBlock) {
        RequiredBlocks requiredBlocks = new RequiredBlocks(desiredBlock);
        requiredBlocks.requiredAmount = 1;
        requiredBlocksList.add(requiredBlocks);
    }
}
