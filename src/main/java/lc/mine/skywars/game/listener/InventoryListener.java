package lc.mine.skywars.game.listener;

import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.chestrefill.creator.ChestInventoryCreator;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.map.gui.MapInventoryBuilder;
import lc.mine.skywars.spawn.SpawnConfig;
import lc.mine.skywars.utils.ClickableInventory;
import lc.mine.skywars.utils.IntegerUtil;

public final class InventoryListener implements Listener {

    private final ConfigManager configManager;
    private final GameManager gameManager;

    public InventoryListener(GameManager gameManager, ConfigManager configManager) {
        this.gameManager = gameManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onClickInventory(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !(event.getInventory().getHolder() instanceof ClickableInventory clickableInventory)) {
            return;
        }
        event.setCancelled(true);
        clickableInventory.onClick(event);
    }

    @EventHandler
    public void onChestInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.CHEST) {
            return;
        }
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null) {
            return;
        }

        final LongOpenHashSet chestsInCooldown = game.getChestsInCooldown();
        final Location location = event.getClickedBlock().getLocation();
        final long key = IntegerUtil.compress((int)location.getX(), (int)location.getY(), (int)location.getZ(), false);

        if (chestsInCooldown.contains(key)) {
            return;
        }

        final User user = SkywarsDatabase.getDatabase().getCached(event.getPlayer().getUniqueId());

        if(user.activeChallenges.contains(configManager.getChallengeConfig().getWithoutChests())){
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio sin abrir cofres!"));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WITHER_HURT, 1f, 1f);
            return;
        }

        ChestInventoryCreator.setItems(((Chest)event.getClickedBlock().getState()).getBlockInventory(), game.getChestMode());
        chestsInCooldown.add(key);
    }

    @EventHandler
    public void onItemInteract(final PlayerInteractEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null) {
            event.setCancelled(true);
            final Material material = event.getMaterial();
            if (interactUniversalItems(material, event.getPlayer())) {
                return;
            }
            interactSpawnItems(material, event.getPlayer());
            return;
        }
        if (game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
            final Material material = event.getMaterial();
            if (interactUniversalItems(event.getMaterial(), event.getPlayer())) {
                return;
            }
            interactPregameItems(material, event.getPlayer());
        }
    }

    private boolean interactUniversalItems(final Material material, final Player player) {
        final SpawnConfig spawn = configManager.getSpawnConfig();
        if (material == spawn.getCagesSelectorItem().cachedMaterial()) {
            player.openInventory(configManager.getCageInventory());
            return true;
        }
        if (material == spawn.getKitSelectorItem().cachedMaterial()) {
            player.openInventory(configManager.getKitsConfig().getInventory());
            return true;
        }
        if (material == spawn.getTopsItem().cachedMaterial()) {
            configManager.getTopConfig().getTopInventoryBuilder().buildMainInventory(player);
            return true;
        }
        return false;
    }

    private void interactPregameItems(final Material material, final Player player) {
        if (material == configManager.getSpawnConfig().getChestModeItem().cachedMaterial()) {
            player.openInventory(configManager.getChestRefillConfig().getInventory());
        }
        if(material == configManager.getSpawnConfig().getChallengeItem().cachedMaterial()){
            configManager.getChallengeConfig().getChallengeInventoryBuilder().buildMainInventory(player);
        }
    }

    private void interactSpawnItems(final Material material, final Player player) {
        if (material == configManager.getSpawnConfig().getMapSelectorItem().cachedMaterial()) {
            new MapInventoryBuilder().build(player, gameManager);
        }
    }
}