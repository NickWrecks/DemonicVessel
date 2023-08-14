package nickwrecks.demonicvessel.client.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import nickwrecks.demonicvessel.block.ModBlocks;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;


public class BatteryMenu extends AbstractContainerMenu {


    private final BlockPos pos;
    private int power;


    public BatteryMenu(int windowId,Player player, BlockPos pos) {
        super(ModScreens.BATTERY_CONTAINER.get(), windowId);
        this.pos = pos;
        if(player.level.getBlockEntity(pos) instanceof BatteryBlockEntity blockEntity) {
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return blockEntity.getStoredEnergy() & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    BatteryMenu.this.power = (BatteryMenu.this.power & 0xffff0000) | (pValue & 0xffff);
                }
            });
            addDataSlot(new DataSlot() {
                @Override
                public int get() {
                    return (blockEntity.getStoredEnergy() >> 16) & 0xffff;
                }

                @Override
                public void set(int pValue) {
                    BatteryMenu.this.power = (BatteryMenu.this.power & 0xffff) | ((pValue & 0xffff) << 16);
                }
            });
            layoutPlayerInventorySlots(player.getInventory(), 8, 84);
        }
    }
    public int getPower() {
        return power;
    }
    public BlockPos getPos() {return pos;}
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

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

                if (index < 27) {
                    if (!this.moveItemStackTo(stack, 27, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE  && !this.moveItemStackTo(stack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }


            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(pPlayer.getLevel(), pos), pPlayer, ModBlocks.BATTERY_BLOCK.get());
    }
}
