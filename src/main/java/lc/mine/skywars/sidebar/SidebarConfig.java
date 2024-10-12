package lc.mine.skywars.sidebar;

public class SidebarConfig {

    private GameSidebar gameSidebar;
    private PregameSidebar pregameSidebar;
    private SpawnSidebar spawnSidebar;

    public void setGameSidebar(GameSidebar gameSidebar) {
        this.gameSidebar = gameSidebar;
    }
    public void setPregameSidebar(PregameSidebar pregameSidebar) {
        this.pregameSidebar = pregameSidebar;
    }
    public void setSpawnSidebar(SpawnSidebar spawnSidebar) {
        this.spawnSidebar = spawnSidebar;
    }
    public GameSidebar getGameSidebar() {
        return gameSidebar;
    }
    public PregameSidebar getPregameSidebar() {
        return pregameSidebar;
    }
    public SpawnSidebar getSpawnSidebar() {
        return spawnSidebar;
    }
}
