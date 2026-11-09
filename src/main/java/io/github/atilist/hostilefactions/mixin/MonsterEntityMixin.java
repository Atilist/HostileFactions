package io.github.atilist.hostilefactions.mixin;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.mob.MonsterEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MonsterEntity.class)
public abstract class MonsterEntityMixin extends MobEntity {
    public MonsterEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "getTargetInRange", at = @At("HEAD"), cancellable = true)
    public void locateDrone(CallbackInfoReturnable<Entity> cir) {
        List droneList = this.world.collectEntitiesByClass(DroneEntity.class, Box.createCached(this.x, this.y, this.z, this.x + 1, this.y + 1, this.z + 1).expand(16.0F, 4.0F, 16.0F));
        if (!droneList.isEmpty()) {
            cir.setReturnValue((Entity)droneList.get(this.world.random.nextInt(droneList.size())));
        }
    }
}
