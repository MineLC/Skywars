package lc.mine.skywars.game.challenge.gui;

import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.challenge.ChallengeConfig;
import lc.mine.skywars.utils.ClickableInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ChallengeMainInventory implements ClickableInventory {
    private final ChallengeInventoryBuilder challengeInventoryBuilder;
    private final ChallengeConfig challengeConfig;

    public ChallengeMainInventory(ChallengeInventoryBuilder challengeInventoryBuilder, ChallengeConfig challengeConfig) {
        this.challengeInventoryBuilder = challengeInventoryBuilder;
        this.challengeConfig = challengeConfig;
    }


    @Override
    public void onClick(final InventoryClickEvent event) {
        event.setCancelled(true);
        final int clickedSlot = event.getSlot();
        final HumanEntity human = event.getWhoClicked();
        final User user = SkywarsDatabase.getDatabase().getCached(human.getUniqueId());

        if (clickedSlot == challengeConfig.getUhc().getInventorySlot()) {
            toggleChallenge(user, challengeConfig.getUhc());
        }
        if (clickedSlot == challengeConfig.getNoob().getInventorySlot()) {
            toggleChallenge(user, challengeConfig.getNoob());
        }
        if (clickedSlot == challengeConfig.getWithoutBlocks().getInventorySlot()) {
            toggleChallenge(user, challengeConfig.getWithoutBlocks());
        }
        if (clickedSlot == challengeConfig.getArcher().getInventorySlot()) {
            toggleChallenge(user, challengeConfig.getArcher());
        }
        if (clickedSlot == challengeConfig.getWithoutChests().getInventorySlot()) {
            toggleChallenge(user, challengeConfig.getWithoutChests());
        }
        if (clickedSlot == challengeConfig.getDefinitiveWarrior().getInventorySlot()) {
            toggleChallenge(user, challengeConfig.getDefinitiveWarrior());
        }
        if (clickedSlot == challengeConfig.getMiddleLife().getInventorySlot()) {
            toggleChallenge(user, challengeConfig.getMiddleLife());
        }

    }

    private void toggleChallenge(User user, ChallengeConfig.ChallengeSelector ch) {
        Player p = Bukkit.getPlayer(user.uuid);
        if(ch == challengeConfig.getDefinitiveWarrior() &&
                user.activeChallenges.contains(challengeConfig.getArcher())){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEl desafio &f"+ch.getName()+"&c es incompatible con el desafio &eArquero&c."));
            return;
        }else if(ch == challengeConfig.getArcher() &&
                user.activeChallenges.contains(challengeConfig.getDefinitiveWarrior())){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEl desafio &f"+ch.getName()+"&c es incompatible con el desafio &eGuerrero Definitivo&c."));
            return;
        }

        if(user.activeChallenges.contains(ch)){
            user.activeChallenges.remove(ch);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDesactivaste el desafio &f"+ch.getName()+"&c."));
        }else{
            user.activeChallenges.add(ch);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aActivaste el desafio &f"+ch.getName()+"&a."));
        }
        p.closeInventory();
        challengeInventoryBuilder.buildMainInventory(p);
    }


}
