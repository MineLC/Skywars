package lc.mine.skywars.config.message;

import java.util.Map;

import org.bukkit.command.CommandSender;

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
