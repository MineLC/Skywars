package lc.mine.skywars.game;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class PlayerInGame {

    private final SkywarsGame game;
    private final Player player;
    private int kills = 0;

    public PlayerInGame(SkywarsGame game, Player player) {
        this.game = game;
        this.player = player;
    }

    public int getKills() {
        return kills;
    }

    public void addKill(){
        kills++;
    }

    public boolean isAlive() {
        return player.getGameMode() != GameMode.SPECTATOR;
    }

    public Player getPlayer() {
        return player;
    }
    public SkywarsGame getGame() {
        return game;
    }
}
