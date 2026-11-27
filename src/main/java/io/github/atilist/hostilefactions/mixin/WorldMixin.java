package io.github.atilist.hostilefactions.mixin;

import io.github.atilist.hostilefactions.util.FactionWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow
    protected WorldProperties properties;

    @Shadow
    public abstract Chunk getChunkFromPos(int x, int z);

    private boolean loadedFactions = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void loadFactionsContent(CallbackInfo ci) {
        if (loadedFactions) {
            return;
        }
        loadedFactions = true;
        if (properties instanceof FactionWorldProperties factionWorldProperties) {
            List<Integer[]> factionLocations = factionWorldProperties.getFactionLocations();
            for (Integer[] factionLocation : factionLocations) {
                getChunkFromPos(factionLocation[0], factionLocation[1]);
            }
            List<Integer[]> droneLocations = factionWorldProperties.getDroneLocations();
            for (Integer[] droneLocation : droneLocations) {
                getChunkFromPos(droneLocation[0], droneLocation[1]);
            }
        }
    }
}
