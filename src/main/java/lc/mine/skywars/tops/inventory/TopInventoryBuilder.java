package lc.mine.skywars.tops.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lc.mine.skywars.tops.Top;
import lc.mine.skywars.utils.ClickableInventory;
import lc.mine.skywars.utils.IntegerUtil;

public class TopInventoryBuilder {
        
    private static final ClickableInventory TOP_INVENTORY_HOLDER = new ClickableInventory() {
        @Override
        public void onClick(InventoryClickEvent event) {
            // Add something xd
        }
    };

    public static void build(Player player, Top top, String title) {
        final int amountTops = top.getPlayers().length;
        Inventory inventory = Bukkit.createInventory(TOP_INVENTORY_HOLDER, IntegerUtil.calculateRows(amountTops), title);

        for (int i = 0; i < amountTops; i++) {
            Material material;
            Top.Player topPlayer = top.getPlayers()[i];
            if (topPlayer == null) {
                break; 
            }
            int topPos = i + 1;
            switch (topPos) {
            case 1:
                material = Material.DIAMOND_BLOCK;
                break;
            case 2:
                material = Material.GOLD_BLOCK;
                break;
            case 3:
                material = Material.IRON_BLOCK;
                break;
            default:
                if (topPos <= 10) {
                material = Material.REDSTONE_BLOCK;
                break;
                } 
                material = Material.COAL;
                break;
            } 
            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("§b#" + topPos + " §7- §e" + topPlayer.name + " §7 -> §a" + topPlayer.value);
            itemStack.setItemMeta(meta);
            inventory.setItem(i, itemStack);
        } 
        player.openInventory(inventory);
    }
}