package lc.mine.skywars;

import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.skywars.chestrefill.listener.ChestInventoryListener;
import lc.mine.skywars.command.SkywarsCommand;
import lc.mine.skywars.config.ConfigManager;

public class SkywarsPlugin extends JavaPlugin {

    private static SkywarsPlugin instance;

    private final ConfigManager manager = new ConfigManager(getLogger());

    @Override
    public void onEnable() {
        instance = this;
        manager.load(null);

        getCommand("skywars").setExecutor(new SkywarsCommand(this, manager));
        getServer().getPluginManager().registerEvents(new ChestInventoryListener(), this);
    }

    public void reload() {
        manager.load(null);
    }

    @Override
    public void onDisable() {
        
    }
    
    public static SkywarsPlugin getInstance() {
        return instance;
    }
}
