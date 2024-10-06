package lc.mine.skywars.database.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.UUID;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.FastEntrySet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lc.mine.skywars.database.Database;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.database.CompleteOperation;
import lc.mine.skywars.database.User;

final class MongoDBImpl implements Database {

    private final Object2ObjectOpenHashMap<UUID, User> cache = new Object2ObjectOpenHashMap<>();
    private final MongoClient client;
    private final MongoCollection<Document> collection;
    private final ExecutorService service;

    private static final String
        KILLS = "kills",
        DEATHS = "deaths",
        KITS = "kits",
        SELECTED_KIT = "kit",
        WINS = "wins",
        CAGE_MATERIAL = "cage";

    MongoDBImpl(MongoClient client, MongoCollection<Document> collection, ExecutorService service) {
        this.client = client;
        this.collection = collection;
        this.service = service;
    }
    
    @Override
    public User getCached(UUID uuid) {
        return cache.get(uuid);
    }

    @Override
    public void save(final Player player) {
        final User data = cache.remove(player.getUniqueId());

        if (data == null) {
            return;
        }
        if (data.isNew()) {
            final Document newData = getNew(data);
            if (newData != null) {
                service.execute(() -> collection.insertOne(newData));
            }
            return;
        }

        final Bson query = createUpdateQuery(data);
        if (query != null) {
            service.execute( () -> collection.updateOne(Filters.eq("_id", player.getUniqueId()), query));
        }
    }
    private Document getNew(final User user) {
        final Document document = new Document();
        setIf(document, KILLS, user.kills, 0);
        setIf(document, DEATHS, user.deaths, 0);
        setIf(document, WINS, user.wins, 0);
        if (user.selectedKit != null) {
            document.put(SELECTED_KIT, user.selectedKit.name());
        }
        setIf(document, CAGE_MATERIAL, user.cageMaterial, Material.GLASS);
        if (!user.kits.isEmpty()) {
            document.put(KITS, user.kits);    
        }
        if (document.isEmpty()) {
            return null;
        }
        document.put("_id", user.uuid);
        return document;
    }

    private Bson createUpdateQuery(final User user) {
        final List<Bson> updates = new ArrayList<>();
        setIf(updates, KILLS, user.kills, 0);
        setIf(updates, DEATHS, user.deaths, 0);
        setIf(updates, WINS, user.wins, 0);
        if (user.selectedKit != null) {
            updates.add(Updates.set(SELECTED_KIT, user.selectedKit.name()));
        }
        setIf(updates, CAGE_MATERIAL, user.cageMaterial, Material.GLASS);
        if (!user.kits.isEmpty()) {
            updates.add(Updates.set(KITS, user.kits));    
        }
        if (updates.isEmpty()) {
            return null;
        }
        return Updates.combine(updates);
    }
    private void setIf(final Document document, final String key, final Object value, final Object compare) {
        if (!value.equals(compare)) {
            document.put(key, value);
        }
    }

    private void setIf(final List<Bson> updates, final String name, final Object value, final Object compare) {
        if (!value.equals(compare)) {
            updates.add(Updates.set(name, value));
        }
    }

    @Override
    public void load(final Player player, final CompleteOperation operation) {
        service.execute(() -> {
            final UUID uuid = player.getUniqueId();
            final Document document = collection.find(Filters.eq("_id", uuid)).limit(1).first();
            if (document == null) {
                final User user = new User.New(uuid, player.getName());
                cache.put(uuid, user);
                SkywarsPlugin.getInstance().getServer().getScheduler().runTask(SkywarsPlugin.getInstance(), ()->operation.execute(user));
                return;
            }
        
            final User user = new User(uuid, player.getName());

            user.kills = document.getInteger(KILLS, 0);
            user.deaths = document.getInteger(DEATHS, 0);
            user.wins = document.getInteger(WINS, 0);

            String materialName = document.getString(CAGE_MATERIAL);
            if (materialName != null) {
                user.cageMaterial = Material.getMaterial(materialName);
            }

            final List<String> kits = document.getList(KITS, String.class, List.of());
            if (!kits.isEmpty()) {
                user.kits = new ObjectOpenHashSet<>(kits);
            }
            user.selectedKit = SkywarsPlugin.getInstance().getManager().getConfig().kits.perName.get(document.getString(SELECTED_KIT));

            cache.put(uuid, user);
            SkywarsPlugin.getInstance().getServer().getScheduler().runTask(SkywarsPlugin.getInstance(), ()->operation.execute(user));
        });
    }

    @Override
    public void close() {
        if (cache.isEmpty()) {
            service.shutdown();
            client.close();
            return;
        }
        saveAll();

        service.shutdown();
        client.close();
    }

    @Override
    public void saveAll() {
        if (cache.isEmpty()) {
            return;
        }
        final FastEntrySet<UUID, User> entries = cache.object2ObjectEntrySet();
        final List<Document> toInsert = new ArrayList<>();

        for (final Object2ObjectMap.Entry<UUID, User> entry : entries) {
            if (entry.getValue().isNew()) {
                final Document playerData = getNew(entry.getValue());
                if (playerData != null) {
                    toInsert.add(playerData);
                }
                return;
            }
            final Bson query = createUpdateQuery(entry.getValue());
            if (query != null) {
                collection.updateOne(Filters.eq("_id", entry.getValue().uuid), query);
            }
        }
        if (!toInsert.isEmpty()) {
            collection.insertMany(toInsert);
        }
    }

    @Override
    public Map<UUID, User> getUsers() {
        return cache;
    }
}