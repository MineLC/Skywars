package lc.mine.skywars.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/*
 * El único comentario del plugin y es para un buen uso
 */
public class HentaiCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        sender.sendMessage(" verhentai.top §e9/10 §7(Ofrece preview y una detallada sinopsis) \n §fhentaila.com §e7/10 §7(Comunidad activa, pero muy rara) \n §fnhentai.com §e9/10 §7(God pero ingles) \n §fhentaird.com §e7/10 §7(Muchos hentais viejos, con buena historia) \n §fmuchohentai.com §e8/10 §7(Ofrece episodios RAW y en otros idiomas) \n §fchochox.com §e10/10 §7(De hecho, hay un comic en chochox que lo explica) \n \n §8by iChocoMilk (Lector de la biblia)");
        return true;
    }
}
