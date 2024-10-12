package lc.mine.skywars.game.states.timer;

import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.Sidebar;
import io.github.ichocomilk.lightsidebar.nms.v1_8R3.Sidebar1_8R3;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameStatesConfig;
import lc.mine.skywars.utils.TimeUtil;

final class InGameTimer {
    private final GameStatesConfig.InGame config;
    private final GameManager gameManager;

    InGameTimer(GameStatesConfig.InGame config, GameManager gameManager) {
        this.config = config;
        this.gameManager = gameManager;
    }

    public void tickGame(final SkywarsGame game) {
        game.addTick();

        tickChestRefill(game);
        sendSidebar(game, game.getPlayers());
        tickWorldBorder(game);

        if (game.getTicks() >= config.getMaxGameDuration()) {
            gameManager.endGame(game);
        }
    }

    private void tickWorldBorder(final SkywarsGame game) {
        if (game.getTicks() == config.getWorldborderStart()) {
            Messages.sendNoGet(game.getPlayers(), "worldborder");
            return;
        }
        if (game.getTicks() > config.getWorldborderStart()) {
            final WorldBorder worldBorder = game.getWorld().getWorldBorder();
            if (worldBorder.getSize() <= config.getWorldborderLimit()) {
                return;
            }
            worldBorder.setSize(worldBorder.getSize() - config.getWorldborderReduce());
        }
    }

    private void tickChestRefill(final SkywarsGame game) {
        game.setNextChestRefill(game.getNextChestRefill()-1);
        if (game.getNextChestRefill() > 0) {
            return;
        }
        game.setNextChestRefill(config.getChestRefillTime());
        game.getChestsInCooldown().clear();

        if (config.isChestRefillMessage()) {
            Messages.sendNoGet(game.getPlayers(), Messages.get("chest-refill"));
        }
        if (config.getChestRefillSound() != null) {
            for (final PlayerInGame playerInGame : game.getPlayers()) {
                config.getChestRefillSound().send(playerInGame.getPlayer());
            }
        }
    }

    private void sendSidebar(final SkywarsGame game, final Set<PlayerInGame> players) {
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
                sidebar.setLines(getSurvivorSidebar(sidebar, game, playerInGame.getPlayer(), amountPlayerLiving));
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

    private Object[] getSurvivorSidebar(final Sidebar sidebar, final SkywarsGame game, final Player player, final int amountPlayerLiving) {
        return sidebar.createLines(new String[]{
            "",
            "§fRefill: §e" + TimeUtil.getMinutesAndSeconds(game.getNextChestRefill()),
            "",
            "§fAsesinatos: §c" + SkywarsDatabase.getDatabase().getCached(player.getUniqueId()).kills,
            "§fJugadores: §a" + amountPlayerLiving,
            "",
            "§fMapa: " + game.getMap().getDisplayName(),
            "",
            "§bplay.mine.lc"
        });
    }
}
