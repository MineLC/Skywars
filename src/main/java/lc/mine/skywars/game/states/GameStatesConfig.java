package lc.mine.skywars.game.states;

import lc.mine.skywars.config.sound.SkywarsSound;

public final class GameStatesConfig {
    
    private final PreGame pregameConfig = new PreGame();
    private final EndGame endgameConfig = new EndGame();
    private final InGame ingameConfig = new InGame();

    public static final class PreGame {
        int minPlayers = 2;
        int countdownTime = 60;
        int spamTime = 10;

        SkywarsSound spamSound;

        public int getMinPlayers() {
            return minPlayers;
        }
        public int getCountdownTime() {
            return countdownTime;
        }
        public SkywarsSound getSpamSound() {
            return spamSound;
        }
        public int getSpamTime() {
            return spamTime;
        }
    }

    public static final class EndGame {
        boolean spawnFireworks = true;

        public boolean isSpawnFireworks() {
            return spawnFireworks;
        }
    }

    public static final class InGame {
        boolean chestRefillMessage = true;
        SkywarsSound chestRefillSound;
        int chestRefillTime = 120;

        public int getChestRefillTime() {
            return chestRefillTime;
        }
        public SkywarsSound getChestRefillSound() {
            return chestRefillSound;
        }
        public boolean isChestRefillMessage() {
            return chestRefillMessage;
        }
    }

    public PreGame getPregameConfig() {
        return pregameConfig;
    }

    public EndGame getEndgameConfig() {
        return endgameConfig;
    }

    public InGame getInGameConfig() {
        return ingameConfig;
    }
}
