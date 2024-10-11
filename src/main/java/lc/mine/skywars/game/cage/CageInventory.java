package lc.mine.skywars.game.cage;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.utils.ClickableInventory;

final class CageInventory implements ClickableInventory {
    
    private Inventory[] inventories;

    private final Cage[] cages;
    private final int inventoryIndex;

    public CageInventory(int inventoryIndex, Cage[] cages) {
        this.inventoryIndex = inventoryIndex;
        this.cages = cages;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        event.setCancelled(true);

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
        if (slot >= cages.length) {
            return;
        }
        final Cage cage = cages[slot];
        final Location location = event.getWhoClicked().getLocation();

        if (!event.getWhoClicked().hasPermission(cage.permission)) {
            event.getWhoClicked().sendMessage(cage.permissionMessage);
            return;
        }

        GameCage.build(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), cage.material);
        SkywarsDatabase.getDatabase().getCached(event.getWhoClicked().getUniqueId()).cageMaterial = cage.material;
    }

    public void setInventories(Inventory[] inventories) {
        this.inventories = inventories;
    }

    static final record Cage(String permission, Material material, String permissionMessage){}
}
