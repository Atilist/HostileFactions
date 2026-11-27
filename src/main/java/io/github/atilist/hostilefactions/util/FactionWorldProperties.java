package io.github.atilist.hostilefactions.util;

import io.github.atilist.hostilefactions.entity.DroneEntity;

import java.util.List;

public interface FactionWorldProperties {
    void addFactionLocation(int x, int y, int z);

    void addDrone(DroneEntity drone);

    List<Integer[]> getFactionLocations();

    List<Double[]> getDroneLocations();
}
