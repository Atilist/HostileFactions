package io.github.atilist.hostilefactions.util.strategy.task;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.Task;
import net.minecraft.item.ItemStack;

public class InventoryCraftingTask implements Task {
    private final ItemStack[] requiredItems;
    private final MissionTemplate[] backupMissions;
    private final ItemStack result;

    public InventoryCraftingTask(ItemStack[] requiredItems, MissionTemplate[] backupMissions, ItemStack result) {
        this.requiredItems = requiredItems;
        this.backupMissions = backupMissions;
        this.result = result;
    }

    @Override
    public void assignBackupMission(DroneEntity drone) {
        for (int i = 0; i < requiredItems.length; i++) {
            if (!drone.hasItems(requiredItems[i])) {
                drone.setMission(backupMissions[i]);
                return;
            }
        }
    }

    @Override
    public boolean requirementsMet(DroneEntity drone) {
        if (drone.hasItems(result)) {
            return true;
        }
        for (ItemStack requiredItem : requiredItems) {
            if (!drone.hasItems(requiredItem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFinished(DroneEntity drone) {
        return drone.hasItems(result);
    }

    @Override
    public void execute(DroneEntity drone) {
        for (ItemStack requiredItem : requiredItems) {
            drone.removeItems(requiredItem.copy());
        }
        drone.addItems(result.copy());
    }
}
