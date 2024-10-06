package lc.mine.skywars.kit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.yaml.snakeyaml.Yaml;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.Config;
import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.csv.deserializer.KitCSVDeserializer;
import lc.mine.skywars.config.item.ItemDeserializer;
import lc.mine.skywars.config.message.MessageColor;
import lc.mine.skywars.config.utils.FileUtils;
import lc.mine.skywars.kit.gui.KitInventory;
import lc.mine.skywars.utils.IntegerUtil;

public final class KitConfigManager {

    private final Logger logger;

    public KitConfigManager(Logger logger) {
        this.logger = logger;
    }

    public void load(final Yaml yaml, final String defaultKitName, final Config.Kits configToLoad) {
        final File[] kitsFiles = getKitsFiles();
        final ItemDeserializer itemDeserializer = new ItemDeserializer(logger, null);
        final KitCSVDeserializer kitCSVDeserializer = new KitCSVDeserializer(logger);

        final Kit[] kits = new Kit[kitsFiles.length];
        final Map<String, Kit> perName = new Object2ObjectOpenHashMap<>();
        final Inventory inventory = Bukkit.createInventory(new KitInventory(kits), 54, "Kits");
        int i = 0;

        for (final File kitFile : kitsFiles) {
            final String fileName = kitFile.getName();
            if (!fileName.endsWith(".yml")) {
                continue;
            }
            final String kitName = fileName.substring(0, fileName.length()-4);
            itemDeserializer.setFile(fileName);
            kitCSVDeserializer.setKitFile(fileName);

            final ConfigSection kitConfig = FileUtils.getConfig(yaml, kitFile);

            final ConfigSection inventoryItemSection = kitConfig.getSection("inventory-item");
            final ItemStack inventoryItem = itemDeserializer.buildSafeItem(inventoryItemSection, "inventory-item");
            final String noPermissionMessage = kitConfig.getString("no-permission-message");
            final int inventorySlot = inventoryItemSection.getInt("slot");
            final Kit kit = new Kit(
                kitName,
                armorPiece("helmet", kitConfig, itemDeserializer),
                armorPiece("chestplate", kitConfig, itemDeserializer),
                armorPiece("leggings", kitConfig, itemDeserializer),
                armorPiece("boots", kitConfig, itemDeserializer),
                getItems(kitConfig.getStringList("items"), kitCSVDeserializer),
                getEffects(kitConfig.getStringList("effects"), fileName),
                inventorySlot,
                kitConfig.getString("permission"),
                noPermissionMessage == null ? null : MessageColor.fastColor(noPermissionMessage),
                kitConfig.getInt("cost")
            );
            inventory.setItem(inventorySlot, inventoryItem);

            kits[i++] = kit;
            perName.put(kitName, kit);
        }

        Kit defaultKit = perName.get(defaultKitName);
        if (defaultKit == null) {
            logger.warning("Can't found the default kit " + defaultKitName);
            defaultKit = kits[0];
        }

        configToLoad.arrayKits = kits;
        configToLoad.perName = perName;
        configToLoad.defaultKit = defaultKit;
        configToLoad.inventory = inventory;
    }

    private File[] getKitsFiles() {
        final File kitsFolder = new File(SkywarsPlugin.getInstance().getDataFolder(), "kits");
        final File[] kitsFiles = kitsFolder.listFiles();
        if (kitsFiles != null) {
            return kitsFiles;
        }
        if (!kitsFolder.exists()) {
            kitsFolder.mkdir();
        }
        return FileUtils.createFiles(
            "kits/constructor.yml",
            "kits/hulk.yml"
        );
    }

    private net.minecraft.server.v1_8_R3.ItemStack[] getItems(final List<String> items, final KitCSVDeserializer kitCSVDeserializer) {
        if (items == null) {
            return null;
        }
        final net.minecraft.server.v1_8_R3.ItemStack[] kitItems = new net.minecraft.server.v1_8_R3.ItemStack[items.size()];
        int i = 0;
        for (final String item : items) {
            kitItems[i++] = kitCSVDeserializer.deserialize(item);
        }
        return kitItems;
    }
    
    private PotionEffect[] getEffects(final List<String> effects, final String kitName) {
        if (effects == null) {
            return null;
        }
        final List<PotionEffect> listEffects = new ArrayList<>(effects.size());
        for (final String effect : effects) {
            final String[] typeDurationLevel = StringUtils.split(effect, ',');
            final PotionEffectType potionEffectType = PotionEffectType.getByName(typeDurationLevel[0]);
            if (potionEffectType == null) {
                logger.warning("Can't found the effect " + typeDurationLevel[0] + " in the kit " + kitName);
                continue;
            }
            final int duration = (typeDurationLevel.length >= 2) ? IntegerUtil.parsePositive(typeDurationLevel[1], 100000) : 100000;
            final int level = (typeDurationLevel.length == 3) ? IntegerUtil.parsePositive(typeDurationLevel[2], 0) : 0;

            listEffects.add(new PotionEffect(potionEffectType, duration, level));
        }
        return listEffects.toArray(new PotionEffect[0]);
    }

    private net.minecraft.server.v1_8_R3.ItemStack armorPiece(final String piece, final ConfigSection section, final ItemDeserializer itemDeserializer) {
        final ConfigSection pieceSection = section.getSection(piece);
        return pieceSection == null ? null : CraftItemStack.asNMSCopy(itemDeserializer.buildItem(pieceSection, piece));
    }
}