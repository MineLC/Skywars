package lc.mine.skywars.config;

import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;

import lc.mine.skywars.chestrefill.ChestRefillConfigManager;
import lc.mine.skywars.config.utils.FileUtils;
import lc.mine.skywars.kit.Kit;
import lc.mine.skywars.kit.KitConfigManager;

public class ConfigManager {

    private final Logger logger;

    public Inventory inventory;
    public Kit[] kits;

    public ConfigManager(Logger logger) {
        this.logger = logger;
    }

    public void load(final Config config) {
        FileUtils.createIfAbsent("chest.yml");

        final Yaml yaml = new Yaml();

        inventory = new ChestRefillConfigManager(logger).load(FileUtils.getConfig(yaml, "chest.yml"));
        kits = new KitConfigManager(logger).load(yaml);
    }
}