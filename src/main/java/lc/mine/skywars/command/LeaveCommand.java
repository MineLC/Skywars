package lc.mine.skywars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.game.GameManager;

public final class LeaveCommand implements CommandExecutor {

    private final GameManager gameManager;

    public LeaveCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players");
            return true;
        }
        if (gameManager.getGame(player) == null) {
            Messages.send(player, "quit-need-be-in-game");
            return true;
        }
        gameManager.quit(player, false);
        return true;
    }
}
