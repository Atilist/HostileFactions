package io.github.atilist.hostilefactions.events.init;

import io.github.atilist.hostilefactions.block.HubBlock;
import io.github.atilist.hostilefactions.block.LazyBlockWithEntityTemplate;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class BlockListener {

    public static LazyBlockWithEntityTemplate hub;

    @Entrypoint.Namespace
    public static Namespace NAMESPACE;

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        hub = new HubBlock(Identifier.of(NAMESPACE, "hub"), Material.METAL, 50, Block.METAL_SOUND_GROUP);
    }
}
