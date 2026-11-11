package io.github.atilist.hostilefactions.util.strategy;

import io.github.atilist.hostilefactions.util.strategy.construction.FactionBuildingTemplate;
import net.minecraft.item.ItemStack;

public record AgeRequirement(FactionBuildingTemplate[] requiredBuildings, ItemStack[] requiredMaterials) {
}
