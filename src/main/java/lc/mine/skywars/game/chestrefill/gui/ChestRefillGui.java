package lc.mine.skywars.game.chestrefill.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.chestrefill.ChestMode;
import lc.mine.skywars.utils.ClickableInventory;

public class ChestRefillGui implements ClickableInventory {

    private ChestMode[] modes;

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        event.setCancelled(true);
        
        if (event.getClick() == ClickType.LEFT) {
            final int amountModes = modes.length;
            for (int i = 0; i < amountModes; i++) {
                final ChestMode mode = modes[i];
                if (mode.getInventorySlot() != slot) {
                    continue;
                }
                final User user = SkywarsDatabase.getDatabase().getCached(event.getWhoClicked().getUniqueId());
                user.chestRefillVoteIndex = i; 
                event.getWhoClicked().sendMessage(Messages.get("selectChestMode").replace("%mode%", mode.getName()));
                return;
            }                
            return;
        }

        for (final ChestMode mode : modes) {
            if (mode.getInventorySlot() == slot) {
                event.getWhoClicked().openInventory(mode.getPreviewItemsInventory());
                return;
            }
        }
    }

    public void setModes(ChestMode[] modes) {
        this.modes = modes;
    }
}