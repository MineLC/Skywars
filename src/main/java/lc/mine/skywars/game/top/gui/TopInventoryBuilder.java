package lc.mine.skywars.game.top.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lc.mine.core.CorePlugin;
import lc.mine.core.database.PlayerData;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.top.Top;
import lc.mine.skywars.game.top.TopPlayer;
import lc.mine.skywars.game.top.TopConfig;

public final class TopInventoryBuilder {

    private final TopConfig topConfig;

    private final CorePlugin corePlugin;

    private final TopSecundaryInventory topInventory;
    private final TopMainInventory mainInventory;

    public TopInventoryBuilder(TopConfig topConfig, CorePlugin corePlugin) {
        this.topConfig = topConfig;
        this.corePlugin = corePlugin;
        this.mainInventory = new TopMainInventory(this, topConfig);
        this.topInventory = new TopSecundaryInventory(45, this);
    }

    public void buildMainInventory(final HumanEntity entity) {
        final User user = SkywarsDatabase.getDatabase().getCached(entity.getUniqueId());
        final PlayerData data = corePlugin.getData().getCached(entity.getUniqueId());

        final Inventory inventory = Bukkit.createInventory(mainInventory, 54, "Tops");

        inventory.setItem(topConfig.getDeathSelector().getInventorySlot(), topConfig.getDeathSelector().getItemInventory());
        inventory.setItem(topConfig.getKillSelector().getInventorySlot(), topConfig.getKillSelector().getItemInventory());
        inventory.setItem(topConfig.getWinSelector().getInventorySlot(), topConfig.getWinSelector().getItemInventory());
        inventory.setItem(topConfig.getPlayedSelector().getInventorySlot(), topConfig.getPlayedSelector().getItemInventory());

        final ItemStack statsItem = new ItemStack(Material.PAPER);
        final ItemMeta meta = statsItem.getItemMeta();
        meta.setDisplayName("§bEstadísticas");
        meta.setLore(List.of(
            "§fKills: §c" + user.kills,
            "§fMuertes: §4" + user.deaths,
            "§fVictorias: §a" + user.wins,
            "§fPartidas jugadas: §d" + user.played,
            "",
            "§fLCoins: §6" + data.getLcoins(),
            "§fVipPoints: §b" + data.getVipPoins()
        ));
        statsItem.setItemMeta(meta);
        inventory.setItem(40, statsItem);

        entity.openInventory(inventory);
    }

    public void buildTop(final HumanEntity entity, final Top top, final String inventoryTitle) {
        final Inventory inventory = Bukkit.createInventory(topInventory, 54, inventoryTitle);

        int slot = 0;
        for (final TopPlayer player : top.getPlayers()) {
            if (player == null) {
                continue;
            }
            Material material;
            if (slot == 0) {
                material = Material.DIAMOND_BLOCK;
            } else if (slot == 1) {
                material = Material.GOLD_BLOCK;
            } else if (slot == 2) {
                material = Material.IRON_BLOCK;
            } else if (slot < 9) {
                material = Material.REDSTONE_BLOCK;
            } else if (slot < 19) {
                material = Material.COAL_BLOCK;
            } else {
                material = Material.LOG;
            }

            final ItemStack itemStack = new ItemStack(material);
            final ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName("§6§l#"+(slot+1) + " §e"+player.name + "§7 -> " + "§f" + player.score);
            itemStack.setItemMeta(meta);
            inventory.setItem(slot++, itemStack);
        }

        final ItemStack returnItem = new ItemStack(Material.ARROW);
        final ItemMeta meta = returnItem.getItemMeta();
        meta.setDisplayName("§cVolver");
        returnItem.setItemMeta(meta);

        inventory.setItem(45, returnItem);

        entity.openInventory(inventory);        
    }
}