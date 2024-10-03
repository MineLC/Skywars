package lc.mine.skywars.chestrefill.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import lc.mine.skywars.chestrefill.creator.ChestInventoryCreator;
import lc.mine.skywars.game.GameState;
import lc.mine.skywars.utils.ClickableInventory;
import lc.mine.skywars.utils.IntegerUtil;

public final class ChestInventoryListener implements Listener {

    private final LongOpenHashSet chestsInCooldown;

    public ChestInventoryListener(LongOpenHashSet chestsInCooldown) {
        this.chestsInCooldown = chestsInCooldown;
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (GameState.currentState != GameState.PREGAME || event.getClickedInventory() == null || !(event.getInventory().getHolder() instanceof ClickableInventory clickableInventory)) {
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
        final Location location = event.getClickedBlock().getLocation();
        final long key = IntegerUtil.compress((int)location.getX(), (int)location.getY(), (int)location.getZ(), false);

        if (chestsInCooldown.contains(key)) {
            return;
        }

        ChestInventoryCreator.setItems(((Chest)event.getClickedBlock().getState()).getBlockInventory());
        chestsInCooldown.add(key);
    }

    @EventHandler
    public void breaka(final BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.BEACON) {
            final Location a = event.getBlock().getLocation();
            System.out.println(a.getBlockX() + "," + a.getBlockY() + "," + a.getBlockZ());
        }
    }
}
