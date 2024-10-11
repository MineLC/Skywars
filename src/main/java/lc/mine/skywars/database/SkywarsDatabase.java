package lc.mine.skywars.database;

import java.util.logging.Logger;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.database.mongodb.MongoDBConfig;

public final class SkywarsDatabase {

    private static Database database;

    public static void loadDatabase(final ConfigSection config, final SkywarsPlugin plugin) {
        if (database != null) {
            database.close();
        }
        final Logger logger = plugin.getLogger();
        if (!config.getBoolean("enable-mongodb")) {
            database = new NoneDatabase();
            logger.info("MongoDB Disabled. Using none database");
            return;
        }

        try {
            database = new MongoDBConfig().load(config.getSection("mongodb"), plugin);
            logger.info("Mongodb connection successfully");
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error trying to enable the mongodb", e);
            database = new NoneDatabase();
        }
    }

    public static Database getDatabase() {
        return database;
    }
}