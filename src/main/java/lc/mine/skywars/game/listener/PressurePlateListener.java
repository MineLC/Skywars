package lc.mine.skywars.game.listener;

import java.util.SplittableRandom;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import lc.mine.core.config.messages.Messages;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;

public final class PressurePlateListener implements Listener {

    private static final SplittableRandom RANDOM = new SplittableRandom();
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
        if (gameManager.cantJoin(player)) {
            return;
        }
        final SkywarsGame[] games = gameManager.getGames();      
        final SkywarsGame randomGame = games[RANDOM.nextInt(games.length-1)];
        if (!randomGame.hasStarted()) {
            gameManager.join(player, randomGame);
            return;
        }
        for (final SkywarsGame game : games) {
            if (!game.hasStarted()) {
                gameManager.join(player, game);
                return;
            }
        }
        Messages.send(player, "all-maps-are-used");
    }
}