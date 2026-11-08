package io.github.atilist.hostilefactions.entity;

import io.github.atilist.hostilefactions.block.entity.HubBlockEntity;
import io.github.atilist.hostilefactions.events.init.EntityListener;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

@HasTrackingParameters(trackingDistance = 30, updatePeriod = 1, sendVelocity = TriState.TRUE)
public class DroneEntity extends PlayerEntity implements EntitySpawnDataProvider {
    private int hubX, hubY, hubZ;

    public DroneEntity(World world, double x, double y, double z) {
        this(world);
        setPosition(x, y, z);
    }

    public DroneEntity(World world) {
        super(world);
        name = "Drone";
    }

    public void setHubLocation(int hubX, int hubY, int hubZ) {
        this.hubX = hubX;
        this.hubY = hubY;
        this.hubZ = hubZ;
    }

    @Override
    public void spawn() {
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return EntityListener.NAMESPACE.id("Drone");
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        hubX = nbt.getInt("HubX");
        hubY = nbt.getInt("HubY");
        hubZ = nbt.getInt("HubZ");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("HubX", hubX);
        nbt.putInt("HubY", hubY);
        nbt.putInt("HubZ", hubZ);
    }

    @Override
    public void beforeRemove() {
        super.beforeRemove();
        BlockEntity blockEntity = world.getBlockEntity(hubX, hubY, hubZ);
        if (blockEntity instanceof HubBlockEntity hubBlockEntity) {
            hubBlockEntity.reportDroneDeath();
        }
    }
}
