package lc.mine.skywars.game.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.nms.v1_8R3.Sidebar1_8R3;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.map.GameMap;
import lc.mine.skywars.utils.TimeUtil;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class InGameTask implements Runnable {

    private final GameMap map;

    private final LongOpenHashSet chestsInCooldown;

    private int nextRefill;

    public InGameTask(GameMap map, LongOpenHashSet chestsInCooldown) {
        this.map = map;
        this.nextRefill = 300;
        this.chestsInCooldown = chestsInCooldown;
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(SkywarsPlugin.getInstance(), this, 0, 20);
    }

    @Override
    public void run() {
        if (--nextRefill <= 0) {
            nextRefill = 300;
            chestsInCooldown.clear();
            Bukkit.broadcastMessage(Messages.get("chest-refill"));
        }

        final Sidebar1_8R3 sidebar = new Sidebar1_8R3();
        final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
        sidebar.setTitle("§6§lSkywars");

        int amountPlayerLiving = 0;
        for (final EntityPlayer player : players) {
            if (player.playerInteractManager.getGameMode() != EnumGamemode.SPECTATOR) {
                amountPlayerLiving++;
            }
        }
        final String refill = TimeUtil.getMinutesAndSeconds(nextRefill);
        for (final EntityPlayer player : players) {
            final Player bukkitPlayer = player.getBukkitEntity();
            sidebar.setLines(sidebar.createLines(new String[]{
                "",
                "§fRefill: §e" + refill,
                "",
                "§fAsesinatos: §c" + SkywarsPlugin.getInstance().getManager().getDatabase().getCached(player.getUniqueID()).kills,
                "§fJugadores: §a" + amountPlayerLiving,
                "",
                "§fMapa: " + map.displayName() ,
                "",
                "§bplay.mine.lc"
            }));
            sidebar.sendLines(bukkitPlayer);
            sidebar.sendTitle(bukkitPlayer);
        }
    }
}