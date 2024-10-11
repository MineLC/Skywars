package lc.mine.skywars.config.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public record SkywarsSound(
    Sound sound,
    float volume,
    float pitch
) {

    public final void send(final Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
