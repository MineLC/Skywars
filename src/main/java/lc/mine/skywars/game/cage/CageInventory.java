package lc.mine.skywars.game.cage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.utils.ClickableInventory;

final class CageInventory implements ClickableInventory {
    
    private Inventory[] inventories;

    private final Material[] cageMaterials;
    private final int inventoryIndex;

    public CageInventory(int inventoryIndex, Material[] cageMaterials) {
        this.inventoryIndex = inventoryIndex;
        this.cageMaterials = cageMaterials;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();

        if (slot == 45) {
            if (inventoryIndex == 0) {
                return;
            }
            event.getWhoClicked().openInventory(inventories[inventoryIndex - 1]);
            return;
        }

        if (this.inventories != null && inventoryIndex+1 != inventories.length && slot == 53) {
            event.getWhoClicked().openInventory(inventories[inventoryIndex + 1]);
            return;
        }
        if (slot >= cageMaterials.length) {
            return;
        }
        final Location location = event.getWhoClicked().getLocation();
        GameCage.build(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), cageMaterials[slot]);
        SkywarsPlugin.getInstance().getManager().getDatabase().getCached(event.getWhoClicked().getUniqueId()).cageMaterial = cageMaterials[slot];
    }

    public void setInventories(Inventory[] inventories) {
        this.inventories = inventories;
    }
}
