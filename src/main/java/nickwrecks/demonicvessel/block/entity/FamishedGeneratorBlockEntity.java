package nickwrecks.demonicvessel.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage.ENERGY_CAPABILITY;

public class FamishedGeneratorBlockEntity extends BlockEntity {
    private final Map<Direction, LazyOptional<IRawDemonicEnergyStorage>> energyCache = new EnumMap<>(Direction.class);

    public static final int FAMISHED_GEN_CAPACITY = 10000;
    public static final int FAMISHED_GEN_EXPERIENCE_CAPACITY = 1000;
    public static final int EXPERIENCE_EXTRACTION = 1;
    public boolean collecting = false;
    public boolean hasGem = false;
    public int experience = 0;
    public int time;
    public int hunger = 0;
    private int hungerTimer = 160;
    private int hungerSaturation = 0;

    public int gemBreakerTimer = 200;
    public static final int MAXIMUM_HUNGER = 20;
    private final RawDemonicEnergyStorage rawDemonicEnergyStorage = createEnergy();
    private final LazyOptional<IRawDemonicEnergyStorage> rawDemonicEnergy = LazyOptional.of(() -> rawDemonicEnergyStorage);
    private RawDemonicEnergyStorage createEnergy() {
        return new RawDemonicEnergyStorage(FAMISHED_GEN_CAPACITY, 100) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }

