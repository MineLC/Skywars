package lc.mine.skywars.utils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface ClickableInventory extends InventoryHolder {
    
    default void onClick(final InventoryClickEvent event) {
        event.setCancelled(true);
    }

    default Inventory getInventory() {
        return null;
    }
}