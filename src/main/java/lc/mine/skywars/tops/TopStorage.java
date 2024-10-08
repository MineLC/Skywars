package lc.mine.skywars.tops;

public final class TopStorage  {
  private final Top wins;
  private final Top deaths;
  
  private static TopStorage storage;

    public TopStorage(Top wins, Top deaths) {
        this.wins = wins;
        this.deaths = deaths;
    }

    public static Top wins() {
        return storage.wins;
    }
    public static Top deaths() {
        return storage.deaths;
    }
    static void set(TopStorage newStorage) {
        storage = newStorage;
    }
}
