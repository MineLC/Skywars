package lc.mine.skywars.chestrefill.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import lc.mine.skywars.chestrefill.creator.ChestInventoryCreator;
import lc.mine.skywars.utils.ClickableInventory;

public final class ChestInventoryListener implements Listener {

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !(event.getClickedInventory().getHolder() instanceof ClickableInventory clickableInventory)) {
            return;
        }
        clickableInventory.onClick(event);
        event.setCancelled(true);
    }

    @EventHandler
    public void onChestInteract(final PlayerInteractEvent event) {
        if (ChestInventoryCreator.currentMode == null || event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.CHEST) {
            return;
        }
        event.getPlayer().openInventory(ChestInventoryCreator.createInventory());
        event.setUseInteractedBlock(Result.DENY);
    }
}
