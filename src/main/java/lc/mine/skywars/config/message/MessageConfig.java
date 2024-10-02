package lc.mine.skywars.config.message;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lc.mine.skywars.config.ConfigSection;

public final class MessageConfig {

    @SuppressWarnings("unchecked")
    public void load(final ConfigSection section) {
        final Set<Entry<String, Object>> entries = section.values().entrySet();        
        final Map<String, String> messages = new HashMap<>(entries.size());

        for (final Entry<String, Object> entry : entries) {
            if (entry.getValue() instanceof Map map) {
                loadSection(entry.getKey(), messages, map.entrySet());
                continue;
            }
            messages.put(entry.getKey(), MessageColor.toString(entry.getValue()));
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSection(final String key, final Map<String, String> messages, final Set<Entry<Object,Object>> entries) {
        for (final Entry<Object,Object> entry : entries) {
            if (!(entry instanceof Map map)) {
                messages.put(key + '.' + entry.getKey(), MessageColor.toString(entry.getValue()));
                continue;
            }
            loadSection(key + '.' + entry.getKey(), messages, map.entrySet());    
        }
    }
}
