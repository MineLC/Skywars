package lc.mine.skywars.game.states.timer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.top.TopManager;

public final class GameTimer implements Runnable {

    private final GameManager gameManager;

    private final EndGameTimer endGameTimer;
    private final InGameTimer inGameTimer;
    private final PregameTimer pregameTimer;

    public GameTimer(final GameManager gameManager, final TopManager topManager, final ConfigManager configManager) {
        this.gameManager = gameManager;
        this.endGameTimer = new EndGameTimer(configManager.getGameStatesConfig().getEndgameConfig());
        this.inGameTimer = new InGameTimer(configManager.getGameStatesConfig().getInGameConfig(), gameManager, configManager.getSidebarConfig());
        this.pregameTimer = new PregameTimer(topManager, configManager.getGameStatesConfig().getPregameConfig(), configManager.getKitsConfig(), configManager.getChestRefillConfig(), configManager);
    }

    public void start(final JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20).getTaskId();
    }

    @Override
    public void run() {
        if (!gameManager.haveMaps()) {
            return;
        }
        final SkywarsGame[] games = gameManager.getGames();
        for (final SkywarsGame game : games) {
            switch (game.getState()) {
                case UNSTARTED:
                    continue;
                case PREGAME:
                    pregameTimer.tickGame(game);
                    continue;
                case IN_GAME:
                    inGameTimer.tickGame(game);
                    continue;
                case FINISH:
                    endGameTimer.tickGame(game);
                    continue;
            }
        }
    }
}