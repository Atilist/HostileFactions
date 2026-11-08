package io.github.atilist.hostilefactions.util;

import net.minecraft.nbt.NbtCompound;

public class TickClock {
    private final int tickRate;
    private final String name;

    private int currentTick;

    public TickClock(int tickRate, String name) {
        this.tickRate = tickRate;
        this.name = name;
    }

    public void tick() {
        if (currentTick < tickRate) {
            currentTick++;
        } else {
            currentTick = 0;
        }
    }

    public boolean inProgress() {
        return currentTick < tickRate;
    }

    public void readNbt(NbtCompound nbt) {
        currentTick = nbt.getInt("CurrentTick" + name);
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("CurrentTick" + name, currentTick);
    }
}
