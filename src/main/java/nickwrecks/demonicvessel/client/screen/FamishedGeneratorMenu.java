package nickwrecks.demonicvessel.client.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import nickwrecks.demonicvessel.block.ModBlocks;
import nickwrecks.demonicvessel.block.entity.FamishedGeneratorBlockEntity;

public class FamishedGeneratorMenu extends AbstractContainerMenu {

    private final BlockPos pos;
    private int power;
    private int experience;
    private int hunger;

    public FamishedGeneratorMenu(int pContainerId, Player pPlayer, BlockPos pos) {
        super(ModScreens.FAMISHED_GENERATOR_CONTAINER.get(), pContainerId);
        this.pos = pos;
        if(pPlayer.level.getBlockEntity(pos) instanceof FamishedGeneratorBlockEntity blockEntity) {
            addSlot(new SlotItemHandler(blockEntity.getItems(),SLOT,80,40));
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return blockEntity.getStoredEnergy() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    FamishedGeneratorMenu.this.power = (FamishedGeneratorMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (blockEntity.getStoredEnergy() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    FamishedGeneratorMenu.this.power = (FamishedGeneratorMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return blockEntity.experience & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    FamishedGeneratorMenu.this.experience = (FamishedGeneratorMenu.this.experience & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (blockEntity.experience >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    FamishedGeneratorMenu.this.experience = (FamishedGeneratorMenu.this.experience & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return blockEntity.hunger;
                }

                @Override
                public void set(int pValue) {
                    FamishedGeneratorMenu.this.hunger = pValue;
                }
            });
        }
        layoutPlayerInventorySlots(pPlayer.getInventory(),8,84);
    }

    public int getPower() {
        return power;
    }
    public BlockPos getPos() {return pos;}


    public int getExperience() {
        return experience;
    }

    public int getHunger() {
        return hunger;
    }

    private static final int SLOT_COUNT = 1;
    private static final int SLOT = 0;
    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot sourceSlot = this.slots.get(pIndex);
        if(sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyStack = sourceStack.copy();
        if(pIndex >= SLOT_COUNT) {
        if(!moveItemStackTo(sourceStack, SLOT, SLOT+1,false)) {
            return ItemStack.EMPTY;
            }
        }
        else if(pIndex < SLOT + SLOT_COUNT) {
            if(!moveItemStackTo(sourceStack,SLOT_COUNT+1, SLOT_COUNT+Inventory.INVENTORY_SIZE,false)) {
                return ItemStack.EMPTY;
            }
        }
        else  {
            System.out.println("Invalid slot index:" + pIndex);
            return ItemStack.EMPTY;
        }

        if(sourceStack.getCount() == 0)
            sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();

        sourceSlot.onTake(pPlayer,sourceStack);
        return copyStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.getLevel(), pos), pPlayer, ModBlocks.FAMISHED_GENERATOR_BLOCK.get());

    }
    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(Container playerInventory, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

}
