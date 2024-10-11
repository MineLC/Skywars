package lc.mine.skywars.config.message;

import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;

import lc.mine.skywars.game.PlayerInGame;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public final class Messages {

    private static Messages instance;
    private final Map<String, String> messages;

    Messages(Map<String, String> messages) {
        this.messages = messages;
    }

    public static void send(final CommandSender sender, final String key) {
        final String message = get(key);
        if (message != null) {
            sender.sendMessage(message);
        }
    }

    public static void sendNoGet(final Set<PlayerInGame> players, final String message) {
        final BaseComponent[] components = TextComponent.fromLegacyText(message);
        for (final PlayerInGame playerInGame : players) {
            playerInGame.getPlayer().spigot().sendMessage(components);
        }
    }

    public static String get(final String key) {
        if (instance != null) {
            return instance.messages.get(key);
        }
        return "";
    }

    public static void setInstance(Messages instance) {
        Messages.instance = instance;
    }
}
