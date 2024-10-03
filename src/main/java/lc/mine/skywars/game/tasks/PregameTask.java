package lc.mine.skywars.game.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.map.GameMap;
import lc.mine.skywars.map.MapSpawn;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class PregameTask implements Runnable {

    private final int countdownTimeNeed;

    private final GameMap map;
    private final InGameTask inGameTask;

    private int remainTime;

    private int taskId;

    public PregameTask(GameMap map, InGameTask inGameTask) {
        this.countdownTimeNeed = 30;
        this.remainTime = countdownTimeNeed;
        this.map = map;
        this.inGameTask = inGameTask;
    }

    public void start() {
        this.taskId = Bukkit.getScheduler().runTaskTimer(SkywarsPlugin.getInstance(), this, 0, 20).getTaskId();
    }

    @Override
    public void run() {
        if (MinecraftServer.getServer().getPlayerList().players.size() < 2) {
            remainTime = countdownTimeNeed;
            return;
        }
        
        if (--remainTime < 10) {
            final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
            for (final EntityPlayer player : players) {
                player.getBukkitEntity().playSound(player.getBukkitEntity().getLocation(), Sound.NOTE_PLING, 1.0f, 0.65f);
            }
        }
        if (remainTime != 0) {
            return;
        }

        final MapSpawn[] spawns = map.spawns();
        final World world = map.world();
        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt != null) {
                world.getBlockAt(spawn.x, spawn.y, spawn.z).setType(Material.AIR);
            }
        }

        final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
        for (final EntityPlayer player : players) {
            player.getBukkitEntity().setNoDamageTicks(5*20);
        }
        Bukkit.broadcastMessage(Messages.get("game-started"));

        Bukkit.getScheduler().cancelTask(taskId);
        inGameTask.start();
    }
}