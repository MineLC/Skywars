package lc.mine.skywars.database;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class NoneDatabase implements Database {

    private final Map<UUID, User> cache = new HashMap<>();

    @Override
    public void save(Player player) {}
    @Override
    public void saveAll() {}

    @Override
    public void load(Player player, CompleteOperation operation) {
        final User user = new User(player.getUniqueId(), player.getName());
        cache.put(player.getUniqueId(), user);
        operation.execute(user);
    }

    @Override
    public User getCached(UUID uuid) {
        return cache.get(uuid);
    }

    @Override
    public Map<UUID, User> getUsers() {
        return cache;
    }

    @Override
    public void close() {
        cache.clear();
    }
}
