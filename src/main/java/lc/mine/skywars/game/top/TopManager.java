package lc.mine.skywars.game.top;

import lc.mine.skywars.database.User;

public final class TopManager {

    private final TopFiles topFiles;
    private final TopConfig topConfig;

    public TopManager(TopConfig topConfig, TopFiles topFiles) {
        this.topFiles = topFiles;
        this.topConfig = topConfig;
    }

    public void calculateDeaths(final User user, final String name) {
        Top.calculatePosition(new TopPlayer(name, user.deaths), topConfig.getDeathSelector().top);
    }

    public void calculateKills(final User user, final String name) {
        Top.calculatePosition(new TopPlayer(name, user.kills), topConfig.getKillSelector().top);
    }
    
    public void calculateWins(final User user, final String name) {
        Top.calculatePosition(new TopPlayer(name, user.wins), topConfig.getWinSelector().top);
    }
    
    public void calculatePlayed(final User user, final String name) {
        Top.calculatePosition(new TopPlayer(name, user.played), topConfig.getPlayedSelector().top);
    }

    public void save() {
        topFiles.saveAll();
    }

    public void load() {
        topFiles.loadTops();
    }
}
