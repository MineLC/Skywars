package lc.mine.skywars.game.states.timer;

import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.nms.v1_8R3.Sidebar1_8R3;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameStatesConfig;
import lc.mine.skywars.utils.TimeUtil;

final class InGameTimer {
    private final GameStatesConfig.InGame config;

    InGameTimer(GameStatesConfig.InGame config) {
        this.config = config;
    }

    public void tickGame(final SkywarsGame game) {
        game.setNextChestRefill(game.getNextChestRefill()-1);
        final Set<PlayerInGame> players = game.getPlayers();

        if (game.getNextChestRefill() <= 0) {
            game.setNextChestRefill(config.getChestRefillTime());
            game.getChestsInCooldown().clear();

            if (config.isChestRefillMessage()) {
                Messages.sendNoGet(players, Messages.get("chest-refill"));
            }
            if (config.getChestRefillSound() != null) {
                for (final PlayerInGame playerInGame : players) {
                    config.getChestRefillSound().send(playerInGame.getPlayer());
                }
            }
        }

        final String refill = TimeUtil.getMinutesAndSeconds(game.getNextChestRefill());

        final Sidebar1_8R3 sidebar = new Sidebar1_8R3();
        sidebar.setTitle("§6§lSkywars");

        int amountPlayerLiving = 0;
        for (final PlayerInGame playerInGame : players) {
            final Player player = playerInGame.getPlayer();
            if (player.getGameMode() != GameMode.SPECTATOR) {
                amountPlayerLiving++;
                sidebar.setLines(sidebar.createLines(new String[]{
                    "",
                    "§fRefill: §e" + refill,
                    "",
                    "§fJugadores: §a" + amountPlayerLiving,
                    "",
                    "§fMapa: " + game.getMap().displayName() ,
                    "",
                    "§bplay.mine.lc"
                }));
            } else {
                sidebar.setLines(sidebar.createLines(new String[]{
                    "",
                    "§fRefill: §e" + refill,
                    "",
                    "§fAsesinatos: §c" + SkywarsDatabase.getDatabase().getCached(player.getUniqueId()).kills,
                    "§fJugadores: §a" + amountPlayerLiving,
                    "",
                    "§fMapa: " + game.getMap().displayName(),
                    "",
                    "§bplay.mine.lc"
                }));
            }

            sidebar.sendLines(player);
            sidebar.sendTitle(player);
        }
    }
}
