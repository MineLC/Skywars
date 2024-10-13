package lc.mine.skywars.game;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.cage.GameCage;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.game.states.GameStatesConfig;
import lc.mine.skywars.game.top.TopManager;
import lc.mine.skywars.map.MapSpawn;
import lc.mine.skywars.map.MapState;
import lc.mine.skywars.map.SkywarsMap;

public final class GameManager {

    private final Map<UUID, PlayerInGame> playersInGame = new Object2ObjectOpenHashMap<>();

    private final Set<UUID> playersTryingToJoinInGame = new ObjectOpenHashSet<>();

    private final GameStatesConfig gameStatesConfig;
    private final TopManager topManager;

    private final SkywarsPlugin plugin;

    private SkywarsGame[] games;
    private boolean haveMaps;

    public GameManager(GameStatesConfig gameStatesConfig, TopManager topManager, SkywarsPlugin plugin) {
        this.gameStatesConfig = gameStatesConfig;
        this.plugin = plugin;
        this.topManager = topManager;
    }

    public void join(final Player player, final SkywarsGame game) {
        final UUID uuid = player.getUniqueId();
        if (playersTryingToJoinInGame.contains(uuid) || !plugin.getMapManager().canJoin(game.getMap(), player)) {
            return;
        }
        if (game.getState() == GameState.UNSTARTED) {
            plugin.getMapManager().loadMap(game.getMap(), new GameMapSupply(player, this, game, plugin));
            playersTryingToJoinInGame.add(uuid);
            return;
        }
        if (game.getState() == GameState.FINISH) {
            Messages.send(player, "game-finish");
            return;
        }

        playersTryingToJoinInGame.remove(uuid);
        final PlayerInGame playerInGame = new PlayerInGame(game, player);
        game.getPlayers().add(playerInGame);
        playersInGame.put(uuid, playerInGame);

        player.setAllowFlight(false);
        plugin.getConfigManager().getSpawnConfig().sendPregameItems(player);

        if (game.getState() == GameState.PREGAME) {
            final MapSpawn[] spawns = game.getMap().getSpawns();
            for (final MapSpawn spawn : spawns) {
                if (spawn.playerUsingIt != null) {
                    continue;
                }
                spawn.playerUsingIt = uuid;
                player.teleport(new Location(game.getWorld(), spawn.x+0.5, spawn.y, spawn.z+0.5));
                GameCage.build(game.getWorld(), spawn.x, spawn.y, spawn.z, SkywarsDatabase.getDatabase().getCached(uuid).cageMaterial);
                break;
            }
            plugin.getConfigManager().getSidebarConfig().getPregameSidebar().sendPreGameSidebar(game);
        }
    }

    public void quit(final Player player, final boolean leaveFromServer) {
        User user = SkywarsDatabase.getDatabase().getCached(player.getUniqueId());
        user.activeChallenges.clear();
        if (!leaveFromServer) {
            plugin.getConfigManager().getSpawnConfig().sendToSpawn(player, plugin.getConfigManager().getSidebarConfig().getSpawnSidebar());
        }

        playersTryingToJoinInGame.remove(player.getUniqueId());
        final PlayerInGame playerInGame = playersInGame.remove(player.getUniqueId());
        if (playerInGame == null) {
            return;
        }
        final SkywarsGame game = playerInGame.getGame();
        game.getPlayers().remove(playerInGame);
        
        if (game.getPlayers().isEmpty()) {
            resetGame(game);
            return;
        }

        if (game.getState() == GameState.FINISH) {
            return;
        }
        if (game.getState() == GameState.IN_GAME) {
            tryFindWinner(game);
            return;
        }
        if (game.getState() == GameState.PREGAME) {
            removeCage(game.getMap(), game.getWorld(), player.getUniqueId());

            if (game.getPlayers().size() < gameStatesConfig.getPregameConfig().getMinPlayers()) {
                game.setNextChestRefill(gameStatesConfig.getInGameConfig().getChestRefillTime());
                game.setPregameCountdown(gameStatesConfig.getPregameConfig().getCountdownTime());
            }
        }
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
            }
        }

        game.setState(GameState.FINISH);
        endGame(game);

        final User winnerData = SkywarsDatabase.getDatabase().getCached(lastPlayerLive.getUniqueId());
        winnerData.wins++;
        winnerData.activeChallenges.clear();
        topManager.calculateWins(winnerData, lastPlayerLive.getName());

        lastPlayerLive.sendTitle(Messages.get("win-title"), Messages.get("win-subtitle"));
        Messages.sendNoGet(game.getPlayers(), Messages.get("win").replace("%winner%", lastPlayerLive.getName()));
    }

    public void endGame(final SkywarsGame game) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (game.getState() == GameState.UNSTARTED) {
                return;
            }
            for (final PlayerInGame playerInGame : game.getPlayers()) {
                playersInGame.remove(playerInGame.getPlayer().getUniqueId());
                plugin.getConfigManager().getSpawnConfig().sendToSpawn(playerInGame.getPlayer(), plugin.getConfigManager().getSidebarConfig().getSpawnSidebar());
            }
            resetGame(game);
        }, 200);
    }

    private void removeCage(final SkywarsMap map, final World world, final UUID uuid) {
        final MapSpawn[] spawns = map.getSpawns();
        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt != null && spawn.playerUsingIt.equals(uuid)) {
                spawn.playerUsingIt = null;
                GameCage.delete(world, spawn.x, spawn.y, spawn.z);
                break;
            }
        }
        return;
    }

    public void setGames(final SkywarsMap... maps) {
        if (this.games != null) {
            for (final SkywarsGame game : games) {
                for (final PlayerInGame playerInGame : game.getPlayers()) {
                    playersInGame.remove(playerInGame.getPlayer().getUniqueId());
                    plugin.getConfigManager().getSpawnConfig().sendToSpawn(playerInGame.getPlayer(), plugin.getConfigManager().getSidebarConfig().getSpawnSidebar());
                }
                resetGame(game);
            }
            games = null;
        }
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

    public void resetGame(final SkywarsGame game) {
        game.setNextChestRefill(gameStatesConfig.getInGameConfig().getChestRefillTime());
        game.setPregameCountdown(gameStatesConfig.getPregameConfig().getCountdownTime());
        game.resetTicks();
        game.setChestMode(null);
        game.setState(GameState.UNSTARTED);
        game.getChestsInCooldown().clear();
        game.getPlayers().clear();
        game.getMap().resetSpawns();

        if (game.getWorld() != null) {
            game.getMap().setMapState(MapState.UNLOADING);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getMapManager().unloadMap(game.getMap(), game.getWorld()), 1);
        }
    }

    void removePlayerTryingToJoinInGame(final UUID uuid) {
        playersTryingToJoinInGame.remove(uuid);
    }

    public SkywarsGame getGame(final Player player) {
        final PlayerInGame playerInGame = playersInGame.get(player.getUniqueId());
        return (playerInGame == null) ? null : playerInGame.getGame();
    }
    public boolean haveMaps() {
        return haveMaps;
    }
    public SkywarsGame[] getGames() {
        return games;
    }
}