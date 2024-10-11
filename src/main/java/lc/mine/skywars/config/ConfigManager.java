package lc.mine.skywars.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;

import com.grinderwolf.swm.api.SlimePlugin;

import lc.mine.skywars.LoadOption;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.MessageConfig;
import lc.mine.skywars.config.utils.FileUtils;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.cage.CageInventoryBuilder;
import lc.mine.skywars.game.chestrefill.ChestRefillConfig;
import lc.mine.skywars.game.chestrefill.ChestRefillConfigLoader;
import lc.mine.skywars.game.kit.KitConfig;
import lc.mine.skywars.game.kit.KitConfigLoader;
import lc.mine.skywars.game.states.GameStatesConfig;
import lc.mine.skywars.game.states.GameStatesConfigLoader;
import lc.mine.skywars.game.top.TopConfig;
import lc.mine.skywars.game.top.TopsConfigLoader;
import lc.mine.skywars.map.SkywarsMap;
import lc.mine.skywars.map.MapConfigLoader;

public class ConfigManager {

    private final SkywarsPlugin plugin;
    private final Logger logger;

    private final ChestRefillConfig chestRefillConfig = new ChestRefillConfig();
    private final KitConfig kitsConfig = new KitConfig();
    private final GameStatesConfig gameStatesConfig = new GameStatesConfig();
    private final TopConfig topConfig = new TopConfig();

    private Yaml yaml;

    private Inventory cageInventory;

    public ConfigManager(SkywarsPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void load(final SlimePlugin slime, final LoadOption loadOption) {
        try {
            yaml = new Yaml();

            switch (loadOption) {
                case DATABASE:
                    loadDatabase();
                    break;
                case MESSAGES:
                    loadMessages();
                    break;
                case MAPS:
                    loadMaps(slime);
                    break;
                case KITS:
                    loadKits(loadMainConfig());
                    break;
                case CHESTREFILL:
                    loadChestRefill();
                    break;
                case ALL:
                    loadAllConfig();
                    loadDatabase();
                    loadMaps(slime);
                    break;
                case CONFIG:
                    loadAllConfig();
                    break;
                case TOPS:
                    loadTopConfig();    
                    break;
                case GAMESTATES:
                    loadGamesStates();
                    break;
                default:
                    break;
            }   
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Can't load the plugin. Option: " + loadOption.name(), e);
        } finally {
            yaml = null;
        }
    }

    private void loadAllConfig() {
        loadMessages();
        loadChestRefill();
        loadKits(loadMainConfig());
        loadGamesStates();
        loadTopConfig();
        cageInventory = new CageInventoryBuilder().buildInventories();
    }

    private void loadDatabase() {
        FileUtils.createIfAbsent("database.yml");
        SkywarsDatabase.loadDatabase(FileUtils.getConfig(yaml, "database.yml"), plugin);
    }
    private void loadMessages() {
        FileUtils.createIfAbsent("messages.yml");
        new MessageConfig().load(FileUtils.getConfig(yaml, "messages.yml"));
    }
    private void loadMaps(final SlimePlugin slimePlugin) {
        final SkywarsMap map = new MapConfigLoader(logger).loadMap(yaml, plugin, slimePlugin);
        plugin.getGameManager().setGames(map);
    }
    private void loadKits(final ConfigSection mainConfig) {
        new KitConfigLoader(logger).load(plugin, yaml, mainConfig.getString("default-kit"), kitsConfig);
    }
    private void loadChestRefill() {
        FileUtils.createIfAbsent("chest.yml");
        new ChestRefillConfigLoader(logger).load(FileUtils.getConfig(yaml, "chest.yml"), chestRefillConfig);
    }
    private void loadGamesStates() {
        FileUtils.createIfAbsent("gamestates.yml");
        new GameStatesConfigLoader(logger).loadConfig(FileUtils.getConfig(yaml, "gamestates.yml"), gameStatesConfig);
    }
    private void loadTopConfig() {
        FileUtils.createIfAbsent("tops.yml");
        new TopsConfigLoader(logger).load(FileUtils.getConfig(yaml, "tops.yml"), plugin.getCorePlugin(), topConfig);
    }

    private ConfigSection loadMainConfig() {
        FileUtils.createIfAbsent("config.yml");
        return FileUtils.getConfig(yaml, "config.yml");
    }

    public Inventory getCageInventory() {
        return cageInventory;
    }
    public KitConfig getKitsConfig() {
        return kitsConfig;
    }
    public GameStatesConfig getGameStatesConfig() {
        return gameStatesConfig;
    }
    public ChestRefillConfig getChestRefillConfig() {
        return chestRefillConfig;
    }
    public TopConfig getTopConfig() {
        return topConfig;
    }
}