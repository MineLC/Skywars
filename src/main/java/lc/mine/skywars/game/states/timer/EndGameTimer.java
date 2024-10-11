package lc.mine.skywars.game.states.timer;

import java.util.Set;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameStatesConfig;

final class EndGameTimer {

    private final GameStatesConfig.EndGame config;

    EndGameTimer(GameStatesConfig.EndGame config) {
        this.config = config;
    }

    public void tickGame(final SkywarsGame game) {
        if (config.isSpawnFireworks()) {
            final Set<PlayerInGame> playerInGames = game.getPlayers();
            for (final PlayerInGame player : playerInGames) {
                final Firework firework = (Firework)player.getPlayer().getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.FIREWORK);
                final FireworkMeta fwm = firework.getFireworkMeta();

                fwm.setPower(2);
                fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
            
                firework.setFireworkMeta(fwm);
            }
        }
    }
}
