package lc.mine.skywars.game;

import java.util.Set;

import org.bukkit.World;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lc.mine.skywars.game.chestrefill.ChestMode;
import lc.mine.skywars.game.states.GameState;
import lc.mine.skywars.map.SkywarsMap;

public final class SkywarsGame {
    private final SkywarsMap map;
    private final Set<PlayerInGame> players;

    private GameState state = GameState.UNSTARTED;
    private final LongOpenHashSet chestsInCooldown;
    private ChestMode chestMode;

    private int nextChestRefill, pregameCountdown, ticks;
    private World world;

    public SkywarsGame(SkywarsMap map) {
        this.map = map;
        this.players = new ObjectOpenHashSet<>();
        this.chestsInCooldown = new LongOpenHashSet();
    }

    public Set<PlayerInGame> getPlayers() {
        return players;
    }

    public GameState getState() {
        return state;
    }

    public SkywarsMap getMap() {
        return map;
    }

    public int getNextChestRefill() {
        return nextChestRefill;
    }
    public void setNextChestRefill(int nextChestRefill) {
        this.nextChestRefill = nextChestRefill;
    }
    public int getPregameCountdown() {
        return pregameCountdown;
    }
    public void setPregameCountdown(int pregameCountdown) {
        this.pregameCountdown = pregameCountdown;
    }

    public int getTicks() {
        return ticks;
    }
    public void addTick() {
        this.ticks++;
    }
    public void resetTicks() {
        this.ticks = 0;
    }

    public LongOpenHashSet getChestsInCooldown() {
        return chestsInCooldown;
    }

    public ChestMode getChestMode() {
        return chestMode;
    }
    public void setChestMode(ChestMode chestMode) {
        this.chestMode = chestMode;
    }

    public void setState(GameState state) {
        this.state = state;
    }
    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }

    public boolean hasStarted() {
        return state != GameState.PREGAME && state != GameState.UNSTARTED;
    }
}