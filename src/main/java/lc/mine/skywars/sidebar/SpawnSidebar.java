package lc.mine.skywars.sidebar;

import org.bukkit.entity.Player;

import io.github.ichocomilk.lightsidebar.nms.v1_8R3.Sidebar1_8R3;
import lc.mine.core.CorePlugin;
import lc.mine.core.database.PlayerData;
import lc.mine.skywars.spawn.SpawnConfig;
import net.minecraft.server.v1_8_R3.MinecraftServer;

public final class SpawnSidebar {

    private final SpawnConfig spawnConfig;
    private final CorePlugin corePlugin;

    public SpawnSidebar(SpawnConfig spawnConfig, CorePlugin corePlugin) {
        this.spawnConfig = spawnConfig;
        this.corePlugin = corePlugin;
    }

    public void sendSpawnSidebar(final Player player) {
        if (!player.getWorld().equals(spawnConfig.getSpawn().getWorld())) {
            return;
        }
        final Sidebar1_8R3 sidebar = new Sidebar1_8R3();
        final PlayerData data = corePlugin.getData().getCached(player.getUniqueId());
 
        sidebar.setTitle("§6§lSkywars");
        sidebar.setLines(sidebar.createLines(new String[]{
            "",
            "§fLCoins: §6" + data.getLcoins(),
            "§fVipPoints: §b" + data.getVipPoins(),
            "  ",
            "§fConectados: §6" + MinecraftServer.getServer().getPlayerList().getPlayerCount(),
            " ",
            "§bplay.mine.lc"
        }));
        sidebar.sendLines(player);
        sidebar.sendTitle(player);
    }
}
