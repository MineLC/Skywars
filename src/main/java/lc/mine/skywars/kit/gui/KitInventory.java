package lc.mine.skywars.kit.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.kit.Kit;
import lc.mine.skywars.utils.ClickableInventory;

public final class KitInventory implements ClickableInventory {

    private final Kit[] kits;

    public KitInventory(Kit[] kits) {
        this.kits = kits;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        for (final Kit kit : kits) {
            if (kit.inventorySlot() == slot) {
                SkywarsPlugin.getInstance().getManager().getDatabase().getCached(event.getWhoClicked().getUniqueId()).selectedKit = kit;
                event.getWhoClicked().sendMessage(Messages.get("selected-kit").replace("%kit%", kit.name()));
                event.setCancelled(true);
                return;
            }
        }
    }
}