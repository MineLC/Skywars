package lc.mine.skywars.utils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface ClickableInventory extends InventoryHolder {
    
    void onClick(final InventoryClickEvent event);

    default Inventory getInventory() {
        return null;
    }
}