package lc.mine.skywars.game.challenge;

import lc.mine.skywars.game.challenge.gui.ChallengeInventoryBuilder;
import org.bukkit.inventory.ItemStack;

public class ChallengeConfig {
    ChallengeInventoryBuilder challengeInventoryBuilder;

    ChallengeSelector uhc = new ChallengeSelector();
    ChallengeSelector withoutBlocks = new ChallengeSelector();
    ChallengeSelector noob = new ChallengeSelector();
    ChallengeSelector middleLife = new ChallengeSelector();
    ChallengeSelector archer = new ChallengeSelector();
    ChallengeSelector definitiveWarrior = new ChallengeSelector();
    ChallengeSelector withoutChests = new ChallengeSelector();
    
    ItemStack offItem, onItem;

    public ChallengeInventoryBuilder getChallengeInventoryBuilder() {
        return challengeInventoryBuilder;
    }

    public static class ChallengeSelector {
        int inventorySlot;
        String name;
        ItemStack onItemInventory;
        ItemStack offItemInventory;
        double coinsXKill;

        public String getName() {
            return name;
        }
        public double getCoinsXKill() {
            return coinsXKill;
        }
        public int getInventorySlot() {
            return inventorySlot;
        }
        public ItemStack getOffItemInventory() {
            return offItemInventory;
        }
        public ItemStack getOnItemInventory() {
            return onItemInventory;
        }
    }

    public ChallengeSelector getArcher() {
        return archer;
    }

    public void setArcher(ChallengeSelector archer) {
        this.archer = archer;
    }

    public void setChallengeInventoryBuilder(ChallengeInventoryBuilder challengeInventoryBuilder) {
        this.challengeInventoryBuilder = challengeInventoryBuilder;
    }

    public ChallengeSelector getDefinitiveWarrior() {
        return definitiveWarrior;
    }

    public void setDefinitiveWarrior(ChallengeSelector definitiveWarrior) {
        this.definitiveWarrior = definitiveWarrior;
    }

    public ChallengeSelector getMiddleLife() {
        return middleLife;
    }

    public void setMiddleLife(ChallengeSelector middleLife) {
        this.middleLife = middleLife;
    }

    public ChallengeSelector getNoob() {
        return noob;
    }

    public void setNoob(ChallengeSelector noob) {
        this.noob = noob;
    }

    public ChallengeSelector getUhc() {
        return uhc;
    }

    public void setUhc(ChallengeSelector uhc) {
        this.uhc = uhc;
    }

    public ChallengeSelector getWithoutBlocks() {
        return withoutBlocks;
    }

    public void setWithoutBlocks(ChallengeSelector withoutBlocks) {
        this.withoutBlocks = withoutBlocks;
    }

    public ChallengeSelector getWithoutChests() {
        return withoutChests;
    }

    public void setWithoutChests(ChallengeSelector withoutChests) {
        this.withoutChests = withoutChests;
    }
    public ItemStack getOffItem() {
        return offItem;
    }
    public ItemStack getOnItem() {
        return onItem;
    }
}
