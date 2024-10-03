package lc.mine.skywars.command;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.Config;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.kit.gui.KitInventoryCreator;

public final class SkywarsCommand implements TabExecutor {

    private final SkywarsPlugin plugin;
    private final String version;

    private final Config.ChestRefill chestRefill;
    private final Config.Kits kits;

    public SkywarsCommand(SkywarsPlugin plugin, Config.ChestRefill chestRefill, Config.Kits kits) {
        this.plugin = plugin;
        this.version = plugin.getDescription().getVersion();
        this.chestRefill = chestRefill;
        this.kits = kits;
    }

    private String format() {
        return
            """
                \n
                §6§lSkywars §bv%v% §7- §b§lMINE§6§lLC 
                \n
                §e/skywars
                    §6chest §7-> §fOpen ChestRefill test
                    §6kits §7-> §fSee all kits and select one
                    §6msg §e(key) §7-> §fSend the message
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
            sender.sendMessage("§b§lSkywars §7by §b§lMINE§6§lLC §7- §bv" + version);
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
                ((Player)sender).openInventory(chestRefill.inventory);
                break;
            case "kits":
                ((Player)sender).openInventory(KitInventoryCreator.create(kits.arrayKits));
                break;
            case "msg":
                if (args.length < 2) {
                    sender.sendMessage("Format: /skywars msg (key). Example: /skywars msg join");
                    return true;
                }
                final String message = Messages.get(args[1]);
                if (message == null) {
                    sender.sendMessage("The message " + args[1] + " don't exist");
                    return true;
                }
                sender.sendMessage(message);
                return true;
            default:
                sender.sendMessage(format());
                break;
        }
        return true;
    }
}
