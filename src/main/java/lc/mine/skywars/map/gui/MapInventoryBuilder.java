package lc.mine.skywars.map.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.utils.TimeUtil;

public final class MapInventoryBuilder {

    public void build(final Player player, final GameManager gameManager) {
        final SkywarsGame[] games = gameManager.getGames();
        final Inventory inventory = Bukkit.createInventory(new MapInventory(gameManager), 54, "Mapas disponibles");
        int slot = 0;
        for (final SkywarsGame game : games) {
            Material material = Material.COAL_BLOCK;
            List<String> lore = List.of();
            String name = game.getMap().getDisplayName();
            switch (game.getState()) {
                case PREGAME:
                    name += " §8- §eComenzando";
                    material = Material.GOLD_BLOCK;
                    lore = List.of(
                        "§fJugadores: §b" + game.getPlayers().size() + "§7/§3" + game.getMap().getMaxPlayers(),
                        "§fInicia en: §6" + game.getPregameCountdown(),
                        "",
                        "§aClic para jugar"
                    );
                    break;
                case IN_GAME:
                    name += " §8- §cEn juego";
                    material = Material.REDSTONE_BLOCK;
                    lore = List.of(
                        "§fJugadores: §b" + game.getPlayers().size() + "§7/§3" + game.getMap().getMaxPlayers(),
                        "§fComenzo hace: §c" + TimeUtil.getMinutesAndSeconds(game.getTicks()),
                        "",
                        "§dClic para espectear"
                    );
                    break;
                case UNSTARTED:
                    name += " §8- §bSin empezar";
                    material = Material.COAL_BLOCK;
                    lore = List.of(
                        "§fJugadores: §b0§7/§3" + game.getMap().getMaxPlayers(),
                        "",
                        "§bClic para empezar la partida"
                    );
                    break;
                case FINISH:
                    name += " §8- §cFinalizando";
                    material = Material.EMERALD_BLOCK;
                    lore = List.of(
                        "§fJugadores: §b" + game.getPlayers().size() + "§7/§3" + game.getMap().getMaxPlayers(),
                        "§fComenzo hace: §c" + TimeUtil.getMinutesAndSeconds(game.getTicks()),
                        "",
                        "§cEsta partida ya está terminando"
                    );
                    break;
            }
            final ItemStack item = new ItemStack(material);
            final ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(slot++, item);
        }
        player.openInventory(inventory);
    }
}