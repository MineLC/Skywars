package lc.mine.skywars.config.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record InventoryItem(
    Material cachedMaterial,
    ItemStack item,
    int slot
) {
}