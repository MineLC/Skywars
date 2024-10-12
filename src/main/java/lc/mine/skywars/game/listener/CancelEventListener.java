package lc.mine.skywars.game.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;

import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameState;

public class CancelEventListener implements Listener {

    private final GameManager gameManager;

    public CancelEventListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void breakBlock(final BlockBreakEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null || game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        } 
        final SkywarsGame game = gameManager.getGame(player);
        if (game == null || game.getState() != GameState.IN_GAME || player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            return;
        }
        if (event.getCause() == DamageCause.FALL) {
            event.setDamage(event.getDamage() / 2);
            return;
        }
        if (event.getCause() == DamageCause.VOID) {
            event.setDamage(player.getMaxHealth());
            return;
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
