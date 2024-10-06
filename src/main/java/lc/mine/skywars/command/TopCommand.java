package lc.mine.skywars.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import lc.mine.skywars.tops.TopStorage;
import lc.mine.skywars.tops.inventory.TopInventoryBuilder;


public class TopCommand implements TabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 0 ? List.of("kills", "deaths") : List.of();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("Formato: /top (wins/deaths)");
            return true;
        }
        switch (args[0]) {
            case "deaths":
            case "death":
            case "muertes":
            case "muerte":
                TopInventoryBuilder.build(player, TopStorage.deaths(), "Top de muertes");
                break;

            case "wins":
            case "victorias":
                TopInventoryBuilder.build(player, TopStorage.wins(), "Top de muertes");
                break;
            default:
                break;
        }
        return false;
    }
}
