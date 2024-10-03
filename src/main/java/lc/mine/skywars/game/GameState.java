package lc.mine.skywars.game;

public enum GameState {
    PREGAME,
    IN_GAME;

    public static GameState currentState = GameState.PREGAME;
}
