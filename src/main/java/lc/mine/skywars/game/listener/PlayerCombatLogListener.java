package lc.mine.skywars.game.listener;

import lc.mine.skywars.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import lc.mine.combatlog.events.PlayerCombatLogEvent;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.GameManager;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.top.TopManager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class PlayerCombatLogListener implements Listener {
    
    private final GameManager gameManager;
    private final TopManager topManager;
    private final ConfigManager configManager;
    public PlayerCombatLogListener(GameManager gameManager, TopManager topManager, ConfigManager configManager) {
        this.topManager = topManager;
        this.gameManager = gameManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerCombatLog(final PlayerCombatLogEvent event) {
        final SkywarsGame game = gameManager.getGame(event.getVictim());
        if (game == null) {
            return;
        }

        final User victimData = SkywarsDatabase.getDatabase().getCached(event.getVictim().getUniqueId());
        victimData.deaths++;
        topManager.calculateDeaths(victimData, event.getVictim().getName());
        if (event.getKiller() != null) {
            final User killerData = SkywarsDatabase.getDatabase().getCached(event.getKiller().getUniqueId());
            killerData.kills++;
            topManager.calculateKills(killerData, event.getKiller().getName());
        }

        Messages.sendNoGet(game.getPlayers(), event.getDeathMessage());
        event.setCancelDeathMessage(true);
    }

    @EventHandler
    public void onDamageByEntity(final EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player vic) {
            if (e.getDamager() instanceof Player dam) {
                final User user = SkywarsDatabase.getDatabase().getCached(dam.getUniqueId());

                if (user.activeChallenges.contains(configManager.getChallengeConfig().getArcher())) {
                    e.setCancelled(true);
                    e.setDamage(0);
                    dam.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio, solo dañas con las flechas de tu arco!"));
                    dam.playSound(dam.getLocation(), Sound.WITHER_HURT, 1f, 1f);
                    return;
                }
                if(user.activeChallenges.contains(configManager.getChallengeConfig().getDefinitiveWarrior())){
                    if(dam.getItemInHand().getType() != Material.STONE_SWORD){
                        e.setCancelled(true);
                        e.setDamage(0);
                        dam.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio, no puedes usar otra arma que no sea la espada de piedra!"));
                        dam.playSound(dam.getLocation(), Sound.WITHER_HURT, 1f, 1f);
                        return;
                    }
                }
            }
            if(e.getDamager() instanceof Snowball sn){
                if(!(sn.getShooter() instanceof Player)) return;
                final User user = SkywarsDatabase.getDatabase().getCached(((Player) sn.getShooter()).getUniqueId());
                if(user.activeChallenges.contains(configManager.getChallengeConfig().getDefinitiveWarrior())) {
                    e.setCancelled(true);
                    e.setDamage(0);
                    ((Player) sn.getShooter()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio, no puedes usar otra arma que no sea la espada de piedra!"));
                    ((Player) sn.getShooter()).playSound(((Player) sn.getShooter()).getLocation(), Sound.WITHER_HURT, 1f, 1f);
                    return;
                }
                if (user.activeChallenges.contains(configManager.getChallengeConfig().getArcher())) {
                    e.setCancelled(true);
                    e.setDamage(0);
                    ((Player) sn.getShooter()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio, solo dañas con las flechas de tu arco!"));
                    ((Player) sn.getShooter()).playSound(((Player) sn.getShooter()).getLocation(), Sound.WITHER_HURT, 1f, 1f);
                    return;
                }
            }
            if(e.getDamager() instanceof Arrow arrow){
                if(!(arrow.getShooter() instanceof Player)) return;
                final User user = SkywarsDatabase.getDatabase().getCached(((Player) arrow.getShooter()).getUniqueId());

                if(user.activeChallenges.contains(configManager.getChallengeConfig().getDefinitiveWarrior())) {
                    e.setCancelled(true);
                    e.setDamage(0);
                    ((Player) arrow.getShooter()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio, no puedes usar otra arma que no sea la espada de piedra!"));
                    ((Player) arrow.getShooter()).playSound(((Player) arrow.getShooter()).getLocation(), Sound.WITHER_HURT, 1f, 1f);
                    return;
                }
            }
            if(e.getDamager() instanceof Egg egg){
                if(!(egg.getShooter() instanceof Player)) return;
                final User user = SkywarsDatabase.getDatabase().getCached(((Player) egg.getShooter()).getUniqueId());

                if(user.activeChallenges.contains(configManager.getChallengeConfig().getDefinitiveWarrior())) {
                    e.setCancelled(true);
                    e.setDamage(0);
                    ((Player) egg.getShooter()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio, no puedes usar otra arma que no sea la espada de piedra!"));
                    ((Player) egg.getShooter()).playSound(((Player) egg.getShooter()).getLocation(), Sound.WITHER_HURT, 1f, 1f);
                    return;
                }

                if (user.activeChallenges.contains(configManager.getChallengeConfig().getArcher())) {
                    e.setCancelled(true);
                    e.setDamage(0);
                    ((Player) egg.getShooter()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo un desafio, solo dañas con las flechas de tu arco!"));
                    ((Player) egg.getShooter()).playSound(((Player) egg.getShooter()).getLocation(), Sound.WITHER_HURT, 1f, 1f);
                    return;
                }
            }
        }
    }
}