            @Override
            public boolean canReceive() {
                return false;
            }
        };
    }
    private static final int SLOT_COUNT = 1;

    private final ItemStackHandler items = createItemHandler();

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);
    public FamishedGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAMISHED_GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Nonnull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }
    public void tickClient() {
        time++;
    }
    private int delay = 3;
    public void tickServer() {
            distributeEnergy();
            if(collecting && experience < FAMISHED_GEN_EXPERIENCE_CAPACITY) {
                if(delay == 0) {
                    collectExp();
                    delay = 3;
                }
                else delay--;
            }
            else if(experience > FAMISHED_GEN_EXPERIENCE_CAPACITY ) experience = FAMISHED_GEN_EXPERIENCE_CAPACITY;
            if(experience >0 && rawDemonicEnergyStorage.getEnergyStored() < rawDemonicEnergyStorage.getMaxEnergyStored()) {
                experience--;
                rawDemonicEnergyStorage.addEnergy(30);
            }
            if(rawDemonicEnergyStorage.getEnergyStored() > rawDemonicEnergyStorage.getMaxEnergyStored())
                rawDemonicEnergyStorage.setEnergy(rawDemonicEnergyStorage.getMaxEnergyStored());
            ItemStack food = items.getStackInSlot(0);
            if(!food.isEmpty()) {
                if (food.isEdible() && hunger < MAXIMUM_HUNGER) {
                    FoodProperties foodProperties = food.getFoodProperties(null);
                    hunger = Math.min(hunger + foodProperties.getNutrition(), MAXIMUM_HUNGER);
                    hungerSaturation = (int) Math.min(hungerSaturation + foodProperties.getNutrition() * foodProperties.getSaturationModifier() * 2.0f, hunger);
                    gemBreakerTimer = 200;
                    items.extractItem(0,1,false);
                }
            }
            if(hungerTimer > 0) {
                hungerTimer --;
            }
            else {
                if(hungerSaturation > 0) hungerSaturation--;
                else if(hunger > 0) hunger --;
                else gemBreakerTimer--;
                if(hunger > 0) hungerTimer = 160;
            }
            if(gemBreakerTimer == 0 && hasGem) {
                collecting = false;
                hasGem  = false;
                this.getLevel().playSound(null,getBlockPos(),SoundEvents.ITEM_BREAK,SoundSource.BLOCKS,0.5f,1.0f);
                this.getLevel().sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),Block.UPDATE_ALL);
                ServerLevel serverLevel = (ServerLevel) this.getLevel();
                serverLevel.sendParticles(ParticleTypes.CRIT,getBlockPos().getX(),getBlockPos().getY()+0.5f,getBlockPos().getZ(),4,getLevel().random.nextFloat()*0.7f,0,getLevel().random.nextFloat()*0.7f,0);
            }
    }

    private void distributeEnergy() {
        Direction dir = Direction.DOWN;
        LazyOptional<IRawDemonicEnergyStorage> targetCapability = energyCache.get(dir);
        if (getLevel().getBlockEntity(getBlockPos().relative(dir)) != null) {
            if (targetCapability == null) {
                ICapabilityProvider provider = getLevel().getBlockEntity(getBlockPos().relative(dir));
                targetCapability = provider.getCapability(ENERGY_CAPABILITY, dir.getOpposite());
                energyCache.put(dir, targetCapability);
                targetCapability.addListener(self -> energyCache.put(dir, null));
            }
            targetCapability.ifPresent(storage -> {
                if (rawDemonicEnergyStorage.getEnergyStored() <= 0) return;
                if (storage.canReceive()) {
                    int received = storage.receiveEnergy(Math.min(rawDemonicEnergyStorage.getEnergyStored(), 100), false);
                    rawDemonicEnergyStorage.extractEnergy(received, false);
                    setChanged();
                }
            });

        }
    }
    private void collectExp() {

        List<Player> playerList = this.getLevel().getEntitiesOfClass(Player.class, AABB.ofSize(Vec3.atCenterOf(new BlockPos(getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ())),3f,3f,3f));
        if(!playerList.isEmpty()) {
            playerList.forEach((player) -> {
                if(player.totalExperience <= 0) return;
                else  {
                        player.giveExperiencePoints(-(EXPERIENCE_EXTRACTION*3));
                        experience = experience + EXPERIENCE_EXTRACTION*3;

                        this.getLevel().playSound(null,getBlockPos(),SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS,0.4f,this.getLevel().random.nextFloat());
                        setChanged();
                        Vec3 vec3 = getBlockPos().getCenter();
                        Vec3 vec31 = player.getEyePosition().subtract(vec3);
                        Vec3 vec32 = vec31.normalize();
                        ServerLevel serverLevel = (ServerLevel) getLevel();
                    for(int i = 1; i < Mth.floor(vec31.length()); ++i) {
                        Vec3 vec33 = vec3.add(vec32.scale((double)i));
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, vec33.x, vec33.y, vec33.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            });
        }
    }
    public ItemStackHandler getItems() {
        return items;
    }



    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        boolean oldHasGem = hasGem;
        boolean oldCollect = collecting;
        CompoundTag tag = pkt.getTag();
        handleUpdateTag(tag);

        if(oldHasGem != hasGem || oldCollect != collecting) {
            level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Energy", rawDemonicEnergyStorage.serializeNBT());
        pTag.putInt("Experience", experience);
        pTag.putInt("Hunger",hunger);
        pTag.putInt("HungerTimer",hungerTimer);
        pTag.putInt("HungerSaturation",hungerSaturation);
        pTag.putInt("GemBreakerTimer",gemBreakerTimer);
        pTag.putBoolean("hasGem",hasGem);
        pTag.putBoolean("collecting",collecting);
        pTag.put("Items",items.serializeNBT());

    }

    @Override
    public void load(CompoundTag pTag) {

        if (pTag.contains("Experience"))
            experience = pTag.getInt("Experience");
        if (pTag.contains("Energy"))
            rawDemonicEnergyStorage.deserializeNBT(pTag.get("Energy"));
        if(pTag.contains("Hunger"))
            hunger = pTag.getInt("Hunger");
        if(pTag.contains("HungerSaturation"))
            hungerSaturation = pTag.getInt("HungerSaturation");
        if(pTag.contains("HungerTimer"))
            hungerTimer = pTag.getInt("HungerTimer");
        if(pTag.contains("GemBreakerTimer"))
            gemBreakerTimer = pTag.getInt("GemBreakerTimer");
        if(pTag.contains("hasGem"))
            hasGem = pTag.getBoolean("hasGem");
        if(pTag.contains("collecting"))
            collecting = pTag.getBoolean("collecting");
        if(pTag.contains("Items"))
            items.deserializeNBT(pTag.getCompound("Items"));
    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putBoolean("hasGem",hasGem);
        tag.putBoolean("collecting",collecting);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if(tag !=null) {
            if(tag.contains("hasGem"))
                hasGem = tag.getBoolean("hasGem");
            if(tag.contains("collecting"))
                collecting = tag.getBoolean("collecting");
        }
    }

    @Override
    public void invalidateCaps() {
        itemHandler.invalidate();
        rawDemonicEnergy.invalidate();
        super.invalidateCaps();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== ForgeCapabilities.ITEM_HANDLER)
            return itemHandler.cast();
        if(cap == ENERGY_CAPABILITY)
            if(side == Direction.DOWN)
                return rawDemonicEnergy.cast();
         return super.getCapability(cap,side);
    }


    public int getStoredEnergy() {
        return rawDemonicEnergyStorage.getEnergyStored();
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
