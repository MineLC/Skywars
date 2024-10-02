package lc.mine.skywars.kit.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import lc.mine.skywars.kit.Kit;

public class KitInventoryCreator {

    public static Inventory create(final Kit[] kits) {
        final Inventory inventory = Bukkit.createInventory(new KitInventory(kits), 54, "Kits");
        int i = 0;
        for (final Kit kit : kits) {
            inventory.setItem(i++, kit.inventoryItem());
        }
        return inventory;
    }
}
