package lc.mine.skywars.game.top;

import org.bukkit.inventory.ItemStack;

import lc.mine.skywars.game.top.gui.TopInventoryBuilder;

public final class TopConfig {

    TopInventoryBuilder topInventoryBuilder;

    int amountTops = 50;

    TopSelector killSelector = new TopSelector();
    TopSelector deathSelector = new TopSelector();
    TopSelector winSelector = new TopSelector();
    TopSelector playedSelector = new TopSelector();

    public static final class TopSelector {
        int inventorySlot;
        ItemStack itemInventory;
        Top top;
        
        public int getInventorySlot() {
            return inventorySlot;
        }
        public Top getTop() {
            return top;
        }
        public ItemStack getItemInventory() {
            return itemInventory;
        }
    }

    public TopSelector getDeathSelector() {
        return deathSelector;
    }
    public TopSelector getKillSelector() {
        return killSelector;
    }
    public TopSelector getPlayedSelector() {
        return playedSelector;
    }
    public TopSelector getWinSelector() {
        return winSelector;
    }
    public int getAmountTops() {
        return amountTops;
    }

    public TopInventoryBuilder getTopInventoryBuilder() {
        return topInventoryBuilder;
    }
}