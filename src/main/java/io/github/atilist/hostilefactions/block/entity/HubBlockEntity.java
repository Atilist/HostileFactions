package io.github.atilist.hostilefactions.block.entity;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

public class HubBlockEntity extends BlockEntity {
    private int droneCount = 0;

    @Override
    public void tick() {
        if (droneCount == 0) {
            spawnDrone();
        }
    }

    private void spawnDrone() {
        droneCount++;
        DroneEntity drone = new DroneEntity(world, x + 0.5, y + 3, z + 0.5);
        drone.setHubLocation(x, y, z);
        world.spawnEntity(drone);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        droneCount = nbt.getInt("DroneCount");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("DroneCount", droneCount);
    }

    public void reportDroneDeath() {
        droneCount--;
    }
}
