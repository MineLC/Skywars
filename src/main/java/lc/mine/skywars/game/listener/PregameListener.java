package lc.mine.skywars.game.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameState;

public class PregameListener implements Listener {

    private final GameManager gameManager;
    private final ConfigManager configManager;

    public PregameListener(ConfigManager configManager, GameManager gameManager) {
        this.configManager = configManager;;
        this.gameManager = gameManager;
    }

    @EventHandler
    public void interact(final PlayerInteractEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null || game.getState() != GameState.PREGAME) {
            return;
        }
        event.setCancelled(true);
        final Material type = event.getMaterial();
        switch (type) {
            case BOW:
                event.getPlayer().openInventory(configManager.getKitsConfig().getInventory());
                break;
            case CHEST:
                event.getPlayer().openInventory(configManager.getChestRefillConfig().getInventory());
                break;
            case BEACON:
                event.getPlayer().openInventory(configManager.getCageInventory());
                break;
            case BOOK:
                configManager.getTopConfig().getTopInventoryBuilder().buildMainInventory(event.getPlayer());
                break;
            default:
                break;   
        }
    }

    @EventHandler
    public void breakBlock(final BlockBreakEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null || game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(final PlayerDropItemEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null || game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void placeBlock(final BlockPlaceEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null || game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }
}
