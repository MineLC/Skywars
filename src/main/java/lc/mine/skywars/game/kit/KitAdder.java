package lc.mine.skywars.game.kit;

import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.Items;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.PlayerInventory;

public class KitAdder {

    private static ConfigManager configManager;

    public static void setConfigManager(ConfigManager configManager) {
        KitAdder.configManager = configManager;
    }

    public static void add(final Player player, final Kit kit) {
        final PlayerInventory inventory = ((CraftPlayer)player).getHandle().inventory;
        final User user = SkywarsDatabase.getDatabase().getCached(player.getUniqueId());

        if(!user.activeChallenges.contains(configManager.getChallengeConfig().getNoob())){
            if (kit.effects() != null) {
                for (final PotionEffect effect : kit.effects()) {
                    player.addPotionEffect(effect);
                }
            }
        }


        if(!user.activeChallenges.contains(configManager.getChallengeConfig().getNoob())){
            if (kit.helmet() != null) {
                inventory.armor[3] = kit.helmet().cloneItemStack();
            }
            if (kit.chestplate() != null) {
                inventory.armor[2] = kit.chestplate().cloneItemStack();
            }
            if (kit.leggings() != null) {
                inventory.armor[1] = kit.leggings().cloneItemStack();
            }
            if (kit.boots() != null) {
                inventory.armor[0] = kit.boots().cloneItemStack();
            }
        }


        int i = 0;
        final ItemStack[] items = inventory.items;
        for (final ItemStack item : items) {
            if (item != null) {
                items[i] = null;
            }
            i++;
        }

        if(user.activeChallenges.contains(configManager.getChallengeConfig().getNoob())){
            Item stoneSword = Items.STONE_SWORD;
            items[0] = new ItemStack(stoneSword);
        }

        if (kit.items() == null) {
            return;
        }
        if(!user.activeChallenges.contains(configManager.getChallengeConfig().getNoob())) {
            final ItemStack[] kitItems = kit.items();
            final int kitItemsAmount = kitItems.length;
            for (int slot = 0; slot < kitItemsAmount; slot++) {
                items[slot] = kitItems[slot].cloneItemStack();
            }
        }
    }
}
