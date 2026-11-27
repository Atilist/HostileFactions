package io.github.atilist.hostilefactions.mixin;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.FactionWorldProperties;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldProperties.class)
public class WorldPropertiesMixin implements FactionWorldProperties {
    private final List<Integer[]> factionLocations = new ArrayList<>();
    private final List<DroneEntity> drones = new ArrayList<>();
    private final List<Double[]> droneLocations = new ArrayList<>();

    @Override
    public void addFactionLocation(int x, int y, int z) {
        factionLocations.add(new Integer[] {x, y, z});
    }

    @Override
    public void addDrone(DroneEntity drone) {
        drones.add(drone);
    }

    @Override
    public List<Integer[]> getFactionLocations() {
        return factionLocations;
    }

    @Override
    public List<Double[]> getDroneLocations() {
        return droneLocations;
    }

    @Inject(method = "updateProperties", at = @At("HEAD"))
    private void updateFactionProperties(NbtCompound nbt, NbtCompound playerNbt, CallbackInfo ci) {
        System.out.println("Saving");
    }

    @Inject(method = "Lnet/minecraft/world/WorldProperties;<init>(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void loadFactionProperties(NbtCompound nbt, CallbackInfo ci) {
        System.out.println("Loading");
    }
}
