package lc.mine.skywars.kit;

import org.bukkit.potion.PotionEffect;

import net.minecraft.server.v1_8_R3.ItemStack;

public record Kit(
    String name,
    ItemStack helmet,
    ItemStack chestplate,
    ItemStack leggings,
    ItemStack boots,
    ItemStack[] items,

    PotionEffect[] effects,
    int inventorySlot,

    String permission,
    String noPermissionMessage,
    int cost
) {
}
