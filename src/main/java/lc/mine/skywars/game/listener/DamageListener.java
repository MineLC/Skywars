package lc.mine.skywars.game.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameState;

public class DamageListener implements Listener {

    private final GameManager gameManager;

    public DamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        } 
        final SkywarsGame game = gameManager.getGame(player);
        if (game == null) {
            return;
        }
        if (game.getState() == GameState.FINISH || player.getGameMode() == GameMode.SPECTATOR) {
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
}
