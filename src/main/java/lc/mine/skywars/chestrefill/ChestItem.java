package lc.mine.skywars.chestrefill;

import org.bukkit.inventory.ItemStack;

public record ChestItem(
    ItemStack item,
    int probability
) {
}
