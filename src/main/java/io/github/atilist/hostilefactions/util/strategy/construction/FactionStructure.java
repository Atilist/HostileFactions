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
    private final List<BlockPosition> blockRemovalPositions = new ArrayList<>();
    @Getter
    private final List<BlockPosition> foundationGaps = new ArrayList<>();

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
                        blockRemovalPositions.add(new BlockPosition(combinedX, combinedY, combinedZ));
                        intact = false;
                    } else if (desiredBlock.id != currentId) {
                        if (currentId != 0) {
                            blockRemovalPositions.add(new BlockPosition(combinedX, combinedY, combinedZ));
                            continue;
                        }
                        intact = false;
                        if (requiredBlocksList.isEmpty()) {
                            createNewRequirementEntry(desiredBlock, combinedX, combinedY, combinedZ);
                        } else {
                            boolean needsEntry = true;
                            RequiredBlocks requiredBlocks = null;
                            int requirementIndex = 0;
                            for (; requirementIndex < requiredBlocksList.size(); requirementIndex++) {
                                requiredBlocks = requiredBlocksList.get(requirementIndex);
                                if (requiredBlocks.getBlock() == desiredBlock) {
                                    requiredBlocks.requiredAmount++;
                                    requiredBlocks.positions.add(new BlockPosition(combinedX, combinedY, combinedZ));
                                    needsEntry = false;
                                }
                            }
                            if (needsEntry) {
                                createNewRequirementEntry(desiredBlock, combinedX, combinedY, combinedZ);
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

    public boolean hasFoundation(DroneEntity drone) {
        World world = drone.world;
        boolean intact = true;
        for (int xOffset = 0; xOffset < template.getXLength(); xOffset++) {
            int combinedX = x + xOffset;
            for (int zOffset = 0; zOffset < template.getZLength(); zOffset++) {
                int combinedZ = z + zOffset;
                int blockId = world.getBlockId(combinedX, y - 1, combinedZ);
                if (blockId == 0) {
                    intact = false;
                    foundationGaps.add(new BlockPosition(combinedX, y - 1, combinedZ));
                }
            }
        }
        return intact;
    }

    private void createNewRequirementEntry(Block desiredBlock, int blockX, int blockY, int blockZ) {
        RequiredBlocks requiredBlocks = new RequiredBlocks(desiredBlock);
        requiredBlocks.requiredAmount = 1;
        requiredBlocks.positions.add(new BlockPosition(blockX, blockY, blockZ));
        requiredBlocksList.add(requiredBlocks);
    }
}
