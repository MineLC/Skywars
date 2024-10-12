package lc.mine.skywars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lc.mine.skywars.config.message.Messages;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players");
            return true;
        }
        if (!player.hasPermission("vip")) {
            Messages.send(sender, "fly.need-permission");
            return true;
        }
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            Messages.send(sender, "fly.off");
            return true;
        }
        player.setAllowFlight(true);
        Messages.send(sender, "fly.on");
        return true;
    }
}
