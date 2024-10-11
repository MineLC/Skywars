package lc.mine.skywars.game.states.timer;

import java.util.Set;

import org.bukkit.World;
import org.bukkit.entity.Player;

import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.database.SkywarsDatabase;
import lc.mine.skywars.database.User;
import lc.mine.skywars.game.PlayerInGame;
import lc.mine.skywars.game.SkywarsGame;
import lc.mine.skywars.game.cage.GameCage;
import lc.mine.skywars.game.chestrefill.ChestMode;
import lc.mine.skywars.game.chestrefill.ChestRefillConfig;
import lc.mine.skywars.game.kit.KitAdder;
import lc.mine.skywars.game.kit.KitConfig;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.game.states.GameStatesConfig;
import lc.mine.skywars.game.top.TopManager;
import lc.mine.skywars.map.MapSpawn;

final class PregameTimer {
    private final GameStatesConfig.PreGame pregameConfig;
    private final KitConfig kitConfig;
    private final ChestRefillConfig chestRefillConfig;
    private final TopManager topManager;
    
    PregameTimer(TopManager topManager, GameStatesConfig.PreGame pregameConfig, KitConfig kitConfig, ChestRefillConfig chestRefillConfig) {
        this.topManager = topManager;
        this.pregameConfig = pregameConfig;
        this.kitConfig = kitConfig;
        this.chestRefillConfig = chestRefillConfig;
    }

    public void tickGame(final SkywarsGame game) {
        if (game.getPlayers().size() < pregameConfig.getMinPlayers()) {
            return;
        }

        if (game.getPregameCountdown() - 1 > 0) {
            tickPregame(game);
            return;
        }
        startGame(game);
    }

    private void tickPregame(final SkywarsGame game) {
        final Set<PlayerInGame> players = game.getPlayers();

        final int countdown = game.getPregameCountdown()-1;
        game.setPregameCountdown(countdown);

        if (game.getPregameCountdown() > pregameConfig.getSpamTime()) {
            for (final PlayerInGame playerInGame : players) {
                playerInGame.getPlayer().setLevel(countdown);
            }
            return;
        }

        final boolean enableSpamSound = pregameConfig.getSpamSound() != null;
        for (final PlayerInGame playerInGame : players) {
            if (enableSpamSound) {
                pregameConfig.getSpamSound().send(playerInGame.getPlayer());
            }
            playerInGame.getPlayer().setLevel(countdown);
        }
        return;
    }

    private void startGame(final SkywarsGame game) {
        final Set<PlayerInGame> players = game.getPlayers();

        final MapSpawn[] spawns = game.getMap().spawns();
        final World world = game.getMap().world();
        for (final MapSpawn spawn : spawns) {
            if (spawn.playerUsingIt != null) {
                GameCage.delete(world, spawn.x, spawn.y, spawn.z);
                spawn.playerUsingIt = null;
            }
        }

        final ChestMode[] modes = chestRefillConfig.getModes();
        final int[] chestModeVotes = new int[modes.length];
        for (final PlayerInGame playerInGame : players) {
            final Player player = playerInGame.getPlayer();
            startForPlayer(player, chestModeVotes);
        }

        game.setChestMode(getModeWithMoreVotes(modes, chestModeVotes));
        Messages.sendNoGet(players, Messages.get("game-started").replace("%chest%", game.getChestMode().getName()));
        game.setState(GameState.IN_GAME);
    }

    private void startForPlayer(final Player player, final int[] chestVotes) {
        player.setNoDamageTicks(5*20);

        final User data = SkywarsDatabase.getDatabase().getCached(player.getUniqueId());
        data.played++;
        topManager.calculatePlayed(data, player.getName());    
    
        if (data.chestRefillVoteIndex != -1) {
            chestVotes[data.chestRefillVoteIndex]++;
        }
        player.closeInventory();

        if (data.selectedKit == null) {
            KitAdder.add(player, kitConfig.getDefaultKit());    
            return;
        }
        KitAdder.add(player, data.selectedKit);
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