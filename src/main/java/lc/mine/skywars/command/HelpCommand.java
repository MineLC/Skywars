package lc.mine.skywars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lc.mine.skywars.config.message.Messages;

public final class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Messages.send(sender, "help-command");
        return true;
    }   
}
