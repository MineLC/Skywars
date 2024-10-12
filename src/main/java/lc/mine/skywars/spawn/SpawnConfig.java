package lc.mine.skywars.spawn;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import lc.mine.skywars.config.item.InventoryItem;
import lc.mine.skywars.sidebar.SpawnSidebar;

public class SpawnConfig {

    Location spawn;
    InventoryItem kitSelectorItem, chestModeItem, cagesSelectorItem, topsItem, mapSelectorItem;

    public Location getSpawn() {
        return spawn;
    }
    public InventoryItem getKitSelectorItem() {
        return kitSelectorItem;
    }
    public InventoryItem getCagesSelectorItem() {
        return cagesSelectorItem;
    }
    public InventoryItem getChestModeItem() {
        return chestModeItem;
    }
    public InventoryItem getMapSelectorItem() {
        return mapSelectorItem;
    }
    public InventoryItem getTopsItem() {
        return topsItem;
    }

    public void sendPregameItems(final Player player) {
        final PlayerInventory inventory = player.getInventory(); 
        inventory.clear();
        inventory.setItem(kitSelectorItem.slot(), kitSelectorItem.item());
        inventory.setItem(topsItem.slot(), topsItem.item());
        inventory.setItem(cagesSelectorItem.slot(), cagesSelectorItem.item());
        inventory.setItem(chestModeItem.slot(), chestModeItem.item());
    }

    public void sendToSpawn(final Player player, final SpawnSidebar spawnSidebar) {
        if (player.hasPermission("vip")) {
            player.setAllowFlight(true);
        }
        player.setHealth(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(spawn);
        giveSpawnItems(player);
        if (spawnSidebar != null) {
            spawnSidebar.sendSpawnSidebar(player);
        }
    }

    public void giveSpawnItems(final Player player) {
        final PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(kitSelectorItem.slot(), kitSelectorItem.item());
        inventory.setItem(mapSelectorItem.slot(), mapSelectorItem.item());
        inventory.setItem(topsItem.slot(), topsItem.item());
        inventory.setItem(cagesSelectorItem.slot(), cagesSelectorItem.item());
    }
}
