package lc.mine.skywars.config;

import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;

import com.grinderwolf.swm.api.SlimePlugin;

import lc.mine.skywars.LoadOption;
import lc.mine.skywars.chestrefill.ChestRefillConfigManager;
import lc.mine.skywars.config.message.MessageConfig;
import lc.mine.skywars.config.utils.FileUtils;
import lc.mine.skywars.database.Database;
import lc.mine.skywars.database.NoneDatabase;
import lc.mine.skywars.database.mongodb.MongoDBConfig;
import lc.mine.skywars.game.cage.CageInventoryBuilder;
import lc.mine.skywars.kit.KitConfigManager;
import lc.mine.skywars.map.GameMap;
import lc.mine.skywars.map.MapConfigManager;

public class ConfigManager {

    private final Logger logger;

    private final Config config = new Config();

    private GameMap map;
    private Database database;
    private Inventory cageInventory;

    public ConfigManager(Logger logger) {
        this.logger = logger;
    }

    public void load(final SlimePlugin plugin, final byte option) {
        FileUtils.createIfAbsent("chest.yml", "messages.yml");

        final Yaml yaml = new Yaml();
        final ConfigSection configYML = FileUtils.getConfig(yaml, "config.yml");

        if (option == LoadOption.CONFIG) {
            loadConfig(yaml, configYML);
            return;
        }

        if (option == LoadOption.ALL) {
            loadConfig(yaml, configYML);
            map = new MapConfigManager(logger).loadMap(yaml, plugin);       
        }

        if (database == null) {
            database = loadDatabase(FileUtils.getConfig(yaml, "config.yml"));
        }
    }

    private void loadConfig(final Yaml yaml, final ConfigSection configYML) {
        new ChestRefillConfigManager(logger).load(FileUtils.getConfig(yaml, "chest.yml"), config.chestRefill);
        new KitConfigManager(logger).load(yaml, configYML.getString("default-kit"), config.kits);
        new MessageConfig().load(FileUtils.getConfig(yaml, "messages.yml"));
        cageInventory = new CageInventoryBuilder().buildInventories();
    }

    private Database loadDatabase(final ConfigSection config) {
        if (!config.getBoolean("enable-mongodb")) {
            return new NoneDatabase();
        }
        try {
            return new MongoDBConfig().load(config.getSection("mongodb"));
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error trying to enable the mongodb", e);
        }
        return new NoneDatabase();
    }

    public Database getDatabase() {
        return database;
    }
    public GameMap getMap() {
        return map;
    }
    public Inventory getCageInventory() {
        return cageInventory;
    }
    public Config getConfig() {
        return config;
    }
}