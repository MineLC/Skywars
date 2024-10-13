package lc.mine.skywars.game.kit.gui;

import lc.mine.skywars.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import lc.mine.core.CorePlugin;
import lc.mine.core.database.PlayerData;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.kit.Kit;
import lc.mine.skywars.utils.ClickableInventory;

public final class KitInventory implements ClickableInventory {

    private final Kit[] kits;
    private final CorePlugin corePlugin;
    private final ConfigManager configManager;

    public KitInventory(Kit[] kits, CorePlugin corePlugin, ConfigManager configManager) {
        this.kits = kits;
        this.corePlugin = corePlugin;
        this.configManager = configManager;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final int slot = event.getSlot();
        final HumanEntity entity = event.getWhoClicked();
        event.setCancelled(true);

        for (final Kit kit : kits) {
            if (kit.inventorySlot() != slot) {
                continue;
            }
            event.setCancelled(true);

            final User user = SkywarsDatabase.getDatabase().getCached(entity.getUniqueId());

            if(user.activeChallenges.contains(configManager.getChallengeConfig().getNoob())){
                event.setCancelled(true);
                entity.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c¡Estás cumpliendo donde solo empezarás con una espada de piedra!"));
                ((Player) entity).playSound(entity.getLocation(), Sound.WITHER_HURT, 1f, 1f);
                return;
            }

            if (kit.permission() != null && !entity.hasPermission(kit.permission())) {
                entity.sendMessage(kit.noPermissionMessage());   
                return;
            }

            final User skywarsUser = SkywarsDatabase.getDatabase().getCached(entity.getUniqueId());

            if (kit.cost() > 0 && !skywarsUser.kits.contains(kit.name())) {
                PlayerData data = corePlugin.getData().getCached(entity.getUniqueId());                   

                if (data.getLcoins() < kit.cost()) {
                    Messages.send(entity, "no-money");
                    return;
                }

                data.setLcoins(data.getLcoins() - kit.cost());
                skywarsUser.kits.add(kit.name());
            }

            skywarsUser.selectedKit = kit;
            entity.sendMessage(Messages.get("selected-kit").replace("%kit%", kit.name()));
            return;
        }
    }
}