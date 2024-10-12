package lc.mine.skywars.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.message.MessageColor;
import lc.mine.skywars.config.message.Messages;
import lc.mine.skywars.config.utils.FileUtils;

public class MapManager {

    private final Logger logger;
    private final SlimePlugin slimePlugin;
    private final ExecutorService threadPool;
    private final SlimeLoader fileLoader;

    public MapManager(Logger logger, SlimePlugin slimePlugin, ExecutorService threadPool) {
        this.logger = logger;
        this.slimePlugin = slimePlugin;
        this.threadPool = threadPool;
        this.fileLoader = slimePlugin.getLoader("file");
    }

    public boolean canJoin(final SkywarsMap map, final Player player) {
        switch (map.getMapState()) {
            case LOADING:
                Messages.send(player, "map.loading");
                return false;
            case ERROR:
                Messages.send(player, "map.cant-load");
                return false;
            case UNLOADING:
                Messages.send(player, "map.unloading");
                return false;
            default:
                return true;
        }
    }

    public SkywarsMap[] loadAllMaps(final Yaml yaml, final JavaPlugin plugin) {
        final File[] mapFiles = new File(plugin.getDataFolder(), "maps").listFiles();
        if (mapFiles == null || mapFiles.length == 0) {
            return new SkywarsMap[0];
        }

        final List<SkywarsMap> skywarsMaps = new ArrayList<>();
        for (final File mapFile : mapFiles) {
            final String mapName = mapFile.getName();
            if (!mapName.endsWith(".yml")) {
                continue;
            }
            final ConfigSection config = FileUtils.getConfig(yaml, mapFile);
            final String worldName = config.getString("world");
            if (worldName == null) {
                logger.warning("The map " + mapName + " contains a inexistent world : " + worldName);
                continue;
            }
            skywarsMaps.add(new SkywarsMap(
                loadSpawns(config.getStringList("spawns"), mapName),
                MessageColor.fastColor(config.getOrDefault("displayname", mapName)),
                config.getInt("worldborder"),
                worldName));    
        }
        return skywarsMaps.toArray(new SkywarsMap[0]);
    }

    public void unloadMap(final SkywarsMap map, final World world) {
        map.setMapState(MapState.UNLOADING);
        Bukkit.unloadWorld(world, false);
        map.setMapState(MapState.UNLOADED);
    }

    public void loadMap(final SkywarsMap map, final MapLoadSupply supply) {
        map.setMapState(MapState.LOADING);
        threadPool.execute(() -> {
            try {
                slimePlugin.generateWorld(slimePlugin.loadWorld(fileLoader, map.getWorldName(), false, new SlimePropertyMap()));
                map.setMapState(MapState.READY);
                final World world = Bukkit.getWorld(map.getWorldName());
                world.getWorldBorder().setSize(map.getWorldBorderSize());
                supply.onLoad(world, map);
            } catch (UnknownWorldException | CorruptedWorldException | NewerFormatException | WorldInUseException | IOException e) {
                logger.log(Level.SEVERE, "Can't generate the world " + map.getWorldName() + " Error ", e);
                map.setMapState(MapState.ERROR);
                supply.onMapLoadError();
            }
        });
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
}
