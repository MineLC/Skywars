package lc.mine.skywars.game.chestrefill.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import lc.mine.skywars.utils.ClickableInventory;

public class ChestPreviewItemsGui implements ClickableInventory {

    private Inventory[] inventories;

    private final int backInventorySlot, nextInventorySlot;
    private final int inventoryIndex;
    private final Inventory mainInventory;

    public ChestPreviewItemsGui(int backInventorySlot, int nextInventorySlot, int inventoryIndex, Inventory mainInventory) {
        this.backInventorySlot = backInventorySlot;
        this.nextInventorySlot = nextInventorySlot;
        this.inventoryIndex = inventoryIndex;
        this.mainInventory = mainInventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        event.setCancelled(true);

        if (slot == backInventorySlot) {
            if (inventoryIndex == 0) {
                event.getWhoClicked().openInventory(mainInventory);
                return;
            }
            event.getWhoClicked().openInventory(inventories[inventoryIndex - 1]);
            return;
        }

        if (this.inventories != null && inventoryIndex+1 != inventories.length && slot == nextInventorySlot) {
            event.getWhoClicked().openInventory(inventories[inventoryIndex + 1]);
        }   
    }

    public void setInventories(Inventory[] inventories) {
        this.inventories = inventories;
    }
}