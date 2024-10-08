package lc.mine.skywars;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.core.CorePlugin;
import lc.mine.skywars.chestrefill.listener.ChestInventoryListener;
import lc.mine.skywars.command.SkywarsCommand;
import lc.mine.skywars.command.TopCommand;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.game.listener.PlayerLeaveGameListener;
import lc.mine.skywars.game.listener.PlayerJoinListener;
import lc.mine.skywars.game.listener.PregameListener;
import lc.mine.skywars.game.listener.DamageListener;
import lc.mine.skywars.game.tasks.InGameTask;
import lc.mine.skywars.game.tasks.PregameTask;
import lc.mine.skywars.tops.TopFiles;

import com.grinderwolf.swm.api.SlimePlugin;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

public class SkywarsPlugin extends JavaPlugin {

    private static SkywarsPlugin instance;

    private final ConfigManager manager = new ConfigManager(getLogger());

    private SlimePlugin slimePlugin;
    private CorePlugin corePlugin;
    private TopFiles topFiles;

    @Override
    public void onEnable() {
        final Plugin slime = Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        if (slime == null) {
            getLogger().warning("Plugin can't start because don't found slimeworldmanager");
            return;
        }
        final Plugin core = Bukkit.getPluginManager().getPlugin("LCCore");
        if (core == null) {
            getLogger().warning("Plugin can't start because don't found LCCore");
            return;
        }

        final File topFolder = new File(getDataFolder(), "tops");
        if (!topFolder.exists()) {
            topFolder.mkdir(); 
        }
        this.topFiles = new TopFiles(topFolder, 50);
        this.topFiles.start();

        this.corePlugin = (CorePlugin)core;
        this.slimePlugin = (SlimePlugin)slime;       

        instance = this;

        manager.load(slimePlugin, LoadOption.ALL);

        getCommand("skywars").setExecutor(new SkywarsCommand(this, manager.getConfig().chestRefill, manager.getConfig().kits));
        getCommand("top").setExecutor(new TopCommand());
        
        final LongOpenHashSet chestsInCooldown = new LongOpenHashSet();
        getServer().getPluginManager().registerEvents(new ChestInventoryListener(chestsInCooldown), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(manager), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveGameListener(manager.getDatabase(), manager.getMap()), this);
        getServer().getPluginManager().registerEvents(new PregameListener(manager.getConfig().chestRefill, manager.getConfig().kits, manager.getCageInventory()), this);

        new PregameTask(manager, new InGameTask(manager.getMap(), chestsInCooldown)).start();
    }

    public void reload() {
        manager.load(slimePlugin, LoadOption.CONFIG);
    }

    @Override
    public void onDisable() {
        topFiles.save();
        manager.getDatabase().close();
    }

    public ConfigManager getManager() {
        return manager;
    }

    public CorePlugin getCorePlugin() {
        return corePlugin;
    }

    public static SkywarsPlugin getInstance() {
        return instance;
    }
}
