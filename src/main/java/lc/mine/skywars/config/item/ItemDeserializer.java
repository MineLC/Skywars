package lc.mine.skywars.config.item;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.message.MessageColor;

public class ItemDeserializer {
    
    private final Logger logger;
    private String file;

    public ItemDeserializer(Logger logger, String file) {
        this.logger = logger;
        this.file = file;
    }

    public ItemStack buildSafeItem(final ConfigSection section, final String mode) {
        if (section == null) {
            logger.warning("The item section " + mode + " is null. File: " + file);
            return new ItemStack(Material.STONE);
        }
        return buildItem(section, mode);
    }

    public ItemStack buildItem(final ConfigSection section, final String mode) {
        final String name = section.getString("name");
        final List<String> lore = section.getStringList("lore");
        
        String type = section.getString("type");
        Material material;
        if (type == null || (material = Material.getMaterial(type)) == null) {
            logger.warning("The material " + type + " is invalid. Mode: " + mode + ". File: " + file);
            material = Material.STONE;
        }
        int amount = section.getInt("amount");
        if (amount == 0) {
            amount = 1;
        }
        final ItemStack itemStack = new ItemStack(material, amount, (short)section.getInt("data"));
        if (name == null && lore == null) {
            return itemStack;
        }
        final ItemMeta meta = itemStack.getItemMeta();
        if (name != null) {
            meta.setDisplayName(MessageColor.fastColor(name));
        }
        if (lore != null) {
            meta.setLore(MessageColor.colorList(lore));
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
