package lc.mine.skywars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.skywars.chestrefill.listener.ChestInventoryListener;
import lc.mine.skywars.command.SkywarsCommand;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.game.listener.EntityDeathListener;
import lc.mine.skywars.game.listener.PlayerQuitAndJoinListener;
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

        manager.load(slimePlugin);

        getCommand("skywars").setExecutor(new SkywarsCommand(this, manager));
        
        final LongOpenHashSet chestsInCooldown = new LongOpenHashSet();
        getServer().getPluginManager().registerEvents(new ChestInventoryListener(chestsInCooldown), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitAndJoinListener(manager), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PregameListener(manager), this);

        new PregameTask(manager.map, new InGameTask(manager.map, chestsInCooldown)).start();
    }

    public void reload() {
        manager.load(slimePlugin);
    }

    @Override
    public void onDisable() {
        // TODO : Database
    }
    
    public static SkywarsPlugin getInstance() {
        return instance;
    }
}
