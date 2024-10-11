package lc.mine.skywars.game;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.game.states.GameStatesConfig;
import lc.mine.skywars.map.MapSpawn;
import lc.mine.skywars.map.SkywarsMap;

public final class GameManager {

    private final Map<UUID, PlayerInGame> playersInGame = new Object2ObjectOpenHashMap<>();
    private final GameStatesConfig gameStatesConfig;
    private final SkywarsPlugin plugin;

    private SkywarsGame[] games;
    private boolean haveMaps;

    public GameManager(GameStatesConfig gameStatesConfig, SkywarsPlugin plugin) {
        this.gameStatesConfig = gameStatesConfig;
        this.plugin = plugin;
    }

    public void join(final Player player, final SkywarsGame game) {
        if (game.getState() == GameState.UNSTARTED) {
            game.setState(GameState.PREGAME);
        }
        final PlayerInGame playerInGame = new PlayerInGame(game, player);
        game.getPlayers().add(playerInGame);
        playersInGame.put(player.getUniqueId(), playerInGame);
    }

    public void quit(final Player player) {
        final PlayerInGame playerInGame = playersInGame.remove(player.getUniqueId());
        if (playerInGame == null) {
            return;
        }
        final SkywarsGame game = playerInGame.getGame();
        game.getPlayers().remove(playerInGame);

        if (game.getState() == GameState.FINISH) {
            return;
        }
        if (game.getState() == GameState.IN_GAME) {
            tryFindWinner(game);
            return;
        }
        if (game.getState() == GameState.PREGAME) {
            removeCage(game.getMap(), player.getUniqueId());

            if (game.getPlayers().size() < gameStatesConfig.getPregameConfig().getMinPlayers()) {
                game.setNextChestRefill(gameStatesConfig.getInGameConfig().getChestRefillTime());
                game.setPregameCountdown(gameStatesConfig.getPregameConfig().getCountdownTime());
            }
            if (game.getPlayers().isEmpty()) {
                game.setState(GameState.UNSTARTED);
            }    
        }   
    }

    public SkywarsGame getGame(final Player player) {
        final PlayerInGame playerInGame = playersInGame.get(player.getUniqueId());
        return (playerInGame == null) ? null : playerInGame.getGame();
    }

    public void setGames(final SkywarsMap... maps) {
        if (maps == null) {
            haveMaps = false;
            games = new SkywarsGame[0];
            return;
        }
        games = new SkywarsGame[maps.length];
        for (int i = 0; i < maps.length; i++) {
            final SkywarsGame game = new SkywarsGame(maps[i]);
            game.setNextChestRefill(gameStatesConfig.getInGameConfig().getChestRefillTime());
            game.setPregameCountdown(gameStatesConfig.getPregameConfig().getCountdownTime());
            games[i] = game;
        }
        haveMaps = true;
    }

    @SuppressWarnings("deprecation")
    public void tryFindWinner(final SkywarsGame game) {
        final Set<PlayerInGame> players = game.getPlayers();
        int alivePlayers = 0;
        Player lastPlayerLive = null;

        for (final PlayerInGame otherPlayer : players) {
            if (otherPlayer.isAlive()) {
                if (alivePlayers != 0) {
                    return;
                }
                alivePlayers++;
                lastPlayerLive = otherPlayer.getPlayer();
                continue;
            }
        }

        game.setState(GameState.FINISH);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            SkywarsDatabase.getDatabase().saveAll();
            plugin.getServer().shutdown();
        }, 200);

        final User winnerData = SkywarsDatabase.getDatabase().getCached(lastPlayerLive.getUniqueId());
        winnerData.wins++;

        lastPlayerLive.sendTitle(Messages.get("win-title"), Messages.get("win-subtitle"));
        Messages.sendNoGet(game.getPlayers(), Messages.get("win").replace("%winner%", lastPlayerLive.getName()));
    }

    private void removeCage(final SkywarsMap map, final UUID uuid) {
        final MapSpawn[] spawns = map.spawns();
        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt != null && spawn.playerUsingIt.equals(uuid)) {
                spawn.playerUsingIt = null;
                break;
            }
        }
        return;
    }

    public boolean haveMaps() {
        return haveMaps;
    }
    public SkywarsGame[] getGames() {
        return games;
    }
}