package lc.mine.skywars.game.cage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lc.mine.skywars.game.cage.CageInventory.Cage;
import lc.mine.skywars.spawn.SpawnConfig;

public class CageInventoryBuilder {
    
    public Inventory buildInventories(final SpawnConfig spawnConfig) {

        final Material[] availableCagesMaterials = new Material[] {
            Material.STONE,
            Material.GRASS,
            Material.DIRT,
            Material.COBBLESTONE,
            Material.WOOD,
            Material.BEDROCK,
            Material.GOLD_ORE,
            Material.IRON_ORE,
            Material.COAL_ORE,
            Material.LOG,
            Material.SPONGE,
            Material.GLASS,
            Material.LAPIS_ORE,
            Material.LAPIS_BLOCK,
            Material.SANDSTONE,
            Material.NOTE_BLOCK,
            Material.PISTON_STICKY_BASE,
            Material.PISTON_BASE,
            Material.WOOL,
            Material.GOLD_BLOCK,
            Material.IRON_BLOCK,
            Material.BRICK,
            Material.TNT,
            Material.BOOKSHELF,
            Material.MOSSY_COBBLESTONE,
            Material.OBSIDIAN,
            Material.MOB_SPAWNER,
            Material.WOOD_STAIRS,
            Material.DIAMOND_ORE,
            Material.DIAMOND_BLOCK,
            Material.WORKBENCH,
            Material.FURNACE,
            Material.COBBLESTONE_STAIRS,
            Material.REDSTONE_ORE,
            Material.ICE,
            Material.SNOW_BLOCK,
            Material.JUKEBOX,
            Material.PUMPKIN,
            Material.NETHERRACK,
            Material.SOUL_SAND,
            Material.GLOWSTONE,
            Material.JACK_O_LANTERN,
            Material.STAINED_GLASS,
            Material.MONSTER_EGGS,
            Material.SMOOTH_BRICK,
            Material.HUGE_MUSHROOM_1,
            Material.HUGE_MUSHROOM_2,
            Material.IRON_FENCE,
            Material.THIN_GLASS,
            Material.MELON_BLOCK,
            Material.BRICK_STAIRS,
            Material.SMOOTH_STAIRS,
            Material.MYCEL,
            Material.NETHER_BRICK,
            Material.NETHER_BRICK_STAIRS,
            Material.ENDER_STONE,
            Material.SANDSTONE_STAIRS,
            Material.EMERALD_ORE,
            Material.EMERALD_BLOCK,
            Material.SPRUCE_WOOD_STAIRS,
            Material.BIRCH_WOOD_STAIRS,
            Material.JUNGLE_WOOD_STAIRS,
            Material.COMMAND,
            Material.BEACON,
            Material.REDSTONE_BLOCK,
            Material.QUARTZ_ORE,
            Material.QUARTZ_BLOCK,
            Material.QUARTZ_STAIRS,
            Material.DROPPER,
            Material.STAINED_CLAY,
            Material.STAINED_GLASS_PANE,
            Material.LOG_2,
            Material.ACACIA_STAIRS,
            Material.DARK_OAK_STAIRS,
            Material.SLIME_BLOCK,
            Material.BARRIER,
            Material.PRISMARINE,
            Material.SEA_LANTERN,
            Material.HAY_BLOCK,
            Material.HARD_CLAY,
            Material.COAL_BLOCK,
            Material.PACKED_ICE,
            Material.RED_SANDSTONE,
            Material.RED_SANDSTONE_STAIRS
        };
        
        int amountItems = availableCagesMaterials.length;
        int itemsIndex = 0;
        int inventoryIndex = 0;

        final Inventory[] previewInventories = new Inventory[(amountItems % 45 == 0) ? amountItems / 45 : (amountItems / 45) + 1];

        final ItemStack backItem = new ItemStack(Material.ARROW);
        final ItemMeta meta = backItem.getItemMeta();
        meta.setDisplayName("§cVolver");
        backItem.setItemMeta(meta);
        
        final ItemStack nextItem = new ItemStack(Material.EMERALD);
        final ItemMeta nextmeta = backItem.getItemMeta();
        nextmeta.setDisplayName("§aSiguiente página");
        nextItem.setItemMeta(nextmeta);

        do {
            final int remainItems = amountItems - itemsIndex;
            final int endIndex = (remainItems > 45) ? itemsIndex + 45 : amountItems;

            final Cage[] cages = new Cage[(remainItems > 45) ? 45 : remainItems];

            final CageInventory previewPage = new CageInventory(inventoryIndex, spawnConfig, cages);
            final Inventory previewInventory = Bukkit.createInventory(previewPage, 54, "Seleciona tu caja");

            setItems(availableCagesMaterials, itemsIndex, endIndex, previewInventory, cages);

            previewPage.setInventories(previewInventories);
            if (inventoryIndex != 0) {
                previewInventory.setItem(45, backItem);
            }
            previewInventories[inventoryIndex++] = previewInventory;

            itemsIndex = endIndex;

            if (itemsIndex < amountItems) {
                previewInventory.setItem(53, nextItem);
            }
        } while (itemsIndex != amountItems); 
        return previewInventories[0];
    }

    private void setItems(final Material[] items, final int startIndex, final int endIndex, final Inventory inventory, final Cage[] cages) {
        int slot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            final ItemStack item = new ItemStack(items[i]);
            final ItemMeta meta = item.getItemMeta();
            String permission, permissionMessage;
            if (i <= 20) {
                meta.setDisplayName("§a§lVIP");
                permission = "vip";
                permissionMessage = "§fNecesitas el rango §a§lVIP §fO superior";
            } else if (i <= 40) {
                meta.setDisplayName("§a§lSVIP");
                permission = "svip";
                permissionMessage = "§fNecesitas el rango §a§lSVIP §fO superior";
            } else if (i <= 60) {
                meta.setDisplayName("§6§lELITE");
                permission = "elite";
                permissionMessage = "§fNecesitas el rango §6§lELITE §fO superior";
            } else {
                meta.setDisplayName("§c§lOPTIMUM");
                permission = "optimum";
                permissionMessage = "§fNecesitas el rango §c§lOPTIMUM §fO superior";
            }
            item.setItemMeta(meta);
            cages[slot] = new Cage(permission, items[i], permissionMessage);

            inventory.setItem(slot++, item);
        }
    }
}
