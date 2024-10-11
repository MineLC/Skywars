package lc.mine.skywars.game.chestrefill;

import org.bukkit.inventory.Inventory;

public final class ChestRefillConfig {
    private ChestMode[] modes;
    private Inventory inventory;

    public ChestMode[] getModes() {
        return modes;
    }

    public Inventory getInventory() {
        return inventory;
    }

    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    void setModes(ChestMode[] modes) {
        this.modes = modes;
    }
}
