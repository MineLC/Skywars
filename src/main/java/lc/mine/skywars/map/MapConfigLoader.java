package lc.mine.skywars.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.message.MessageColor;
import lc.mine.skywars.config.utils.FileUtils;

public class MapConfigLoader {

    private final Logger logger;

    public MapConfigLoader(Logger logger) {
        this.logger = logger;
    }

    public SkywarsMap loadMap(final Yaml yaml, final JavaPlugin plugin, final SlimePlugin slimePlugin) {
        final File[] mapFiles = new File(plugin.getDataFolder(), "maps").listFiles();
        if (mapFiles == null || mapFiles.length == 0) {
            return null;
        }
        final File mapFile = mapFiles[new SplittableRandom().nextInt(mapFiles.length - 1)];
        final String mapName = mapFile.getName();
        if (!mapName.endsWith(".yml")) {
            return null;
        }
        final ConfigSection config = FileUtils.getConfig(yaml, mapFile);
        final String worldName = config.getString("world");
        if (worldName == null) {
            logger.warning("The map " + mapName + " contains a inexistent world : " + worldName);
            return null;
        }
        final World world = loadWorld(slimePlugin, worldName);
        if (world == null) {
            return null;
        }
        world.getWorldBorder().setSize(config.getInt("worldborder"));
        return new SkywarsMap(
            loadSpawns(config.getStringList("spawns"), mapName),
            MessageColor.fastColor(config.getOrDefault("displayname", mapName)),
            world);
    }

    private MapSpawn[] loadSpawns(final List<String> spawnList, final String mapName) {
        if (spawnList == null) {
            return null;
        }
        final List<MapSpawn> listSpawns = new ArrayList<>();
        for (final String spawn : spawnList) {
            final String[] split = StringUtils.split(spawn, ',');
            if (split.length != 3) {
                logger.warning("The spawn " + spawn + " is invalid. Map: " + mapName);
                continue;
            }
            listSpawns.add(new MapSpawn(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
            ));
        }
        return listSpawns.toArray(new MapSpawn[0]);
    }

    private World loadWorld(final SlimePlugin plugin, final String worldName) {
        try {
            plugin.generateWorld(plugin.loadWorld(plugin.getLoader("file"), worldName, false, new SlimePropertyMap()));
            return Bukkit.getWorld(worldName);
        } catch (UnknownWorldException | CorruptedWorldException | NewerFormatException | WorldInUseException | IOException e) {
            logger.log(Level.SEVERE, "Can't generate the world " + worldName + " Error ", e);
        }
        return null;
    }
}
