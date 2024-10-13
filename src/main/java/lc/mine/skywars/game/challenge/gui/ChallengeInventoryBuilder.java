package lc.mine.skywars.game.challenge.gui;

import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.challenge.ChallengeConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChallengeInventoryBuilder {
    private final ChallengeConfig challengeConfig;

    private final ItemStack on = new ItemStack(Material.INK_SACK, 1, (short) 2);
    private final ItemStack off = new ItemStack(Material.INK_SACK, 1, (short) 8);
    private final ChallengeMainInventory mainInventory;

    public ChallengeInventoryBuilder(ChallengeConfig challengeConfig) {
        this.challengeConfig = challengeConfig;
        this.mainInventory = new ChallengeMainInventory(this, challengeConfig);
    }

    public void buildMainInventory(final HumanEntity entity) {
        final User user = SkywarsDatabase.getDatabase().getCached(entity.getUniqueId());

        final Inventory inventory = Bukkit.createInventory(mainInventory, 54, "Desafios en partida");

        if(user.activeChallenges.contains(challengeConfig.getUhc())) {
            inventory.setItem(challengeConfig.getUhc().getInventorySlot(), challengeConfig.getUhc().getOnItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, on);
        } else {
            inventory.setItem(challengeConfig.getUhc().getInventorySlot(), challengeConfig.getUhc().getOffItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, off);
        }


        if(user.activeChallenges.contains(challengeConfig.getDefinitiveWarrior())) {
            inventory.setItem(challengeConfig.getDefinitiveWarrior().getInventorySlot(), challengeConfig.getDefinitiveWarrior().getOnItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, on);
        } else {
            inventory.setItem(challengeConfig.getDefinitiveWarrior().getInventorySlot(), challengeConfig.getDefinitiveWarrior().getOffItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, off);
        }


        if(user.activeChallenges.contains(challengeConfig.getNoob())) {
            inventory.setItem(challengeConfig.getNoob().getInventorySlot(), challengeConfig.getNoob().getOnItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, on);
        } else {
            inventory.setItem(challengeConfig.getNoob().getInventorySlot(), challengeConfig.getNoob().getOffItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, off);
        }


        if(user.activeChallenges.contains(challengeConfig.getArcher())) {
            inventory.setItem(challengeConfig.getArcher().getInventorySlot(), challengeConfig.getArcher().getOnItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, on);
        } else {
            inventory.setItem(challengeConfig.getArcher().getInventorySlot(), challengeConfig.getArcher().getOffItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, off);
        }


        if(user.activeChallenges.contains(challengeConfig.getMiddleLife())) {
            inventory.setItem(challengeConfig.getMiddleLife().getInventorySlot(), challengeConfig.getMiddleLife().getOnItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, on);
        } else {
            inventory.setItem(challengeConfig.getMiddleLife().getInventorySlot(), challengeConfig.getMiddleLife().getOffItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, off);
        }


        if(user.activeChallenges.contains(challengeConfig.getWithoutBlocks())) {
            inventory.setItem(challengeConfig.getWithoutBlocks().getInventorySlot(), challengeConfig.getWithoutBlocks().getOnItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, on);
        } else {
            inventory.setItem(challengeConfig.getWithoutBlocks().getInventorySlot(), challengeConfig.getWithoutBlocks().getOffItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, off);
        }



        if(user.activeChallenges.contains(challengeConfig.getWithoutChests())) {
            inventory.setItem(challengeConfig.getWithoutChests().getInventorySlot(), challengeConfig.getWithoutChests().getOnItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, on);
        } else {
            inventory.setItem(challengeConfig.getWithoutChests().getInventorySlot(), challengeConfig.getWithoutChests().getOffItemInventory());
            inventory.setItem(challengeConfig.getUhc().getInventorySlot()+9, off);
        }


        entity.openInventory(inventory);
    }

}
