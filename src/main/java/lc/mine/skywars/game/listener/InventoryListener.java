package lc.mine.skywars.game.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.chestrefill.creator.ChestInventoryCreator;
import lc.mine.skywars.utils.ClickableInventory;
import lc.mine.skywars.utils.IntegerUtil;

public final class InventoryListener implements Listener {

    private final GameManager gameManager;

    public InventoryListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !(event.getInventory().getHolder() instanceof ClickableInventory clickableInventory)) {
            return;
        }
        clickableInventory.onClick(event);
    }

    @EventHandler
    public void onChestInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.CHEST) {
            return;
        }
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null) {
            return;
        }
        final LongOpenHashSet chestsInCooldown = game.getChestsInCooldown();
        final Location location = event.getClickedBlock().getLocation();
        final long key = IntegerUtil.compress((int)location.getX(), (int)location.getY(), (int)location.getZ(), false);

        if (chestsInCooldown.contains(key)) {
            return;
        }
        ChestInventoryCreator.setItems(((Chest)event.getClickedBlock().getState()).getBlockInventory(), game.getChestMode());
        chestsInCooldown.add(key);
    }
}
