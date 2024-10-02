package lc.mine.skywars.config.csv.deserializer;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

import lc.mine.skywars.chestrefill.ChestItem;
import lc.mine.skywars.utils.IntegerUtil;

public final class ChestRefillCSVDeserializer extends ItemCSVDeserializer {

    public ChestRefillCSVDeserializer(Logger logger, String mode) {
        super(logger, mode);
    }
    
    public ChestItem deserialize(final String input) {
        final String[] variables = StringUtils.split(input, ',');

        int itemPercentage = 50;

        int itemAmount = 1;
        Material material = Material.STONE;
        short data = 0;

        String[] lore = null;
        String name = null;
        List<EnchantmentWrapper> enchantments = null;

        for (final String variable : variables) {
            final int length = variable.length();
            final int endIndex = length - 1;

            if (length <= 4 && variable.charAt(endIndex) == '%') {
                itemPercentage = (variable.length() == 1) ? 50 : getPercentage(input.substring(0, endIndex), input);
                continue;
            }

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
                    logger.warning("Can't found the variable " + variable + " in the input " + input +  " Mode: " + mode + ". File: chest.yml");
                    break;
            }
        }
        return new ChestItem(createItem(material, name, lore, itemAmount, data, enchantments), itemPercentage);
    }

    private int getPercentage(final String input, final String item) {
        final int itemPercentage = IntegerUtil.parsePositive(input, -1);
        if (itemPercentage == -1) {
            logger.warning("The item " + item + " contains invalid percentage (Setting in 50% default). Mode: " + mode + ". File: chest.yml");
            return 50;
        }
        return itemPercentage;
    }
}
