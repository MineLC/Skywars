package lc.mine.skywars.game.chestrefill.creator;

import java.util.SplittableRandom;

import org.bukkit.inventory.Inventory;

import lc.mine.skywars.game.chestrefill.ChestItem;
import lc.mine.skywars.game.chestrefill.ChestMode;

public final class ChestInventoryCreator {

    private static final SplittableRandom RANDOM = new SplittableRandom();

    public static void setItems(final Inventory inventory, final ChestMode mode) {
        final ChestItem[] items = mode.getItems();
        final int length = items.length - 1;
        final int itemsSlots = inventory.getSize() - 1;
        int remainItems = mode.getItemsPerChest();

        do {
            final ChestItem item = items[RANDOM.nextInt(length)];
            if (item.probability() >= RANDOM.nextInt(100)) {
                inventory.setItem(RANDOM.nextInt(itemsSlots), item.item());
                remainItems--;
            }
        } while (remainItems > 0);
    }
}
