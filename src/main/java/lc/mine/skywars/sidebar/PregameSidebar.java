package lc.mine.skywars.sidebar;

import java.util.Set;

import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.nms.v1_8R3.Sidebar1_8R3;
import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.map.SkywarsMap;

public class PregameSidebar {
    
    public void sendPreGameSidebar(final SkywarsGame game) {
        final SkywarsMap map = game.getMap();
        final Sidebar1_8R3 sidebar = new Sidebar1_8R3();
        final Set<PlayerInGame> players = game.getPlayers();
        final int amountPlayers = players.size();

        sidebar.setTitle("§6§lSkywars");
        sidebar.setLines(sidebar.createLines(new String[]{
            "",
            "§fJugadores: §a" + amountPlayers + '/' + map.getSpawns().length,
            "",
            "§fMapa: " + map.getDisplayName() ,
            "",
            "§bplay.mine.lc"
        }));

        for (final PlayerInGame player : players) {
            final Player bukkitPlayer = player.getPlayer();
            sidebar.sendLines(bukkitPlayer);
            sidebar.sendTitle(bukkitPlayer);
        }
    }
}
