package io.github.atilist.hostilefactions.util.strategy;

import io.github.atilist.hostilefactions.util.strategy.construction.FactionBuildingTemplate;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum Age {
    DARK("Dark Age", new FactionBuildingTemplate[]{FactionBuildingTemplateBoard.HUB}, 16, new AgeRequirement(new FactionBuildingTemplate[]{FactionBuildingTemplateBoard.HUB}, new ItemStack[]{new ItemStack(Block.DIRT, 32), new ItemStack(Block.LOG, 8)})),
    WOOD("Wood Age", new FactionBuildingTemplate[]{FactionBuildingTemplateBoard.HUB}, 32, new AgeRequirement(new FactionBuildingTemplate[]{FactionBuildingTemplateBoard.HUB}, new ItemStack[]{new ItemStack(Block.DIRT, 48), new ItemStack(Block.LOG, 16)})),
    STONE("Stone Age", new FactionBuildingTemplate[]{FactionBuildingTemplateBoard.HUB}, 64, new AgeRequirement(new FactionBuildingTemplate[]{FactionBuildingTemplateBoard.HUB}, new ItemStack[]{new ItemStack(Block.DIRT, 64), new ItemStack(Block.LOG, 24)}));

    @Getter
    final FactionBuildingTemplate[] availableBuildings;
    @Getter
    final int populationLimit;
    @Getter
    final AgeRequirement ageRequirement;
    @Getter
    final String ageName;

    Age(String ageName, FactionBuildingTemplate[] availableBuildings, int populationLimit, AgeRequirement ageRequirement) {
        this.ageName = ageName;
        this.availableBuildings = availableBuildings;
        this.populationLimit = populationLimit;
        this.ageRequirement = ageRequirement;
    }
}
