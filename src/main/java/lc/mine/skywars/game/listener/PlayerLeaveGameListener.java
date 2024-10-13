package lc.mine.skywars.game.listener;

import java.util.List;

import lc.mine.skywars.database.User;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.map.MapSpawn;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerLeaveGameListener implements Listener {

    private final JavaPlugin plugin;
    private final GameManager gameManager;

    public PlayerLeaveGameListener(GameManager gameManager, JavaPlugin plugin) {
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(final EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        final User user = SkywarsDatabase.getDatabase().getCached(player.getUniqueId());
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        final SkywarsGame game = gameManager.getGame(player);
        if (game == null) {
            return;
        }
        player.setGameMode(GameMode.SPECTATOR);
        Messages.send(player, "death");
        final MapSpawn spawn = game.getMap().getSpawns()[0];

        gameManager.tryFindWinner(game);
        user.activeChallenges.clear();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.teleport(new Location(game.getWorld(), spawn.x, spawn.y, spawn.z)), 2);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(null);
        final User user = SkywarsDatabase.getDatabase().getCached(event.getPlayer().getUniqueId());
        user.activeChallenges.clear();
        event.getPlayer().setMaxHealth(20.0);
        final List<Player> playersInSpawn = event.getPlayer().getWorld().getPlayers();
        final BaseComponent[] quitMessage = TextComponent.fromLegacyText(Messages.get("quit-format").replace("%player%", event.getPlayer().getName()));
        for (final Player player : playersInSpawn) {
            player.spigot().sendMessage(quitMessage);
        }

        SkywarsDatabase.getDatabase().save(event.getPlayer());
        gameManager.quit(event.getPlayer(), true);
    }
}