package lc.mine.skywars.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import lc.mine.skywars.LoadOption;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;

public final class SkywarsCommand implements TabExecutor {

    private final SkywarsPlugin plugin;
    private final String version;

    public SkywarsCommand(SkywarsPlugin plugin) {
        this.plugin = plugin;
        this.version = plugin.getDescription().getVersion();
    }

    private String format() {
        return
            """
                \n
                §6§lSkywars §bv%v% §7- §b§lMINE§6§lLC 
                \n
                §e/skywars
                    §6msg §e(key) §7-> §fSend the message
                \n
                    §6reload §7-> §fReload plugin
                        §call §7-> Database, configs, etc
                        §cconfig §7-> Messages, kits, cages, etc
                        §ckits §7-> Only kits
                        §cchestrefill §7-> Only chestrefill
                        §cmessages §7-> Only messages
                        §cmaps §7-> Only maps
                        §cstates §7-> pregame, ingame and endgame
                        §cdatabase §7-> Only database
                \n
            """.replace("%v%", version);
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 1) {
            return List.of("reload", "chest", "kits", "msg");
        }
        return (args.length == 2 && args[0].equalsIgnoreCase("reload"))
            ? List.of("all", "config", "kits", "chestrefill", "messages", "maps", "states", "database")
            : List.of();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1 || !sender.hasPermission("skywars.admin")) {
            sender.sendMessage(format());
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "reload", "r":
                if (args.length != 2) {
                    sender.sendMessage("§cAvailable reload options: §6" + LoadOption.OPTIONS);
                    break;
                }
                final String optionName = args[1].toUpperCase();
                for (final LoadOption option : LoadOption.OPTIONS) {
                    if (option.name().equals(optionName)) {
                        plugin.reload(option);
                        sender.sendMessage("§aPlugin reloaded. Option: " + option.name());    
                        break;
                    }
                }
                sender.sendMessage("§cCan't found the load option: " + optionName + " Available options: §6" + LoadOption.OPTIONS);
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
