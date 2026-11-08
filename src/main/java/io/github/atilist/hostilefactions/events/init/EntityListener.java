package io.github.atilist.hostilefactions.events.init;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.event.entity.EntityRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.EntityHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;

public class EntityListener {
    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public static void registerEntities(EntityRegisterEvent event) {
        event.register(NAMESPACE.id("Drone"), DroneEntity.class);
    }

    @EventListener
    public static void registerEntityHandlers(EntityHandlerRegistryEvent event) {
        event.register(NAMESPACE.id("Drone"), DroneEntity::new);
    }

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerEntityRenderer(EntityRendererRegisterEvent event) {
        event.renderers.put(DroneEntity.class, new PlayerEntityRenderer());
    }
}
