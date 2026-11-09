package io.github.atilist.hostilefactions.block.entity;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.strategy.FactionStructureBoard;
import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructure;
import io.github.atilist.hostilefactions.util.strategy.mission.StructureBuildingMissionTemplate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class HubBlockEntity extends BlockEntity {
    private final List<MissionTemplate> missions = new ArrayList<>();

    private int droneCount = 0;
    private int currentMission = 0;
    private boolean initialized = false;

    public HubBlockEntity() {
    }

    @Override
    public void tick() {
        if (droneCount == 0) {
            spawnDrone();
        }
        if (!initialized) {
            missions.add(new StructureBuildingMissionTemplate(
                    new FactionStructure(FactionStructureBoard.SMALL_HUB_FOUNDATION, x - 2, y - 1, z - 2)
            ));
            initialized = true;
        }
    }

    private void spawnDrone() {
        droneCount++;
        DroneEntity drone = new DroneEntity(world, x + 0.5, y + 3, z + 0.5);
        drone.setHubLocation(x, y, z);
        world.spawnEntity(drone);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        droneCount = nbt.getInt("DroneCount");
        currentMission = nbt.getInt("CurrentMission");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("DroneCount", droneCount);
        nbt.putInt("CurrentMission", currentMission);
    }

    public void reportDroneDeath() {
        droneCount--;
    }

    public MissionTemplate getCurrentMission() {
        if (currentMission >= missions.size()) {
            return null;
        }
        return missions.get(currentMission);
    }
}
