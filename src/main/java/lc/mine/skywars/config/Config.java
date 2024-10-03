package lc.mine.skywars.config;

import java.util.Map;

import org.bukkit.inventory.Inventory;

import lc.mine.skywars.chestrefill.ChestMode;
import lc.mine.skywars.kit.Kit;

public class Config {

    public final ChestRefill chestRefill = new ChestRefill();
    public final Kits kits = new Kits();

    public static final class ChestRefill {
        public ChestMode[] modes;
        public Inventory inventory;
    }

    public static final class Kits {
        public Kit[] arrayKits;
        public Map<String, Kit> perName;
        public Kit defaultKit;
    }
}
