package lc.mine.skywars.config.sound;

import java.util.logging.Logger;

import org.bukkit.Sound;

import lc.mine.skywars.config.ConfigSection;

public final class SoundDeserializer {

    private final Logger logger;
    private final String fileName;

    public SoundDeserializer(Logger logger, String fileName) {
        this.logger = logger;
        this.fileName = fileName;
    }

    public SkywarsSound loadSound(final ConfigSection section, final String sectionName) {
        if (!section.getBoolean("enable")) {
            return null;
        }
        final String soundName = section.getString("type");
        if (soundName == null) {
            logger.warning("Can't found any sound in the " + sectionName + ". File: " + fileName);
            return null;   
        }
        final Sound sound;
        try {
            sound = Sound.valueOf(soundName);
        } catch (Exception e) {
            logger.warning("Can't found the sound " + soundName + ". Section: " + sectionName + ". File " + fileName);
            return null;
        }
        return new SkywarsSound(sound, (float)section.getDouble("volume"), (float)section.getDouble("pitch"));
    }
}
