package lc.mine.skywars.sidebar;

import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.Sidebar;
import io.github.ichocomilk.lightsidebar.nms.v1_8R3.Sidebar1_8R3;
import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.utils.TimeUtil;

public final class GameSidebar {

    public void sendSidebar(final SkywarsGame game, final Set<PlayerInGame> players) {
        int amountPlayerLiving = 0;
        for (final PlayerInGame playerInGame : players) {
            final Player player = playerInGame.getPlayer();
            if (player.getGameMode() != GameMode.SPECTATOR) {
                amountPlayerLiving++;
            }
        }

        final Sidebar1_8R3 sidebar = new Sidebar1_8R3();
        final Object[] spectatorSidebar = getSpectatorSidebar(sidebar, game, amountPlayerLiving);

        sidebar.setTitle("§6§lSkywars");

        for (final PlayerInGame playerInGame : players) {
            if (playerInGame.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                sidebar.setLines(spectatorSidebar);
            } else {
                sidebar.setLines(getSurvivorSidebar(sidebar, game, playerInGame, amountPlayerLiving));
            }
            sidebar.sendLines(playerInGame.getPlayer());
            sidebar.sendTitle(playerInGame.getPlayer());
        }
    }

    private Object[] getSpectatorSidebar(final Sidebar sidebar, final SkywarsGame game, final int amountPlayerLiving) {
        return sidebar.createLines(new String[]{
            "",
            "§fRefill: §e" + TimeUtil.getMinutesAndSeconds(game.getNextChestRefill()),
            "",
            "§fJugadores: §a" + amountPlayerLiving,
            "",
            "§fMapa: " + game.getMap().getDisplayName() ,
            "",
            "§bplay.mine.lc"
        });
    }

    private Object[] getSurvivorSidebar(final Sidebar sidebar, final SkywarsGame game, final PlayerInGame playerInGame, final int amountPlayerLiving) {
        return sidebar.createLines(new String[]{
            "",
            "§fRefill: §e" + TimeUtil.getMinutesAndSeconds(game.getNextChestRefill()),
            "",
            "§fJugadores: §a" + amountPlayerLiving,
            "§fAsesinatos: §c" + playerInGame.getKills(),
            "",
            "§fMapa: " + game.getMap().getDisplayName(),
            "",
            "§bplay.mine.lc"
        });
    }
}
