package lc.mine.skywars.game.top.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.game.top.TopConfig;
import lc.mine.skywars.utils.ClickableInventory;

public class TopMainInventory implements ClickableInventory {

    private final TopInventoryBuilder topInventoryBuilder;
    private final TopConfig topConfig;

    public TopMainInventory(TopInventoryBuilder topInventoryBuilder, TopConfig topConfig) {
        this.topInventoryBuilder = topInventoryBuilder;
        this.topConfig = topConfig;
    }

    @Override
    public void onClick(final InventoryClickEvent event) {
        event.setCancelled(true);
        final int clickedSlot = event.getSlot();
        final HumanEntity human = event.getWhoClicked();

        if (clickedSlot == topConfig.getDeathSelector().getInventorySlot()) {
            topInventoryBuilder.buildTop(human, topConfig.getDeathSelector().getTop());
            return;
        }
        if (clickedSlot == topConfig.getKillSelector().getInventorySlot()) {
            topInventoryBuilder.buildTop(human, topConfig.getKillSelector().getTop());
            return;
        }
        if (clickedSlot == topConfig.getWinSelector().getInventorySlot()) {
            topInventoryBuilder.buildTop(human, topConfig.getWinSelector().getTop());
            return;
        }
        if (clickedSlot == topConfig.getPlayedSelector().getInventorySlot()) {
            topInventoryBuilder.buildTop(human, topConfig.getPlayedSelector().getTop());
            return;
        }
    }
}
