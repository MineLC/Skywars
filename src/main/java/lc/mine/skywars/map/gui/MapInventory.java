package lc.mine.skywars.map.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.utils.ClickableInventory;

public final class MapInventory implements ClickableInventory {

    private final GameManager gameManager;

    public MapInventory(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        final int slot = event.getSlot();
        if (slot >= gameManager.getGames().length) {
            return;
        }
        gameManager.join((Player)event.getWhoClicked(), gameManager.getGames()[slot]);
    }
}
