package lc.mine.skywars.map;

public final class SkywarsMap {
    private final MapSpawn[] spawns;
    private final String displayName;
    private final String worldName;
    private final int worldBorderSize;

    private MapState mapState = MapState.UNLOADED;

    SkywarsMap(MapSpawn[] spawns, String displayName, int worldBorderSize, String worldName) {
        this.spawns = spawns;
        this.displayName = displayName;
        this.worldBorderSize = worldBorderSize;
        this.worldName = worldName;
    }

    public void resetSpawns() {
        for (final MapSpawn spawn : spawns) {
            spawn.playerUsingIt = null;
        }
    }

    public String getDisplayName() {
        return displayName;
    }
    public MapSpawn[] getSpawns() {
        return spawns;
    }
    public String getWorldName() {
        return worldName;
    }
    public int getMaxPlayers() {
        return spawns.length;
    }
    public int getWorldBorderSize() {
        return worldBorderSize;
    }
    public void setMapState(MapState mapState) {
        this.mapState = mapState;
    }
    public MapState getMapState() {
        return mapState;
    }
}