package lc.mine.skywars.chestrefill.creator;

import java.util.SplittableRandom;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import lc.mine.skywars.chestrefill.ChestItem;
import lc.mine.skywars.chestrefill.ChestMode;

public final class ChestInventoryCreator {

    private static final SplittableRandom RANDOM = new SplittableRandom();
    public static ChestMode currentMode;

    public static Inventory createInventory() {
        final Inventory inventory = Bukkit.createInventory(null, 27, currentMode.getName());
        final ChestItem[] items = currentMode.getItems();
        final int length = items.length - 1;
        int remainItems = currentMode.getItemsPerChest();

        do {
            final ChestItem item = items[RANDOM.nextInt(length)];
            if (item.probability() >= RANDOM.nextInt(100)) {
                inventory.setItem(RANDOM.nextInt(27), item.item());
                remainItems--;
            }
        } while (remainItems > 0);

        return inventory;
    }
}
