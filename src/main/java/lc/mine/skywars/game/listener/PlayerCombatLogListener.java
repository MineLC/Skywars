package lc.mine.skywars.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import lc.mine.combatlog.events.PlayerDeathCombatLog;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.top.TopManager;

public final class PlayerCombatLogListener implements Listener {
    
    private final TopManager topManager;
    
    public PlayerCombatLogListener(TopManager topManager) {
        this.topManager = topManager;
    }
    
    @EventHandler
    public void onPlayerCombatLog(final PlayerDeathCombatLog event) {
        final User victimData = SkywarsDatabase.getDatabase().getCached(event.getVictim().getUniqueId());
        victimData.deaths++;
        topManager.calculateDeaths(victimData, event.getVictim().getName());

        if (event.getKiller() != null) {
            final User killerData = SkywarsDatabase.getDatabase().getCached(event.getKiller().getUniqueId());
            killerData.kills++;
            topManager.calculateKills(killerData, event.getKiller().getName());
        }
    }
}
