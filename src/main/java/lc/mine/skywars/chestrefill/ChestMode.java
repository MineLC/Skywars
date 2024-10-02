package lc.mine.skywars.chestrefill;

import org.bukkit.inventory.Inventory;

public class ChestMode {
    private final ChestItem[] items;
    private final int itemsPerChest;

    private final int inventorySlot;

    private final Inventory previewItemsInventory;
    private final String name;

    public ChestMode(ChestItem[] items, int itemsPerChest, int inventorySlot, Inventory previewItemsInventory, String name) {
        this.items = items;
        this.itemsPerChest = itemsPerChest;
        this.inventorySlot = inventorySlot;
        this.previewItemsInventory = previewItemsInventory;
        this.name = name;
    }

    public ChestItem[] getItems() {
        return items;
    }

    public int getItemsPerChest() {
        return itemsPerChest;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public Inventory getPreviewItemsInventory() {
        return previewItemsInventory;
    }

    public String getName() {
        return name;
    }
}
