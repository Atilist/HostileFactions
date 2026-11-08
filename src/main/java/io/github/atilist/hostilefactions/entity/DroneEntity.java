package io.github.atilist.hostilefactions.entity;

import io.github.atilist.hostilefactions.block.entity.HubBlockEntity;
import io.github.atilist.hostilefactions.events.init.EntityListener;
import io.github.atilist.hostilefactions.util.NameGenerator;
import io.github.atilist.hostilefactions.util.TickClock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

@HasTrackingParameters(trackingDistance = 30, updatePeriod = 1, sendVelocity = TriState.TRUE)
public class DroneEntity extends PlayerEntity implements EntitySpawnDataProvider {
    private static final int SLOT_SWAP_DURATION = 10;
    private static final int HOTBAR_SCROLL_DURATION = 5;

    private final TickClock hubCheck = new TickClock(200, "HubCheck");
    private final TickClock decay = new TickClock(20, "Decay");

    private int hubX, hubY, hubZ;
    private boolean assignedName = false;
    private boolean hubDeprived = false;
    private int actionCooldown;

    private Entity enemy = null;

    public DroneEntity(World world, double x, double y, double z) {
        this(world);
        setPosition(x, y, z);
    }

    public DroneEntity(World world) {
        super(world);
        if (!assignedName) {
            name = NameGenerator.generateDroneName();
            assignedName = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        checkExistence();

        // Simulate inventory management delays
        if (actionCooldown > 0) {
            actionCooldown--;
            return;
        }

        // Survival instincts
        respondToHealthStatus();
        prepareCounterattack();
    }

    private void prepareCounterattack() {
        if (actionCooldown > 0) {
            return;
        }
        if (enemy == null) {
            return;
        }

        // Check hand for sword
        ItemStack hand = getHand();
        if (hand != null) {
            Item item = hand.getItem();
            if (item instanceof SwordItem) {
                counterattack();
                return;
            }
        }

        // Check hotbar for sword
        for (int slot = 0; slot < 9; slot++) {
            ItemStack hotbarStack = this.inventory.getStack(slot);
            if (hotbarStack == null) {
                continue;
            }
            Item hotbarItem = hotbarStack.getItem();
            if (hotbarItem instanceof SwordItem) {
                actionCooldown = HOTBAR_SCROLL_DURATION;
                inventory.selectedSlot = slot;
                return;
            }
        }

        // Check inventory for sword
        for (int slot = 9; slot < this.inventory.main.length; slot++) {
            ItemStack inventoryStack = this.inventory.getStack(slot);
            if (inventoryStack == null) {
                continue;
            }
            Item inventoryItem = inventoryStack.getItem();
            if (inventoryItem instanceof SwordItem) {
                setHand(inventoryStack);
                this.inventory.setStack(slot, hand);
                actionCooldown = SLOT_SWAP_DURATION;
                return;
            }
        }

        // Counterattack without weapon
        counterattack();
    }

    private void counterattack() {
        if (!enemy.isAlive()) {
            enemy = null;
            return;
        }
        double enemyDistance = enemy.getSquaredDistance(this);
        if (enemyDistance < 2) {
            attack(enemy);
            return;
        }
        if (enemyDistance > 400) {
            enemy = null;
        }
    }

    @Override
    public void attack(Entity target) {
        if (target == null) {
            enemy = null;
            return;
        }
        super.attack(target);
        if (!target.isAlive()) {
            enemy = null;
        }
    }

    private void checkExistence() {
        hubCheck.tick();
        if (!hubCheck.inProgress()) {
            checkForHub();
        }
        if (hubDeprived) {
            decay.tick();
            if (!decay.inProgress()) {
                damage(1);
            }
        }
    }

    private void respondToHealthStatus() {
        if (health == 20) {
            return;
        }

        // Check hand for food and eat if valid
        ItemStack hand = getHand();
        if (hand != null) {
            Item item = hand.getItem();
            if (item instanceof FoodItem foodItem) {
                hand = foodItem.use(hand, world, this);
                if (hand != null && hand.count <= 0) {
                    hand = null;
                }
                setHand(hand);
                return;
            }
        }

        // Check hotbar for food
        for (int slot = 0; slot < 9; slot++) {
            ItemStack hotbarStack = this.inventory.getStack(slot);
            if (hotbarStack == null) {
                continue;
            }
            Item hotbarItem = hotbarStack.getItem();
            if (hotbarItem instanceof FoodItem) {
                inventory.selectedSlot = slot;
                actionCooldown = HOTBAR_SCROLL_DURATION;
                return;
            }
        }

        // Check inventory for food
        for (int slot = 9; slot < this.inventory.main.length; slot++) {
            ItemStack inventoryStack = this.inventory.getStack(slot);
            if (inventoryStack == null) {
                continue;
            }
            Item inventoryItem = inventoryStack.getItem();
            if (inventoryItem instanceof FoodItem) {
                setHand(inventoryStack);
                this.inventory.setStack(slot, hand);
                actionCooldown = SLOT_SWAP_DURATION;
                return;
            }
        }
    }

    private void setHand(ItemStack hand) {
        this.inventory.setStack(inventory.selectedSlot, hand);
    }

    public void setHubLocation(int hubX, int hubY, int hubZ) {
        this.hubX = hubX;
        this.hubY = hubY;
        this.hubZ = hubZ;
    }

    private void checkForHub() {
        BlockEntity blockEntity = world.getBlockEntity(hubX, hubY, hubZ);
        if (!(blockEntity instanceof HubBlockEntity)) {
            hubDeprived = true;
        }
    }

    @Override
    public boolean damage(Entity damageSource, int amount) {
        enemy = damageSource;
        return super.damage(damageSource, amount);
    }

    @Override
    public void spawn() {
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return EntityListener.NAMESPACE.id("Drone");
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        hubX = nbt.getInt("HubX");
        hubY = nbt.getInt("HubY");
        hubZ = nbt.getInt("HubZ");
        assignedName = nbt.getBoolean("AssignedName");
        if (assignedName) {
            name = nbt.getString("Name");
        }
        hubCheck.readNbt(nbt);
        decay.readNbt(nbt);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("HubX", hubX);
        nbt.putInt("HubY", hubY);
        nbt.putInt("HubZ", hubZ);
        nbt.putBoolean("AssignedName", assignedName);
        if (assignedName) {
            nbt.putString("Name", name);
        }
        hubCheck.writeNbt(nbt);
        decay.writeNbt(nbt);
    }

    @Override
    public void beforeRemove() {
        super.beforeRemove();
        BlockEntity blockEntity = world.getBlockEntity(hubX, hubY, hubZ);
        if (blockEntity instanceof HubBlockEntity hubBlockEntity) {
            hubBlockEntity.reportDroneDeath();
        }
    }

    @Override
    public void updateCapeUrl() {
        // Does not use capes, do nothing here
    }
}
