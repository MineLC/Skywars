package lc.mine.skywars.game.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.Database;
import lc.mine.skywars.game.GameState;
import lc.mine.skywars.map.GameMap;
import lc.mine.skywars.map.MapSpawn;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class PlayerLeaveGameListener implements Listener {

    private final Database database;
    private final GameMap map;

    public PlayerLeaveGameListener(Database database, GameMap map) {
        this.database = database;
        this.map = map;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(final EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        onPlayerLeaveFromGame(player);
        final MapSpawn spawn = map.spawns()[0];
        SkywarsPlugin.getInstance().getServer().getScheduler().runTaskLater(SkywarsPlugin.getInstance(), () -> player.teleport(new Location(map.world(), spawn.x, spawn.y, spawn.z)), 2);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (map == null || map.spawns() == null || map.spawns().length == 0) {
            return;
        }

        if (GameState.currentState == GameState.PREGAME) {
            final UUID uuid = event.getPlayer().getUniqueId();
            final MapSpawn[] spawns = map.spawns();
            for (final MapSpawn spawn : spawns) {
                if (spawn.playerUsingIt != null && spawn.playerUsingIt.equals(uuid)) {
                    spawn.playerUsingIt = null;
                    break;
                }
            }
            database.save(event.getPlayer());
            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.SPECTATOR) {
            if (database.getCached(event.getPlayer().getUniqueId()) == null) { // Prevent fast login/quit
                return;
            }
            onPlayerLeaveFromGame(event.getPlayer());
            database.save(event.getPlayer());
        }
    }

    private void onPlayerLeaveFromGame(final Player player) {
        final UUID uuid = player.getUniqueId();
        database.getCached(uuid).deaths++;
        if (player.getKiller() != null) {
            database.getCached(player.getKiller().getUniqueId()).kills++;
        }
        database.save(player);

        final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
        int alivePlayers = 0;
        EntityPlayer lastPlayerLive = null;
        player.setGameMode(GameMode.SPECTATOR);

        for (final EntityPlayer otherPlayer : players) {
            if (otherPlayer.playerInteractManager.getGameMode() != EnumGamemode.SPECTATOR) {
                alivePlayers++;
                lastPlayerLive = otherPlayer;
                continue;
            }
        }

        GameState.currentState = GameState.FINISH;
        Messages.send(player, "death");

        if (alivePlayers != 1) {          
            return;
        }

        Bukkit.getScheduler().runTaskLater(SkywarsPlugin.getInstance(), () -> {
            database.saveAll();
            Bukkit.getServer().shutdown();
        }, 200);

        database.getCached(lastPlayerLive.getUniqueID()).wins++;
        lastPlayerLive.getBukkitEntity().sendTitle(Messages.get("win-title"), Messages.get("win-subtitle"));
        Bukkit.broadcastMessage(Messages.get("win").replace("%winner%", lastPlayerLive.getName()));
    } 
}