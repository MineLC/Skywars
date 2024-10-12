package lc.mine.skywars.game.listener;

import java.lang.ref.WeakReference;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import lc.mine.core.event.PlayerDataLoadEvent;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.spawn.SpawnConfig;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerJoinListener implements Listener {

    private final SpawnConfig spawnConfig;
    private final SkywarsPlugin plugin;
    
    public PlayerJoinListener(SpawnConfig spawnConfig, SkywarsPlugin plugin) {
        this.spawnConfig = spawnConfig;
        this.plugin = plugin;
    }
   
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {

        final WeakReference<Player> referencePlayer = new WeakReference<Player>(event.getPlayer());
        Messages.send(event.getPlayer(), "join");

        SkywarsDatabase.getDatabase().load(event.getPlayer(), (user) -> {
            final Player player = referencePlayer.get();
            if (player != null) {
                spawnConfig.sendToSpawn(player, null);
            }
        });
        event.setJoinMessage(null);

        final List<Player> playersInSpawn = spawnConfig.getSpawn().getWorld().getPlayers();
        final BaseComponent[] joinMessage = TextComponent.fromLegacyText(Messages.get("join-format").replace("%player%", event.getPlayer().getName()));
        for (final Player player : playersInSpawn) {
            player.spigot().sendMessage(joinMessage);
        }
    }

    @EventHandler
    public void onPlayerLoadCoreData(final PlayerDataLoadEvent event) {
        plugin.getConfigManager().getSidebarConfig().getSpawnSidebar().sendSpawnSidebar(event.getPlayer());
    }
}