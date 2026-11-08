package io.github.atilist.hostilefactions.util.mission.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.mission.Task;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockGatheringTask implements Task {
    private final ItemStack itemsToGather;
    private final Block[] validBlocks;

    public BlockGatheringTask(ItemStack itemsToGather, Block[] validBlocks) {
        this.itemsToGather = itemsToGather;
        this.validBlocks = validBlocks;
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
        return drone.hasItems(itemsToGather);
    }

    @Override
    public void execute(DroneEntity drone) {
        World world = drone.world;
        int x = (int) drone.x;
        int y = (int) drone.y;
        int z = (int) drone.z;

        // Check surrounding blocks
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int yOffset = -2; yOffset <= 3; yOffset++) {
                for (int zOffset = -2; zOffset <= 2; zOffset++) {
                    if (isValidBlockId(world, x + xOffset, y + yOffset, z + zOffset)) {
                        drone.breakBlock(x + xOffset, y + yOffset, z + zOffset);
                        return;
                    }
                }
            }
        }
    }

    private boolean isValidBlockId(World world, int x, int y, int z) {
        int id = world.getBlockId(x, y, z);
        for (Block validBlock : validBlocks) {
            if (id == validBlock.id) {
                return true;
            }
        }
        return false;
    }
}
