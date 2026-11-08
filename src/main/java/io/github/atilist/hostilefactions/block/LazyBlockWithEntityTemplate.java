package io.github.atilist.hostilefactions.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class LazyBlockWithEntityTemplate extends TemplateBlockWithEntity {

    protected int topTexture;
    protected int sideTexture;
    protected int bottomTexture;
    protected int frontTexture;
    protected int frontTextureActive;

    public LazyBlockWithEntityTemplate(Identifier identifier, Material material, float hardness, BlockSoundGroup blockSounds) {
        super(identifier, material);
        setTranslationKey(identifier.namespace, identifier.path);
        setHardness(hardness);
        setSoundGroup(blockSounds);
    }

    @Override
    public LazyBlockWithEntityTemplate setHardness(float hardness) {
        return (LazyBlockWithEntityTemplate) super.setHardness(hardness);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return null;
    }

    public void specifyTextures(int topTexture, int sideTexture, int bottomTexture, int frontTexture, int frontTextureActive) {
        this.topTexture = topTexture;
        this.sideTexture = sideTexture;
        this.bottomTexture = bottomTexture;
        this.frontTexture = frontTexture;
        this.frontTextureActive = frontTextureActive;
    }

    public void specifyTextures(int topTexture, int sideTexture, int bottomTexture, int frontTexture) {
        this.topTexture = topTexture;
        this.sideTexture = sideTexture;
        this.bottomTexture = bottomTexture;
        this.frontTexture = frontTexture;
    }

    public void specifyTextures(int topTexture, int sideTexture, int bottomTexture) {
        this.topTexture = topTexture;
        this.sideTexture = sideTexture;
        this.bottomTexture = bottomTexture;
        frontTexture = sideTexture;
    }

    public void specifyTextures(int universalTexture) {
        topTexture = universalTexture;
        sideTexture = universalTexture;
        bottomTexture = universalTexture;
        frontTexture = universalTexture;
    }

    @Override
    public int getTexture(int side, int meta) {
        if (meta == 0 && side == 3) {
            return frontTexture;
        }
        if (side == 0) return bottomTexture;
        if (side == 1) return topTexture;
        if (side == meta % 6 && meta > 1) {
            if (meta > 5) {
                return frontTextureActive;
            }
            return frontTexture;
        }
        return sideTexture;
    }
}
