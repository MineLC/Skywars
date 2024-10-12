package lc.mine.skywars.game;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.core.config.messages.Messages;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.map.MapLoadSupply;
import lc.mine.skywars.map.SkywarsMap;

public class GameMapSupply implements MapLoadSupply {

    private final Player playerToTeleport;
    private final GameManager gameManager;
    private final SkywarsGame game;
    private final JavaPlugin plugin;

    public GameMapSupply(Player player, GameManager gameManager, SkywarsGame game, JavaPlugin plugin) {
        this.playerToTeleport = player;
        this.gameManager = gameManager;
        this.game = game;
        this.plugin = plugin;
    }

    @Override
    public void onLoad(World world, SkywarsMap map) {
        game.setState(GameState.PREGAME);
        game.setWorld(world);
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            gameManager.removePlayerTryingToJoinInGame(playerToTeleport.getUniqueId());
            gameManager.join(playerToTeleport, game);
        });
    }

    @Override
    public void onMapLoadError() {
        Messages.send(playerToTeleport, "map.cant-load");
    }
}
