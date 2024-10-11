package lc.mine.skywars.game.top.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.utils.ClickableInventory;

final class TopSecundaryInventory implements ClickableInventory {
    
    private final int returnSlot;
    private final TopInventoryBuilder builder;

    TopSecundaryInventory(int returnSlot, TopInventoryBuilder builder) {
        this.returnSlot = returnSlot;
        this.builder = builder;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getSlot() == returnSlot) {
            builder.buildMainInventory(event.getWhoClicked());
        } 
    }
}
