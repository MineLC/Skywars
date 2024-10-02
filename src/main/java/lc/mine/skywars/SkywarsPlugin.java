package lc.mine.skywars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.skywars.chestrefill.listener.ChestInventoryListener;
import lc.mine.skywars.command.SkywarsCommand;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.game.listener.PlayerJoinListener;

import com.grinderwolf.swm.api.SlimePlugin;

public class SkywarsPlugin extends JavaPlugin {

    private static SkywarsPlugin instance;

    private final ConfigManager manager = new ConfigManager(getLogger());
    private SlimePlugin slimePlugin;

    @Override
    public void onEnable() {
        if (!(Bukkit.getPluginManager().getPlugin("SlimeWorldManager") instanceof SlimePlugin slimePlugin)) {
            getLogger().warning("Plugin can't start because don't found slimeworldmanager");
            return;
        }
        this.slimePlugin = slimePlugin;        
        instance = this;

        manager.load(slimePlugin);

        getCommand("skywars").setExecutor(new SkywarsCommand(this, manager));
        getServer().getPluginManager().registerEvents(new ChestInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(manager), this);
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
