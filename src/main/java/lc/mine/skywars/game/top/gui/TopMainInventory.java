package lc.mine.skywars.game.top.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.game.top.TopConfig;
import lc.mine.skywars.game.top.TopConfig.TopSelector;
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
            sendTop(human, topConfig.getDeathSelector());
            return;
        }
        if (clickedSlot == topConfig.getKillSelector().getInventorySlot()) {
            sendTop(human, topConfig.getKillSelector());
            return;
        }
        if (clickedSlot == topConfig.getWinSelector().getInventorySlot()) {
            sendTop(human, topConfig.getWinSelector());
            return;
        }
        if (clickedSlot == topConfig.getPlayedSelector().getInventorySlot()) {
            sendTop(human, topConfig.getPlayedSelector());
            return;
        }
    }
    private void sendTop(final HumanEntity human, final TopSelector topSelector) {
        topInventoryBuilder.buildTop(human, topSelector.getTop(), topSelector.getInventoryTitle());
    }
}
