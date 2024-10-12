package lc.mine.skywars.spawn;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.item.ItemDeserializer;

public class SpawnConfigLoader {

    private final Logger logger;

    public SpawnConfigLoader(Logger logger) {
        this.logger = logger;
    }

    public void load(final ConfigSection spawnConfig, final SpawnConfig configToLoad) {
        final ItemDeserializer itemDeserializer = new ItemDeserializer(logger, "spawn.yml");

        configToLoad.chestModeItem = itemDeserializer.buildInventoryItem(spawnConfig.getSection("vote-chest"), "vote-chest");
        configToLoad.cagesSelectorItem = itemDeserializer.buildInventoryItem(spawnConfig.getSection("cages"), "cages");
        configToLoad.topsItem = itemDeserializer.buildInventoryItem(spawnConfig.getSection("top"), "top");
        configToLoad.mapSelectorItem = itemDeserializer.buildInventoryItem(spawnConfig.getSection("map-selector"), "map-selector");
        configToLoad.kitSelectorItem = itemDeserializer.buildInventoryItem(spawnConfig.getSection("kits"), "kits");

        configToLoad.spawn = loadLocation(spawnConfig.getSection("spawn-location"));
    }

    private Location loadLocation(final ConfigSection spawnSection) {
        String worldName = spawnSection.getString("world");
        World spawnWorld;
        if (worldName == null) {
            logger.warning("Can't found any world in spawn-location.world . File: spawn.yml");
            spawnWorld = Bukkit.getWorlds().get(0);
        } else {
            if ((spawnWorld = Bukkit.getWorld(worldName)) == null) {
                logger.warning("The world " + worldName + " don't exist. File: spawn.yml");
                spawnWorld = Bukkit.getWorlds().get(0);
            }
        }
        return new Location(
            spawnWorld,
            spawnSection.getInt("x"),
            spawnSection.getInt("y"),
            spawnSection.getInt("z"),          
            (float)spawnSection.getInt("yaw"),
            (float)spawnSection.getDouble("pitch")
        );
    }
}
