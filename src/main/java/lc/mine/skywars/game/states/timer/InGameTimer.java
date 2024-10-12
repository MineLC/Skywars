package lc.mine.skywars.game.states.timer;

import org.bukkit.WorldBorder;

import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameStatesConfig;
import lc.mine.skywars.sidebar.SidebarConfig;

final class InGameTimer {
    private final GameStatesConfig.InGame config;
    private final SidebarConfig sidebarConfig;
    private final GameManager gameManager;

    InGameTimer(GameStatesConfig.InGame config, GameManager gameManager, SidebarConfig sidebarConfig) {
        this.config = config;
        this.gameManager = gameManager;
        this.sidebarConfig = sidebarConfig;
    }

    public void tickGame(final SkywarsGame game) {
        game.addTick();

        tickChestRefill(game);
        sidebarConfig.getGameSidebar().sendSidebar(game, game.getPlayers());
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

}
