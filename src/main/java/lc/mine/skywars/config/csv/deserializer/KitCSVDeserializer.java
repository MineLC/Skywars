package lc.mine.skywars.config.csv.deserializer;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import lc.mine.skywars.utils.IntegerUtil;
import net.minecraft.server.v1_8_R3.ItemStack;

public final class KitCSVDeserializer extends ItemCSVDeserializer {

    public KitCSVDeserializer(Logger logger) {
        super(logger, null);
    }
    
    public ItemStack deserialize(final String input) {
        final String[] variables = StringUtils.split(input, ',');

        int itemAmount = 1;
        Material material = Material.STONE;
        short data = 0;

        String[] lore = null;
        String name = null;
        List<EnchantmentWrapper> enchantments = null;

        for (final String variable : variables) {
            final String[] split = StringUtils.split(variable, ':');
            switch (split[0].toLowerCase()) {
                case "item":
                    material = getMaterial(split[1]);
                    break;
                case "amount":
                    itemAmount = IntegerUtil.parsePositive(split[1], 1);
                    break;
                case "enchantments":
                    enchantments = getEnchantments(split[1]);
                    break;
                case "name":
                    name = split[1];
                    break;
                case "lore":
                    lore = getLore(split[1]);
                    break;
                case "data":
                    data = (short)IntegerUtil.parsePositive(split[1], 1);
                    break;
                default:
                    logger.warning("Can't found the variable " + variable + " in the input " + input + mode);
                    break;
            }
        }
        return CraftItemStack.asNMSCopy(createItem(material, name, lore, itemAmount, data, enchantments));
    }

    public void setKitFile(final String kitFile) {
        this.mode = " KitFile = " + kitFile;
    }
}
