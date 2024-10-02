package lc.mine.skywars.chestrefill;

import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lc.mine.skywars.chestrefill.gui.ChestPreviewItemsGui;
import lc.mine.skywars.chestrefill.gui.ChestRefillGui;
import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.csv.deserializer.ChestRefillCSVDeserializer;
import lc.mine.skywars.config.item.ItemDeserializer;
import lc.mine.skywars.config.message.MessageColor;

public class ChestRefillConfigManager {

    private final Logger logger;

    public ChestRefillConfigManager(Logger logger) {
        this.logger = logger;
    }

    public Inventory load(final ConfigSection config) {
        final ConfigSection modes = config.getSection("modes");
        if (modes == null) {
            logger.info("Can't found chest modes. Check your chest.yml");
            return null;
        }
        final ItemDeserializer itemDeserializer = new ItemDeserializer(logger, "chest.yml");

        final ItemStack backItem = itemDeserializer.buildSafeItem(config.getSection("back-item"), "back-item");
        final ItemStack nextItem = itemDeserializer.buildSafeItem(config.getSection("next-item"), "next-item");
        final int backSlot = config.getInt("back-slot"), nextSlot = config.getInt("next-slot");

        int amountRows = config.getInt("inventory-rows");
        if (amountRows <= 0) {
            amountRows = 1;
        } else if (amountRows > 6) {
            amountRows = 6;
        }

        final ChestRefillGui gui = new ChestRefillGui();
        final Inventory inventory = Bukkit.createInventory(
            gui,
            amountRows * 9,
            MessageColor.toString(config.getOrDefault("inventory-title", "Chest mode"))
        );
 
        final Set<Entry<String, Object>> chestModes = modes.values().entrySet();
        final List<ChestMode> listChestModes = new ArrayList<>();

        for (final Entry<String, Object> mode : chestModes) {
            if (!(mode.getValue() instanceof Map map)) {
                continue;
            }
            @SuppressWarnings("unchecked")
            final ConfigSection modeSection = new ConfigSection(map);
            final String modeName = mode.getKey();

            final ChestItem[] chestItems = getItems(modeSection, modeName);

            if (chestItems == null) {
                logger.warning("Can't found any item in the mode " + modeName + ". File: chest.yml");
                continue;
            }

            int itemsPerChest = modeSection.getInt("items-per-chest");
            if (itemsPerChest <= 0) {
                logger.warning("items-per-chest is 0, in the mode mode " + mode + ". File: chest.yml");
                itemsPerChest = chestItems.length;    
            }

            final Inventory modeInventory = buildPreviewItemInventory(inventory, chestItems, backItem, nextItem, backSlot, nextSlot, modeName);

            final int inventorySlot = modeSection.getInt("slot");
            inventory.setItem(inventorySlot, itemDeserializer.buildItem(modeSection, modeName));

            listChestModes.add(new ChestMode(chestItems, itemsPerChest, inventorySlot, modeInventory, modeName));
        }

        gui.setModes(listChestModes.toArray(new ChestMode[0]));

        return inventory;
    }

    private ChestItem[] getItems(final ConfigSection section, final String mode) {
        final List<String> items = section.getStringList("items");
        if (items == null) {
            return null;
        }
        final ChestItem[] chestItems = new ChestItem[items.size()];
        final ChestRefillCSVDeserializer csvDeserializer = new ChestRefillCSVDeserializer(logger, mode);
        int i = 0;

        for (final String item : items) {
            chestItems[i++] = csvDeserializer.deserialize(item);
        }
        return chestItems;
    }

    private Inventory buildPreviewItemInventory(
        final Inventory mainInventory,
        final ChestItem[] items,
        final ItemStack backItem,
        final ItemStack nextItem,
        final int backSlot,
        final int nextSlot,
        final String inventoryTitle
    ) {
        final ChestPreviewItemsGui chestPreviewItemsGui = new ChestPreviewItemsGui(backSlot, nextSlot, 0, mainInventory);
        final Inventory inventory = Bukkit.createInventory(chestPreviewItemsGui, 54, inventoryTitle);
        inventory.setItem(backSlot, backItem);
        final int amountItems = items.length;

        if (amountItems <= 45) {
            setItems(items, 0, amountItems, inventory);
            return inventory;
        }

        inventory.setItem(nextSlot, nextItem);
        setItems(items, 0, 45, inventory);

        final Inventory[] previewInventories = new Inventory[(amountItems % 45 == 0) ? amountItems / 45 : (amountItems / 45) + 1];
        previewInventories[0] = inventory;

        chestPreviewItemsGui.setInventories(previewInventories);

        int itemsIndex = 45;
        int inventoryIndex = 1;
    
        do {
            final ChestPreviewItemsGui previewPage = new ChestPreviewItemsGui(backSlot, nextSlot, inventoryIndex, mainInventory);
            final Inventory previewInventory = Bukkit.createInventory(previewPage, 54, inventoryTitle);

            final int remainItems = amountItems - itemsIndex;
            final int endIndex = (remainItems > 45) ? itemsIndex + 45 : amountItems;

            setItems(items, itemsIndex, endIndex, previewInventory);

            previewPage.setInventories(previewInventories);
            previewInventories[inventoryIndex++] = previewInventory;

            previewInventory.setItem(backSlot, backItem);

            itemsIndex = endIndex;

            if (itemsIndex < amountItems) {
                previewInventory.setItem(nextSlot, nextItem);
            }
        } while (itemsIndex != amountItems); 

        return inventory;
    }

    private void setItems(final ChestItem[] items, final int startIndex, final int endIndex, final Inventory inventory) {
        int slot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            final ItemStack item = items[i].item().clone();
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e" + items[i].probability() + "%");
            item.setItemMeta(meta);
            inventory.setItem(slot++, item);
        }
    }
}