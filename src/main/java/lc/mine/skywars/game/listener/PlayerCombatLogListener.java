package lc.mine.skywars.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import lc.mine.combatlog.events.PlayerCombatLogEvent;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.top.TopManager;

public final class PlayerCombatLogListener implements Listener {
    
    private final GameManager gameManager;
    private final TopManager topManager;
    
    public PlayerCombatLogListener(GameManager gameManager,TopManager topManager) {
        this.topManager = topManager;
        this.gameManager = gameManager;
    }
    
    @EventHandler
    public void onPlayerCombatLog(final PlayerCombatLogEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getVictim());
        if (game == null) {
            return;
        }

        final User victimData = SkywarsDatabase.getDatabase().getCached(event.getVictim().getUniqueId());
        victimData.deaths++;
        topManager.calculateDeaths(victimData, event.getVictim().getName());
        if (event.getKiller() != null) {
            final User killerData = SkywarsDatabase.getDatabase().getCached(event.getKiller().getUniqueId());
            killerData.kills++;
            topManager.calculateKills(killerData, event.getKiller().getName());
        }

        Messages.sendNoGet(game.getPlayers(), event.getDeathMessage());
        event.setCancelDeathMessage(true);
    }
}
