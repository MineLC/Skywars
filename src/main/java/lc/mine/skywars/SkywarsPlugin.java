package lc.mine.skywars;

import java.util.concurrent.Executors;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.core.CorePlugin;
import lc.mine.skywars.command.FlyCommand;
import lc.mine.skywars.command.HelpCommand;
import lc.mine.skywars.command.HentaiCommand;
import lc.mine.skywars.command.LeaveCommand;
import lc.mine.skywars.command.SkywarsCommand;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.game.listener.PlayerLeaveGameListener;
import lc.mine.skywars.game.listener.PressurePlateListener;
import lc.mine.skywars.game.listener.PlayerJoinListener;
import lc.mine.skywars.game.listener.CancelEventListener;
import lc.mine.skywars.game.states.timer.GameTimer;
import lc.mine.skywars.game.top.TopFiles;
import lc.mine.skywars.game.top.TopManager;
import lc.mine.skywars.map.MapManager;
import lc.mine.skywars.game.listener.InventoryListener;
import lc.mine.skywars.game.listener.PlayerChatListener;
import lc.mine.skywars.game.listener.PlayerCombatLogListener;
import lc.mine.skywars.game.GameManager;

import com.grinderwolf.swm.api.SlimePlugin;

public final class SkywarsPlugin extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(this);
    private final TopManager topManager = new TopManager(configManager.getTopConfig(), new TopFiles(this, configManager.getTopConfig()));
    private final GameManager gameManager = new GameManager(configManager.getGameStatesConfig(), topManager, this);

    private MapManager mapManager;
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
        this.mapManager = new MapManager(getLogger(), slimePlugin, Executors.newFixedThreadPool(2));

        topManager.load();
        configManager.load(slimePlugin, LoadOption.ALL);

        getCommand("skywars").setExecutor(new SkywarsCommand(this));
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("hentai").setExecutor(new HentaiCommand());
        getCommand("leave").setExecutor(new LeaveCommand(gameManager));
        getCommand("help").setExecutor(new HelpCommand());

        pluginManager.registerEvents(new InventoryListener(gameManager, configManager), this);
        pluginManager.registerEvents(new PlayerJoinListener(configManager.getSpawnConfig(), this), this);
        pluginManager.registerEvents(new PlayerLeaveGameListener(gameManager, this), this);
        pluginManager.registerEvents(new PlayerCombatLogListener(gameManager, topManager), this);
        pluginManager.registerEvents(new CancelEventListener(gameManager), this);
        pluginManager.registerEvents(new PressurePlateListener(gameManager), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);

        new GameTimer(gameManager, topManager, configManager).start(this);
    }

    public void reload(final LoadOption option) {
        configManager.load(slimePlugin, option);
    }

    @Override
    public void onDisable() {
        topManager.save();
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
    public MapManager getMapManager() {
        return mapManager;
    }
}
