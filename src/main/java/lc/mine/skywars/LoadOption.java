package lc.mine.skywars;

public enum LoadOption {
    ALL,
    CONFIG,
    MESSAGES,
    KITS,
    MAPS,
    CHESTREFILL,
    GAMESTATES,
    DATABASE;

    public static final LoadOption[] OPTIONS = LoadOption.values();
}
