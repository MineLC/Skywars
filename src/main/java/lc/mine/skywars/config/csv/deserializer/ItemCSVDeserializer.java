package lc.mine.skywars.config.csv.deserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lc.mine.skywars.config.csv.ListCSV;
import lc.mine.skywars.config.message.MessageColor;
import lc.mine.skywars.utils.IntegerUtil;

public class ItemCSVDeserializer {

    protected final Logger logger;
    protected String mode;

    public ItemCSVDeserializer(Logger logger, String mode) {
        this.logger = logger;
        this.mode = mode;
    }

    protected ItemStack createItem(final Material material, final String name, final String[] lore, final int amount, final short data, final List<EnchantmentWrapper> enchantments) {
        final ItemStack item = new ItemStack(material, amount, data);
        if (lore == null && name == null && enchantments == null) {
            return item;
        }

        final ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(MessageColor.fastColor(name));
        }
        if (lore != null) {
            meta.setLore(MessageColor.colorList(lore));
        }
        if (enchantments != null) {
            for (final EnchantmentWrapper enchant : enchantments) {
                meta.addEnchant(enchant.enchantment, enchant.level, true);
            }
        }
        item.setItemMeta(meta);
        return item;
    }

    protected Material getMaterial(final String type) {
        Material material;
        if (type == null || (material = Material.getMaterial(type)) == null) {
            logger.warning("The material " + type + " is invalid. Mode: " + mode + ". File: chest.yml");
            return Material.STONE;
        }
        return material;
    }

    protected String[] getLore(final String input) {
        final String[] lore = ListCSV.deserialize(input);
        if (lore == null) {
            return null;
        }
        for (int i = 0; i < lore.length; i++) {
            lore[i] = MessageColor.fastColor(lore[i]);
        }
        return lore;
    }

    protected List<EnchantmentWrapper> getEnchantments(final String input) {
        final String[] enchantments = ListCSV.deserialize(input);
        if (enchantments == null) {
            return null;
        }
        final List<EnchantmentWrapper> wrappers = new ArrayList<>();
        for (final String enchantmentSection : enchantments) {
            final int indexOfLevel = enchantmentSection.indexOf('=');
            int level = 1;
            String enchantmentName = enchantmentSection;

            if (indexOfLevel != -1) {
                enchantmentName = enchantmentSection.substring(0, indexOfLevel);
                level = IntegerUtil.parsePositive(enchantmentSection.substring(indexOfLevel+1), 1);
            }

            final Enchantment enchantment = Enchantment.getByName(enchantmentName);
            if (enchantment == null) {
                logger.warning("Can't found the enchantment " + enchantmentName + " in the section " + enchantmentSection + " Mode: " + mode + " File: chest.yml");
                continue;
            }
            wrappers.add(new EnchantmentWrapper(enchantment, level));
        }
        return wrappers;
    }


    public static record EnchantmentWrapper(Enchantment enchantment, int level) {}
}
