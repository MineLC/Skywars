package lc.mine.skywars.chestrefill.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.chestrefill.ChestMode;
import lc.mine.skywars.chestrefill.creator.ChestInventoryCreator;
import lc.mine.skywars.utils.ClickableInventory;

public class ChestRefillGui implements ClickableInventory {

    private ChestMode[] modes;

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        
        if (event.getClick() == ClickType.LEFT) {
            for (final ChestMode mode : modes) {
                if (mode.getInventorySlot() == slot) {
                    ChestInventoryCreator.currentMode = mode;
                    event.getWhoClicked().sendMessage("Modo selecionado : " + mode.getName());
                    return;
                }
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