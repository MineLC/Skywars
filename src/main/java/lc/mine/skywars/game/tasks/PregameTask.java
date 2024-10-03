package lc.mine.skywars.game.tasks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import lc.mine.skywars.SkywarsPlugin;
import lc.mine.skywars.chestrefill.ChestMode;
import lc.mine.skywars.chestrefill.creator.ChestInventoryCreator;
import lc.mine.skywars.config.ConfigManager;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.GameState;
import lc.mine.skywars.kit.KitAdder;
import lc.mine.skywars.map.MapSpawn;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public class PregameTask implements Runnable {

    private final ConfigManager manager;
    private final int countdownTimeNeed;

    private final InGameTask inGameTask;

    private int remainTime;

    private int taskId;

    public PregameTask(ConfigManager manager, InGameTask inGameTask) {
        this.countdownTimeNeed = 30;
        this.remainTime = countdownTimeNeed;
        this.manager = manager;
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
        
        final List<EntityPlayer> players = MinecraftServer.getServer().getPlayerList().players;
        if (--remainTime < 10) {
            for (final EntityPlayer player : players) {
                player.getBukkitEntity().playSound(player.getBukkitEntity().getLocation(), Sound.NOTE_PLING, 1.0f, 0.65f);
            }
        }
        if (remainTime != 0) {
            for (final EntityPlayer player : players) {
                player.getBukkitEntity().setLevel(remainTime);
            }
            return;
        }

        final MapSpawn[] spawns = manager.getMap().spawns();
        final World world = manager.getMap().world();
        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt != null) {
                world.getBlockAt(spawn.x, spawn.y-1, spawn.z).setType(Material.AIR);
                spawn.playerUsingIt = null;
            }
        }

        final ChestMode[] modes = manager.getConfig().chestRefill.modes;
        final int[] chestModeVotes = new int[modes.length];
        for (final EntityPlayer player : players) {
            startForPlayer(player, chestModeVotes);
        }

        ChestInventoryCreator.currentMode = getModeWithMoreVotes(modes, chestModeVotes);

        Bukkit.broadcastMessage(Messages.get("game-started").replace("%chest%", ChestInventoryCreator.currentMode.getName()));
        GameState.currentState = GameState.IN_GAME;

        Bukkit.getScheduler().cancelTask(taskId);
        inGameTask.start();
    }

    private void startForPlayer(final EntityPlayer player, final int[] chestVotes) {
        final Player bukkitPlayer = player.getBukkitEntity();
        bukkitPlayer.setNoDamageTicks(5*20);

        final User data = manager.getDatabase().getCached(player.getUniqueID());

        if (data.chestRefillVoteIndex != -1) {
            chestVotes[data.chestRefillVoteIndex]++;
        }

        if (data.selectedKit == null) {
            KitAdder.add(bukkitPlayer, manager.getConfig().kits.defaultKit);    
            return;
        }
        KitAdder.add(bukkitPlayer, data.selectedKit);
    }

    private ChestMode getModeWithMoreVotes(final ChestMode[] modes, final int[] votes) {
        ChestMode modeSelected = null;
        int maxVotes = -1;
        for (int i = 0; i < votes.length; i++) {
            if (votes[i] > maxVotes) {
                modeSelected = modes[i];
                maxVotes = votes[i];
            }
        }
        return modeSelected;
    }
}