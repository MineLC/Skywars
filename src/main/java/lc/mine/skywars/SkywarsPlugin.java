package lc.mine.skywars;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.core.CorePlugin;
import lc.mine.skywars.command.SkywarsCommand;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.listener.PlayerLeaveGameListener;
import lc.mine.skywars.game.listener.PlayerJoinListener;
import lc.mine.skywars.game.listener.PregameListener;
import lc.mine.skywars.game.states.timer.GameTimer;
import lc.mine.skywars.game.listener.InventoryListener;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.listener.DamageListener;

import com.grinderwolf.swm.api.SlimePlugin;

public final class SkywarsPlugin extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private final GameManager gameManager = new GameManager(configManager.getGameStatesConfig(), this);

    private SlimePlugin slimePlugin;
    private CorePlugin corePlugin;

    @Override
    public void onEnable() {
        final PluginManager pluginManager = getServer().getPluginManager();
        final Plugin slime = pluginManager.getPlugin("SlimeWorldManager");
        if (slime == null) {
            getLogger().warning("Plugin can't start because don't found slimeworldmanager");
            return;
        }
        final Plugin core = pluginManager.getPlugin("LCCore");
        if (core == null) {
            getLogger().warning("Plugin can't start because don't found LCCore");
            return;
        }
        this.corePlugin = (CorePlugin)core;
        this.slimePlugin = (SlimePlugin)slime;       

        configManager.load(slimePlugin, LoadOption.ALL);

        getCommand("skywars").setExecutor(new SkywarsCommand(this));

        pluginManager.registerEvents(new InventoryListener(gameManager), this);
        pluginManager.registerEvents(new PlayerJoinListener(gameManager), this);
        pluginManager.registerEvents(new DamageListener(gameManager), this);
        pluginManager.registerEvents(new PlayerLeaveGameListener(gameManager, this), this);
        pluginManager.registerEvents(new PregameListener(configManager, gameManager), this);

        new GameTimer(gameManager, configManager).start(this);
    }

    public void reload(final LoadOption option) {
        configManager.load(slimePlugin, option);
    }

    @Override
    public void onDisable() {
        SkywarsDatabase.getDatabase().close();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public CorePlugin getCorePlugin() {
        return corePlugin;
    }
}
