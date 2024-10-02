package lc.mine.skywars.kit;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PlayerInventory;

public class KitAdder {

    public static void add(final Player player, final Kit kit) {
        final PlayerInventory inventory = ((CraftPlayer)player).getHandle().inventory;

        if (kit.effects() != null) {
            for (final PotionEffect effect : kit.effects()) {
                player.addPotionEffect(effect);
            }
        }

        if (kit.helmet() != null) {
            inventory.armor[3] = kit.helmet();
        }
        if (kit.chestplate() != null) {
            inventory.armor[2] = kit.chestplate();
        }
        if (kit.leggings() != null) {
            inventory.armor[1] = kit.leggings();
        }
        if (kit.boots() != null) {
            inventory.armor[0] = kit.boots();
        }

        int i = 0;
        final ItemStack[] items = inventory.items;
        for (final ItemStack item : items) {
            if (item != null) {
                items[i] = null;
            }
            i++;
        }
        if (kit.items() == null) {
            return;
        }

        final ItemStack[] kitItems = kit.items();
        final int kitItemsAmount = kitItems.length;
        for (int slot = 0; slot < kitItemsAmount; slot++) {
            items[slot] = kitItems[slot];
        }
    }
}
