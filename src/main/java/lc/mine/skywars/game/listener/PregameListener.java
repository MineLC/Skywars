package lc.mine.skywars.game.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import lc.mine.skywars.config.Config;
import lc.mine.skywars.game.GameState;

public class PregameListener implements Listener {

    private final Config.ChestRefill chestRefill;
    private final Config.Kits kits;
    private final Inventory cageInventory;

    public PregameListener(Config.ChestRefill chestRefill, Config.Kits kits, Inventory cageInventory) {
        this.chestRefill = chestRefill;
        this.kits = kits;
        this.cageInventory = cageInventory;
    }

    @EventHandler
    public void interact(final PlayerInteractEvent event) {
        if (GameState.currentState != GameState.PREGAME) {
            return;
        }
        event.setCancelled(true);
        final Material type = event.getMaterial();
        if (type == Material.BOW) {
            event.getPlayer().openInventory(kits.inventory);
            return;
        }
        if (type == Material.CHEST) {
            event.getPlayer().openInventory(chestRefill.inventory);
            return;    
        }
        if (type == Material.BEACON) {
            event.getPlayer().openInventory(cageInventory);
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
