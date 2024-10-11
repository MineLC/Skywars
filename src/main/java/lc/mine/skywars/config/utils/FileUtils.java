package lc.mine.skywars.config.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import lc.mine.skywars.config.ConfigSection;

public final class FileUtils {

    private static final Plugin PLUGIN = Bukkit.getPluginManager().getPlugin("Skywars");

    public static ConfigSection getConfig(final Yaml yaml, final String file) {
        return getConfig(yaml, (new File(PLUGIN.getDataFolder(), file)));
    }

    @SuppressWarnings("unchecked")
    public static ConfigSection getConfig(final Yaml yaml, final File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return new ConfigSection(yaml.loadAs(reader, Map.class));
        } catch (Exception e) {
            PLUGIN.getLogger().log(Level.SEVERE, "Error parsing the file " + file, e);
            return null;
        }
    }

    public static void createIfAbsent(final String... files) {
        final File datafolder = PLUGIN.getDataFolder();
        if (!datafolder.exists()) {
            datafolder.mkdir();
        }
        for (final String file : files) {
            final File fileOut = new File(datafolder, file);
            if (!fileOut.exists()) {
                write(file, fileOut);
            }
        }
    }

    public static File[] createFiles(final String... files) {
        final File datafolder = PLUGIN.getDataFolder();
        final File[] filesArray = new File[files.length];
        int i = 0;
        for (final String file : files) {
            final File fileOut = new File(datafolder, file);
            write(file, fileOut);
            filesArray[i++] = fileOut;
        }
        return filesArray;
    }

    public static void write(final String resourcePath, final File outDestination) {
        final InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        if (stream == null) {
            PLUGIN.getLogger().warning("The file " + resourcePath + " don't exist in resources folder");
            return;
        }
        try {
            IOUtils.copy(stream, new FileOutputStream(outDestination));
        } catch (IOException e) {
            PLUGIN.getLogger().log(Level.SEVERE, "Error copying the file " + resourcePath + " into " + outDestination + ". Error -> ", e);
        }
    }
}