package lc.mine.skywars.config;

import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;

import com.grinderwolf.swm.api.SlimePlugin;

import lc.mine.skywars.chestrefill.ChestRefillConfigManager;
import lc.mine.skywars.config.message.MessageConfig;
import lc.mine.skywars.config.utils.FileUtils;
import lc.mine.skywars.kit.Kit;
import lc.mine.skywars.kit.KitConfigManager;
import lc.mine.skywars.map.GameMap;
import lc.mine.skywars.map.MapConfigManager;

public class ConfigManager {

    private final Logger logger;

    public Inventory inventory;
    public Kit[] kits;
    public GameMap map;

    public ConfigManager(Logger logger) {
        this.logger = logger;
    }

    public void load(final SlimePlugin plugin) {
        FileUtils.createIfAbsent("chest.yml", "messages.yml");

        final Yaml yaml = new Yaml();

        inventory = new ChestRefillConfigManager(logger).load(FileUtils.getConfig(yaml, "chest.yml"));
        kits = new KitConfigManager(logger).load(yaml);
        map = new MapConfigManager(logger).loadMap(yaml, plugin);

        new MessageConfig().load(FileUtils.getConfig(yaml, "messages.yml"));
    }
}