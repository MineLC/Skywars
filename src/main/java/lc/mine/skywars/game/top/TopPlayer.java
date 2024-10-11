package lc.mine.skywars.game.top;

public final class TopPlayer {
    public final String name;
    public int score;

    public TopPlayer(String name, int score) {
        this.name = name;
        this.score = score;
    }
    @Override
    public String toString() {
        return name;
    }
}