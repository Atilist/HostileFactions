package io.github.atilist.hostilefactions.block.entity;

import io.github.atilist.hostilefactions.entity.DroneEntity;
import io.github.atilist.hostilefactions.util.TickClock;
import io.github.atilist.hostilefactions.util.strategy.Age;
import io.github.atilist.hostilefactions.util.strategy.AgeFinder;
import io.github.atilist.hostilefactions.util.strategy.FactionBuildingTemplateBoard;
import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionBuilding;
import io.github.atilist.hostilefactions.util.strategy.mission.StructureBuildingMissionTemplate;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class HubBlockEntity extends BlockEntity {
    private final List<MissionTemplate> missions = new ArrayList<>();
    private final TickClock ageCheck = new TickClock(100, "AgeCheck");

    private int droneCount = 0;
    private int currentMission = 0;
    private boolean initialized = false;
    private int currentAgeOrdinal;
    private Age currentAge = Age.DARK;
    private FactionBuilding hub = null;

    public HubBlockEntity() {
    }

    @Override
    public void tick() {
        if (droneCount == 0) {
            spawnDrone();
        }
        if (!initialized) {
            hub = new FactionBuilding(FactionBuildingTemplateBoard.HUB, x - 3, y - 1, z - 3);
            updateMissions();
            initialized = true;
        }
        attemptAgeUp();
    }

    private void updateMissions() {
        missions.clear();
        hub.setAge(currentAge);
        missions.add(new StructureBuildingMissionTemplate(hub.getCurrentStructure()));
    }

    private void attemptAgeUp() {
        ageCheck.tick();
        if (ageCheck.inProgress()) {
            return;
        }
        if (hasAgeCriteriaMet()) {
            System.out.println("Aged up");
            currentAge = AgeFinder.getNextAge(currentAge);
            updateMissions();
        }
    }

    private boolean hasAgeCriteriaMet() {
        return hub.isAtMaxLevel() && hub.isFinished(world);
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
        currentAgeOrdinal = nbt.getInt("CurrentAge");
        currentAge = AgeFinder.getAge(currentAgeOrdinal);
        ageCheck.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("DroneCount", droneCount);
        nbt.putInt("CurrentMission", currentMission);
        nbt.putInt("CurrentAge", currentAgeOrdinal);
        ageCheck.writeNbt(nbt);
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
