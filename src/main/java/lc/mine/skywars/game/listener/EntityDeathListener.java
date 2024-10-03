package lc.mine.skywars.game.listener;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import lc.mine.skywars.config.message.Messages;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(final EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        final UUID uuid = player.getUniqueId();
        final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;

        for (final EntityPlayer otherPlayer : players) {
            if (otherPlayer.playerInteractManager.getGameMode() != EnumGamemode.SPECTATOR && !otherPlayer.getUniqueID().equals(uuid)) {
                Messages.send(player, "death");
                return;
            }
        }
        Bukkit.broadcastMessage(Messages.get("win").replace("%winner%", player.getName()));
    }
}
