package lc.mine.skywars.game.listener;

import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.spawn.SpawnConfig;
import org.bukkit.inventory.ItemStack;

public class CancelEventListener implements Listener {

    private final GameManager gameManager;
    private final SpawnConfig spawnConfig;
    private final ConfigManager configManager;

    public CancelEventListener(GameManager gameManager, SpawnConfig spawnConfig, ConfigManager configManager) {
        this.gameManager = gameManager;
        this.spawnConfig = spawnConfig;
        this.configManager = configManager;
    }

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {

            final User user = SkywarsDatabase.getDatabase().getCached(player.getUniqueId());

            if (user.activeChallenges.contains(configManager.getChallengeConfig().getUhc())) {


                if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED ||
                        event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
                    event.setCancelled(true);
                }

                if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC ||
                        event.getRegainReason() == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN) {
                    return;
                }

                if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING) {
                    ItemStack itemInHand = player.getItemInHand();
                    if (itemInHand != null &&
                            (itemInHand.getType() == Material.GOLDEN_APPLE ||
                                    itemInHand.getType() == Material.GOLDEN_APPLE)) {
                        return;
                    }
                }

                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void breakBlock(final BlockBreakEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null || game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerHunger(final FoodLevelChangeEvent event) {
        if (event.getEntity().getWorld().equals(spawnConfig.getSpawn().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        } 
        final SkywarsGame game = gameManager.getGame(player);
        if (game == null || game.getState() != GameState.IN_GAME || player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            return;
        }
        if (event.getCause() == DamageCause.FALL) {
            event.setDamage(event.getDamage() / 2);
            return;
        }
        if (event.getCause() == DamageCause.VOID) {
            event.setDamage(player.getMaxHealth());
            return;
        }
    }

    @EventHandler
    public void dropItem(final PlayerDropItemEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if (game == null || game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void placeBlock(final BlockPlaceEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getPlayer());
        if(game != null && game.getState() == GameState.IN_GAME){
            final User user = SkywarsDatabase.getDatabase().getCached(event.getPlayer().getUniqueId());
            if(user.activeChallenges.contains(configManager.getChallengeConfig().getWithoutBlocks())){
                event.setCancelled(true);
                event.setBuild(false);
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WITHER_HURT, 1f, 1f);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio sin colocar bloques!"));
                return;
            }
        }
        if (game == null || game.getState() == GameState.PREGAME) {
            event.setCancelled(true);
            event.setBuild(false);
        }
    }
}
