package lc.mine.skywars.game.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import lc.mine.core.config.messages.Messages;
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
        final Player player = event.getPlayer();
        if (!gameManager.canJoin(player)) {
            return;
        }
        final SkywarsGame[] games = gameManager.getGames();
        SkywarsGame gameWithMorePlayers = null;
        SkywarsGame emptyGame = null;
        int maxPlayersInGame = 0;

        for (final SkywarsGame game : games) {
            if (game.getState() == GameState.FINISH) {
                continue;
            }
            if (emptyGame != null && !game.hasStarted()) {
                emptyGame = game;
                continue;
            }
            final int playersInGame = game.getPlayers().size();
            if (playersInGame > maxPlayersInGame) {
                maxPlayersInGame = playersInGame;
                gameWithMorePlayers = game;
                continue;
            }
            break;
        }

        if (gameWithMorePlayers != null) {
            gameManager.join(player, gameWithMorePlayers);
            return;
        }
        if (emptyGame != null) {
            gameManager.join(player, emptyGame);
            return;
        }
        player.setVelocity(player.getVelocity().setY(3.0));
        Messages.send(player, "all-maps-are-used");
    }
}