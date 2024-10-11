package lc.mine.skywars.game.kit;

import java.util.Map;

import org.bukkit.inventory.Inventory;

public final class KitConfig {
    private Kit[] arrayKits;
    private Map<String, Kit> kitsPerName;
    private Kit defaultKit;

    private Inventory inventory;

    public Kit[] getArrayKits() {
        return arrayKits;
    }
    public Kit getDefaultKit() {
        return defaultKit;
    }
    public Inventory getInventory() {
        return inventory;
    }
    public Map<String, Kit> getKitsPerName() {
        return kitsPerName;
    }

    void setArrayKits(Kit[] arrayKits) {
        this.arrayKits = arrayKits;
    }
    void setDefaultKit(Kit defaultKit) {
        this.defaultKit = defaultKit;
    }
    void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    void setKitsPerName(Map<String, Kit> kitsPerName) {
        this.kitsPerName = kitsPerName;
    }
}
