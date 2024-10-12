package lc.mine.skywars.game.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameState;

public final class PressurePlateListener implements Listener {

    private final GameManager gameManager;

    public PressurePlateListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void pressurePlate(final PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.IRON_PLATE) {
            return;
        }
        final SkywarsGame[] games = gameManager.getGames();
        SkywarsGame randomStartedGame = null;
        for (final SkywarsGame game : games) {
            if (game.hasStarted() && game.getState() != GameState.FINISH) {
                randomStartedGame = game;
                continue;
            }
            gameManager.join(event.getPlayer(), game);
            break;
        }
        if (randomStartedGame != null) {
            gameManager.join(event.getPlayer(), randomStartedGame);       
        }
    }
}