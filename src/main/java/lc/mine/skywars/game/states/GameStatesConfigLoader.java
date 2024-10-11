package lc.mine.skywars.game.states;

import java.util.logging.Logger;

import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.sound.SoundDeserializer;

public final class GameStatesConfigLoader {

    private final Logger logger;
    private final SoundDeserializer soundDeserializer;

    public GameStatesConfigLoader(Logger logger) {
        this.logger = logger;
        this.soundDeserializer = new SoundDeserializer(logger, "gamestates.yml");
    }

    public void loadConfig(final ConfigSection config, final GameStatesConfig configToLoad) {
        loadPregame(config.getSection("pregame"), configToLoad.getPregameConfig());
        loadInGame(config.getSection("ingame"), configToLoad.getInGameConfig());
        loadEndgame(config.getSection("endgame"), configToLoad.getEndgameConfig());
    }

    private void loadPregame(final ConfigSection pregame, final GameStatesConfig.PreGame configToLoad) {
        configToLoad.minPlayers = pregame.getInt("min-players");

        final ConfigSection spam = pregame.getSection("spam");
        if (spam == null) {
            logger.warning("Can't found the spam section in pregame. File: gamestates.yml");
            return;
        }
        configToLoad.spamSound = soundDeserializer.loadSound(spam, "pregame.sound");
    }

    private void loadEndgame(final ConfigSection endgame, final GameStatesConfig.EndGame configToLoad) {
        configToLoad.spawnFireworks = endgame.getBoolean("spawn-fireworks");
    }

    private void loadInGame(final ConfigSection ingame, final GameStatesConfig.InGame configToLoad) {
        final ConfigSection chestRefill = ingame.getSection("chestrefill");
        if (chestRefill == null) {
            logger.warning("Can't found the chestrefill section in ingame. File: gamestates.yml");
            return;
        }
        configToLoad.chestRefillSound = soundDeserializer.loadSound(chestRefill, "ingame.chestrefill");
        configToLoad.chestRefillTime = chestRefill.getOrDefault("waiting-time", 120);
        configToLoad.chestRefillMessage = chestRefill.getBoolean("enable-message");
    }
}