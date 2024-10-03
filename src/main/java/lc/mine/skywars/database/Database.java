package lc.mine.skywars.database;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public interface Database {
    
    void save(final Player player);
    void saveAll();
    void load(final Player player, final CompleteOperation supply);
    User getCached(UUID uuid);
    Map<UUID, User> getUsers();
    void close();
}