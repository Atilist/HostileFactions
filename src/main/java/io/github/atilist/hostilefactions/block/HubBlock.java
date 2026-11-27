package io.github.atilist.hostilefactions.block;

import io.github.atilist.hostilefactions.block.entity.HubBlockEntity;
import io.github.atilist.hostilefactions.util.MessageBroadcast;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class HubBlock extends LazyBlockWithEntityTemplate {
    public HubBlock(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material, hardness, blockSounds);
        setResistance(10000);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new HubBlockEntity();
    }

    @Override
    public void afterBreak(World world, PlayerEntity playerEntity, int x, int y, int z, int meta) {
        super.afterBreak(world, playerEntity, x, y, z, meta);
        MessageBroadcast.sendMessage("NPC Faction Hub destroyed");
    }

    @Override
    public void onBlockBreakStart(World world, int x, int y, int z, PlayerEntity player) {
        super.onBlockBreakStart(world, x, y, z, player);
        BlockEntity blockEntity = world.getBlockEntity(x, y, z);
        if (blockEntity instanceof HubBlockEntity hubBlockEntity) {
            hubBlockEntity.setCommonEnemy(player);
        }
    }
}
