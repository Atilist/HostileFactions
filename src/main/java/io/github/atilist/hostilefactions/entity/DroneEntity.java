package io.github.atilist.hostilefactions.entity;

import io.github.atilist.hostilefactions.block.entity.HubBlockEntity;
import io.github.atilist.hostilefactions.events.init.EntityListener;
import io.github.atilist.hostilefactions.util.NameGenerator;
import io.github.atilist.hostilefactions.util.TickClock;
import io.github.atilist.hostilefactions.util.strategy.Mission;
import io.github.atilist.hostilefactions.util.strategy.MissionTemplate;
import io.github.atilist.hostilefactions.util.strategy.construction.FactionStructure;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.TriState;

@HasTrackingParameters(trackingDistance = 30, updatePeriod = 1, sendVelocity = TriState.TRUE)
public class DroneEntity extends PlayerEntity implements EntitySpawnDataProvider {
    private static final int SLOT_SWAP_DURATION = 10;
    private static final int HOTBAR_SCROLL_DURATION = 5;
    private static final int PATH_PATIENCE = 600;
    private static final ItemStack BUILDING_BLOCK = new ItemStack(Block.DIRT, 1);

    private final TickClock hubCheck = new TickClock(200, "HubCheck");
    private final TickClock decay = new TickClock(20, "Decay");
    private final TickClock hubRegeneration = new TickClock(400, "HubRegeneration");
    private final TickClock missionDelay = new TickClock(10, "MissionDelay");

    private int hubX, hubY, hubZ;
    private boolean assignedName = false;
    private boolean hubDeprived = false;
    private int actionCooldown;
    private Mission currentMission = null;
    public FactionStructure currentStructure = null;
    public Item neededItem = null;

    private Entity threat = null;
    private Path threatPath = null;
    private int pathTime;

    private Path locationPath = null;
    private int locationX, locationY, locationZ;
    private boolean hasLocationTarget = false;
    private boolean heightFlexible = false;

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
    protected void tickLiving() {
        super.tickLiving();
        threatPath = walkToEntity(threat, threatPath);
        locationPath = walkToLocation(locationPath);
        if (isSubmergedInWater()) {
            jumping = true;
        }
    }

