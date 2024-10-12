package lc.mine.skywars.game.listener;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(final AsyncPlayerChatEvent event) {
        final List<Player> players = event.getPlayer().getWorld().getPlayers();
        final Set<Player> recipients = event.getRecipients();
        recipients.clear();
        recipients.addAll(players);
    }
}
