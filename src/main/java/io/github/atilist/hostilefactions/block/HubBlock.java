package io.github.atilist.hostilefactions.block;

import io.github.atilist.hostilefactions.block.entity.HubBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.sound.BlockSoundGroup;
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
}
