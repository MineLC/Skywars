package lc.mine.skywars.kit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.kit.Kit;
import lc.mine.skywars.kit.KitAdder;
import lc.mine.skywars.utils.ClickableInventory;

public final class KitInventory implements ClickableInventory {

    private final Kit[] kits;

    public KitInventory(Kit[] kits) {
        this.kits = kits;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        if (slot > kits.length) {
            return;
        }
        KitAdder.add((Player)event.getWhoClicked(), kits[slot]);
        event.setCancelled(true);
    }
}