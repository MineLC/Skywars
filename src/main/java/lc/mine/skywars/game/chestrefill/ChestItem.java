package lc.mine.skywars.game.chestrefill;

import org.bukkit.inventory.ItemStack;

public record ChestItem(
    ItemStack item,
    int probability
) {
}
