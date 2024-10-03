package lc.mine.skywars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.skywars.chestrefill.listener.ChestInventoryListener;
import lc.mine.skywars.command.SkywarsCommand;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.game.listener.PlayerLeaveGameListener;
import lc.mine.skywars.game.listener.PlayerJoinListener;
import lc.mine.skywars.game.listener.PregameListener;
import lc.mine.skywars.game.tasks.InGameTask;
import lc.mine.skywars.game.tasks.PregameTask;

import com.grinderwolf.swm.api.SlimePlugin;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public class SkywarsPlugin extends JavaPlugin {

    private static SkywarsPlugin instance;

    private final ConfigManager manager = new ConfigManager(getLogger());
    private SlimePlugin slimePlugin;

    @Override
    public void onEnable() {
        final Plugin plugin = Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        if (plugin == null) {
            getLogger().warning("Plugin can't start because don't found slimeworldmanager");
            return;
        }
        this.slimePlugin = (SlimePlugin)plugin;        
        instance = this;

        manager.load(slimePlugin, LoadOption.ALL);

        getCommand("skywars").setExecutor(new SkywarsCommand(this, manager.getConfig().chestRefill, manager.getConfig().kits));
        
        final LongOpenHashSet chestsInCooldown = new LongOpenHashSet();
        getServer().getPluginManager().registerEvents(new ChestInventoryListener(chestsInCooldown), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(manager), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveGameListener(manager.getDatabase(), manager.getMap()), this);
        getServer().getPluginManager().registerEvents(new PregameListener(manager.getConfig().chestRefill, manager.getConfig().kits), this);

        new PregameTask(manager, new InGameTask(manager.getMap(), chestsInCooldown)).start();
    }

    public void reload() {
        manager.load(slimePlugin, LoadOption.CONFIG);
    }

    @Override
    public void onDisable() {
        manager.getDatabase().close();
    }

    public ConfigManager getManager() {
        return manager;
    }

    public static SkywarsPlugin getInstance() {
        return instance;
    }
}
