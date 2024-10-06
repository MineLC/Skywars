package lc.mine.skywars.game.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import lc.mine.skywars.game.GameState;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        } 
        if (GameState.currentState == GameState.FINISH || player.getGameMode() == GameMode.SPECTATOR) {
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