    private Path walkToEntity(Entity target, Path path) {
        if (target == null || path == null) {
            return path;
        }
        int bodyCenter = MathHelper.floor(this.boundingBox.minY + (double)0.5F);
        if (this.random.nextInt(100) != 0) {
            Vec3d nodePosition = path.getNodePosition(this);
            double destinationAreaSize = this.width;

            while(nodePosition != null && nodePosition.squaredDistanceTo(this.x, nodePosition.y, this.z) < destinationAreaSize * destinationAreaSize) {
                path.next();
                if (path.isFinished()) {
                    nodePosition = null;
                    path = null;
                } else {
                    nodePosition = path.getNodePosition(this);
                }
            }

            this.jumping = false;
            if (nodePosition != null) {
                double xDifference = nodePosition.x - this.x;
                double zDifference = nodePosition.z - this.z;
                double yDifference = nodePosition.y - (double)bodyCenter;
                float var14 = (float)(Math.atan2(zDifference, xDifference) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
                float yawCorrection = var14 - this.yaw;

                this.forwardSpeed = movementSpeed;
                while (yawCorrection < -180) {
                    yawCorrection += 360;
                }

                while(yawCorrection >= 180.0F) {
                    yawCorrection -= 360.0F;
                }

                if (yawCorrection > 30.0F) {
                    yawCorrection = 30.0F;
                }

                if (yawCorrection < -30.0F) {
                    yawCorrection = -30.0F;
                }

                this.yaw += yawCorrection;
                if (isStuck()) {
                    this.sidewaysSpeed = 10;
                } else {
                    this.sidewaysSpeed = 0;
                }

                if (yDifference > (double)0) {
                    this.jumping = true;
                }
            }

            this.lookAt(target, 30.0F, 30.0F);

            if (this.horizontalCollision && path == null) {
                this.jumping = true;
            }

            if (this.random.nextFloat() < 0.8F && (isSubmergedInWater() || isTouchingLava())) {
                this.jumping = true;
            }

        }
        return path;
    }

    private Path walkToLocation(Path path) {
        if (path == null || !hasLocationTarget) {
            return null;
        }
        int bodyCenter = MathHelper.floor(this.boundingBox.minY + (double)0.5F);
        if (this.random.nextInt(100) != 0) {
            Vec3d nodePosition = path.getNodePosition(this);

            while(nodePosition != null && nodePosition.squaredDistanceTo(this.x, nodePosition.y, this.z) < 4) {
                path.next();
                if (path.isFinished()) {
                    nodePosition = null;
                    path = null;
                } else {
                    nodePosition = path.getNodePosition(this);
                }
            }

            this.jumping = false;
            if (nodePosition != null) {
                double xDifference = nodePosition.x - this.x;
                double zDifference = nodePosition.z - this.z;
                double yDifference = nodePosition.y - (double)bodyCenter;
                float var14 = (float)(Math.atan2(zDifference, xDifference) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
                float yawCorrection = var14 - this.yaw;

                this.forwardSpeed = movementSpeed;
                while (yawCorrection < -180) {
                    yawCorrection += 360;
                }

                while(yawCorrection >= 180.0F) {
                    yawCorrection -= 360.0F;
                }

                if (yawCorrection > 30.0F) {
                    yawCorrection = 30.0F;
                }

                if (yawCorrection < -30.0F) {
                    yawCorrection = -30.0F;
                }

                this.yaw += yawCorrection;
                if (isStuck()) {
                    jumping = true;
                    this.sidewaysSpeed = 10;
                    if (getBlockId(x, y + 2, z) == 0 && canReplaceBlock(x, y - 2, z) && hasPathCorrectionBlocks()) {
                        placePathCorrectionBlock(x, y - 2, z);
                    }
                } else {
                    this.sidewaysSpeed = 0;
                }

                if (yDifference > (double)0) {
                    this.jumping = true;
                }
            }

            if (this.horizontalCollision && path == null) {
                this.jumping = true;
            }

            if (this.random.nextFloat() < 0.8F && (isSubmergedInWater() || isTouchingLava())) {
                this.jumping = true;
            }

        }
        return path;
    }

    private boolean isStuck() {
        return Math.abs(velocityX) < 0.001 && Math.abs(velocityZ) < 0.001;
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

        // Mission execution
        planLocationPath();
        followMission();

        // Debug
        if (hasLocationTarget && locationPath != null) {
            PathNode end = locationPath.getEnd();
            if (end != null) {
                world.addParticle("explode", end.x, end.y, end.z, 0, 0, 0);
                world.addParticle("largesmoke", locationX, locationY, locationZ, 0, 0, 0);
            }
        }
    }

    private void followMission() {
        if (hasLocationTarget) {
            return;
        }
        missionDelay.tick();
        if (missionDelay.inProgress()) {
            return;
        }
        if (currentMission == null) {
            lookForMission();
            return;
        }
        if (currentMission.isComplete()) {
            currentMission = null;
            return;
        }
        currentMission.attemptTask();
    }

    private void lookForMission() {
        BlockEntity blockEntity = world.getBlockEntity(hubX, hubY, hubZ);
        if (blockEntity instanceof HubBlockEntity hubBlockEntity) {
            MissionTemplate availableMission = hubBlockEntity.getCurrentMission();
            if (availableMission != null) {
                setMission(availableMission);
            }
        }
    }

    private void planLocationPath() {
        if (!hasLocationTarget) {
            return;
        }
        if (pathTime > PATH_PATIENCE) {
            locationPath = null;
            pathTime = 0;
        }
        if (locationPath != null) {
            pathTime++;
            return;
        }
        double differenceX = locationX - x;
        double differenceY = locationY - y;
        double differenceZ = locationZ - z;
        double distanceX = Math.abs(differenceX);
        double distanceY = Math.abs(differenceY);
        double distanceZ = Math.abs(differenceZ);
        if (distanceX < 4 && (heightFlexible || distanceY < 4) && distanceZ < 4) {
            hasLocationTarget = false;
            return;
        }
        if (distanceX < 8 && distanceY < 8 && distanceZ < 8) {
            locationPath = world.findPath(this, locationX, locationY, locationZ, 32);
            return;
        }
        double targetX = locationX;
        double targetY = locationY;
        double targetZ = locationZ;
        if (differenceX > 0) {
            targetX = x + Math.min(distanceX, 8) + pathVariation();
        } else if (differenceX < 0) {
            targetX = x - Math.min(distanceX, 8) - pathVariation();
        }
        if (differenceY > 0) {
            targetY = y + Math.min(distanceY, 8);
        } else if (differenceY < 0) {
            targetY = y - Math.min(distanceY, 8);
        }
        if (differenceZ > 0) {
            targetZ = z + Math.min(distanceZ, 8) + pathVariation();
        } else if (differenceZ < 0) {
            targetZ = z - Math.min(distanceZ, 8) - pathVariation();
        }
        locationPath = world.findPath(this, (int) targetX, (int) targetY, (int) targetZ, 32);
    }

    private int pathVariation() {
        return random.nextInt(5);
    }

    private void prepareCounterattack() {
        if (actionCooldown > 0) {
            return;
        }
        if (threat == null) {
            return;
        }
        lookAt(threat, 30, 30);

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
        if (!threat.isAlive()) {
            threat = null;
            return;
        }
        double enemyDistance = threat.getSquaredDistance(this);
        if (enemyDistance < 4) {
            attack(threat);
            return;
        }
        if (enemyDistance > 400) {
            threat = null;
            return;
        }
        createThreatPath();
    }

    private void createThreatPath() {
        threatPath = world.findPath(this, threat, 32);
    }

    @Override
    public void attack(Entity target) {
        if (target == null) {
            threat = null;
            return;
        }
        swingHand();
        super.attack(target);
        if (!target.isAlive()) {
            threat = null;
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
        if (health < 20) {
            hubRegeneration.tick();
            if (hubRegeneration.inProgress()) {
                return;
            }
            if (isAtHub()) {
                health++;
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

        // Return to hub if health is low and no food available
        if (health <= 5) {
            returnToHub();
            threat = null;
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

    public void breakBlock(int blockX, int blockY, int blockZ) {
        int blockId = world.getBlockId(blockX, blockY, blockZ);
        if (blockId == 0) {
            return;
        }
        Block block = Block.BLOCKS[blockId];
        if (block == null) {
            return;
        }
        int blockMeta = world.getBlockMeta(blockX, blockY, blockZ);
        int droppedItemId = block.getDroppedItemId(blockMeta, random);
        int droppedItemCount = block.getDroppedItemCount(random);
        world.setBlock(blockX, blockY, blockZ, 0);
        if (droppedItemCount <= 0) {
            return;
        }
        if (droppedItemId <= 0) {
            return;
        }
        ItemStack droppedStack = new ItemStack(droppedItemId, droppedItemCount, 0);
        inventory.addStack(droppedStack);
    }

    public boolean hasItems(ItemStack requirement) {
        ItemStack[] mainInventory = this.inventory.main;
        int requiredCount = requirement.count;
        int available = 0;
        for (ItemStack currentItem : mainInventory) {
            if (currentItem == null) {
                continue;
            }
            if (!(currentItem.isItemEqual(requirement))) {
                continue;
            }
            available += currentItem.count;
            if (available >= requiredCount) {
                return true;
            }
        }
        return false;
    }

    public void removeItems(ItemStack itemsToRemove) {
        ItemStack[] mainInventory = this.inventory.main;
        int remainingCount = itemsToRemove.count;
        for (int i = 0; i < mainInventory.length; i++) {
            ItemStack currentItem = mainInventory[i];
            if (currentItem == null) {
                continue;
            }
            if (!(currentItem.isItemEqual(itemsToRemove))) {
                continue;
            }
            int currentCount = currentItem.count;
            if (currentCount <= remainingCount) {
                remainingCount -= currentCount;
                inventory.setStack(i, null);
            } else {
                currentItem.count = currentCount - remainingCount;
                inventory.setStack(i, currentItem);
                return;
            }
        }
    }

    public void addItems(ItemStack stack) {
        inventory.addStack(stack);
    }

    public void setLocationTarget(int x, int y, int z) {
        hasLocationTarget = true;
        locationX = x;
        locationY = y;
        locationZ = z;
        heightFlexible = false;
    }

    public void setLocationTarget(int x, int z) {
        hasLocationTarget = true;
        locationX = x;
        locationY = world.getTopY(x, z);
        locationZ = z;
        heightFlexible = true;
    }

    public void returnToHub() {
        setLocationTarget(hubX, hubY, hubZ);
    }

    public boolean isAtLocation(int targetX, int targetY, int targetZ) {
        double targetDistanceX = Math.abs(x - targetX);
        double targetDistanceY = Math.abs(y - targetY);
        double targetDistanceZ = Math.abs(z - targetZ);
        return targetDistanceX < 6 && targetDistanceY < 6 && targetDistanceZ < 6;
    }

    public boolean isAtHub() {
        return isAtLocation(hubX, hubY, hubZ);
    }

    public boolean isStationary() {
        return !hasLocationTarget && threat == null;
    }

    public boolean isMoving() {
        return hasLocationTarget;
    }

    public void setMission(MissionTemplate missionTemplate) {
        this.currentMission = new Mission(this, missionTemplate);
    }

    public void placeBlock(int blockX, int blockY, int blockZ, Block blockToPlace) {
        if (blockToPlace == null) {
            return;
        }
        world.setBlock(blockX, blockY, blockZ, blockToPlace.id);
        ItemStack stackToRemove = new ItemStack(blockToPlace, 1);
        removeItems(stackToRemove);
    }

    private int getBlockId(double blockX, double blockY, double blockZ) {
        world.addParticle("lava", Math.floor(blockX), blockY, Math.floor(blockZ), 0, 0, 0);
        return world.getBlockId((int) (Math.floor(blockX)), (int) blockY, (int) (Math.floor(blockZ)));
    }

    private boolean canReplaceBlock(double blockX, double blockY, double blockZ) {
        int id = getBlockId(blockX, blockY, blockZ);
        return id == 0 || id == Block.WATER.id || id == Block.FLOWING_WATER.id || id == Block.SNOW.id || id == Block.GRASS.id;
    }

    private boolean hasPathCorrectionBlocks() {
        return hasItems(BUILDING_BLOCK.copy());
    }

    private void placePathCorrectionBlock(double blockX, double blockY, double blockZ) {
        placeBlock((int) (Math.floor(blockX)), (int) blockY, (int) (Math.floor(blockZ)), Block.DIRT);
    }

    @Override
    public boolean damage(Entity damageSource, int amount) {
        threat = damageSource;
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
        hubRegeneration.readNbt(nbt);
        missionDelay.writeNbt(nbt);
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
        hubRegeneration.writeNbt(nbt);
        missionDelay.writeNbt(nbt);
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
