package io.github.atilist.hostilefactions.mixin;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(World.class)
public abstract class DroneUnlistingMixin {

    @Shadow
    public abstract Chunk getChunk(int chunkX, int chunkZ);

    @Shadow
    public List entities;

    @Shadow
    protected abstract void notifyEntityAdded(Entity entity);

    @Shadow
    protected abstract boolean hasChunk(int x, int z);

    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void preventDronePlayerListing(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof DroneEntity) {
            int chunkX = MathHelper.floor(entity.x / (double) 16.0F);
            int chunkZ = MathHelper.floor(entity.z / (double) 16.0F);
            if (!hasChunk(chunkX, chunkZ)) {
                cir.setReturnValue(false);
                return;
            }
            getChunk(chunkX, chunkZ).addEntity(entity);
            entities.add(entity);
            notifyEntityAdded(entity);
            cir.setReturnValue(true);
        }
    }
}
