package lc.mine.skywars.game.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import lc.mine.skywars.config.Config;
import lc.mine.skywars.game.GameState;
import lc.mine.skywars.kit.gui.KitInventoryCreator;

public class PregameListener implements Listener {

    private final Config.ChestRefill chestRefill;
    private final Config.Kits kits;

    public PregameListener(Config.ChestRefill chestRefill, Config.Kits kits) {
        this.chestRefill = chestRefill;
        this.kits = kits;
    }

    @EventHandler
    public void interact(final PlayerInteractEvent event) {
        if (GameState.currentState != GameState.PREGAME) {
            return;
        }
        final Material type = event.getMaterial();
        if (type == Material.BOW) {
            event.getPlayer().openInventory(KitInventoryCreator.create(kits.arrayKits));
            return;
        }
        if (type == Material.CHEST) {
            event.getPlayer().openInventory(chestRefill.inventory);
            return;    
        }
    }

    @EventHandler
    public void breakBlock(final BlockBreakEvent event) {
        if (GameState.currentState == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(final PlayerDropItemEvent event) {
        if (GameState.currentState == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void placeBlock(final BlockPlaceEvent event) {
        if (GameState.currentState == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }
}
