package lc.mine.skywars.command;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.kit.gui.KitInventoryCreator;

public final class SkywarsCommand implements TabExecutor {

    private final SkywarsPlugin plugin;
    private final String version;

    private final ConfigManager manager;

    public SkywarsCommand(SkywarsPlugin plugin, ConfigManager manager) {
        this.plugin = plugin;
        this.version = plugin.getDescription().getVersion();
        this.manager = manager;
    }

    private String format() {
        return
            """
                \n
                §3§lSkywars v§b%v% §7- §b§lMINE§6§lLC 
                \n
                §e/skywars
                    §6chest §7-> §fOpen ChestRefill test
                    §6kits §7-> §fSee all kits and select one
                \n
            """.replace("%v%", version);
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? List.of("reload") : null;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("skywars.use")) {
            sender.sendMessage("§3§lSkywars §7by §b§lMINE§6§lLC §e- v" + version);
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(format());
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reload", "r":
                try {
                    plugin.reload();
                    sender.sendMessage("§aPlugin reloaded");    
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error reloading the plugin", e);
                    sender.sendMessage("§cError reloading the plugin, see the console");
                }
                break;
            case "chest":
                ((Player)sender).openInventory(manager.inventory);
                break;
            case "kits":
                ((Player)sender).openInventory(KitInventoryCreator.create(manager.kits));
                break;
            
            default:
                sender.sendMessage(format());
                break;
        }
        return true;
    }
}
