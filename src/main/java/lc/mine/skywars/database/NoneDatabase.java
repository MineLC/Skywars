package lc.mine.skywars.database;

import java.util.UUID;

import org.bukkit.entity.Player;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class NoneDatabase implements Database {

    private final Object2ObjectOpenHashMap<UUID, User> cache = new Object2ObjectOpenHashMap<>();

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
    public void close() {
        cache.clear();
    }
}
