package lc.mine.skywars.config.message;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

public final class MessageColor {

    public static String toString(final Object object) {
        if (object == null) {
            return null;
        }
        String message = object.toString();

        if (object instanceof List<?>) {
            final List<?> list = (List<?>)object;
            final StringBuilder builder = new StringBuilder();
            int size = list.size();
            int i = 0;
            for (final Object object2 : list) {
                builder.append(object2);
                if (++i == size) {
                    continue;
                }
                builder.append('\n');
            }
            message = builder.toString();
        }

        if (message.isEmpty() || message.isBlank()) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String fastColor(final String message) {
        return message.replace('&', ChatColor.COLOR_CHAR);
    }

    public static List<String> colorList(final List<String> list) {
        final int size = list.size();
        final List<String> copy = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            copy.add(fastColor(list.get(i)));
        }
        return copy;
    }

    
    public static List<String> colorList(final String[] list) {
        final int size = list.length;
        final List<String> copy = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            copy.add(fastColor(list[i]));
        }
        return copy;
    }
}