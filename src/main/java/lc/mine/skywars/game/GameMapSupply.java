package lc.mine.skywars.game;

import java.lang.ref.WeakReference;

import org.bukkit.World;
import org.bukkit.entity.Player;

import lc.mine.core.config.messages.Messages;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.map.MapLoadSupply;
import lc.mine.skywars.map.SkywarsMap;

public class GameMapSupply implements MapLoadSupply {

    private final WeakReference<Player> playerToTeleport;
    private final GameManager gameManager;
    private final SkywarsGame game;
    private final SkywarsPlugin plugin;

    public GameMapSupply(WeakReference<Player> player, GameManager gameManager, SkywarsGame game, SkywarsPlugin plugin) {
        this.playerToTeleport = player;
        this.gameManager = gameManager;
        this.game = game;
        this.plugin = plugin;
    }

    @Override
    public void onLoad(World world, SkywarsMap map) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            final Player player = playerToTeleport.get();
            if (player == null || !player.isOnline()) {
                plugin.getMapManager().unloadMap(map, world);
                return;
            }
            game.setState(GameState.PREGAME);
            game.setWorld(world);

            gameManager.removePlayerTryingToJoinInGame(player.getUniqueId());
            gameManager.join(player, game);
        });
    }

    @Override
    public void onMapLoadError() {
        final Player player = playerToTeleport.get();
        if (player != null) {
            Messages.send(player, "map.cant-load");
        }
    }
}
