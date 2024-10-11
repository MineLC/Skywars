package lc.mine.skywars.game.top;


import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileReader;

import org.apache.commons.lang.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

import lc.mine.skywars.utils.IntegerUtil;

public final class TopFiles {
    private final Logger logger;
    private final File folder;
    private final TopConfig topConfig;
    
    public TopFiles(JavaPlugin plugin, TopConfig topConfig) {
        this.logger = plugin.getLogger();
        this.folder = new File(plugin.getDataFolder(), "tops");
        this.topConfig = topConfig;
    }
    
    public void loadTops() {
        topConfig.killSelector.top = this.read("kills");
        topConfig.winSelector.top = this.read("wins");
        topConfig.playedSelector.top = this.read("played");
        topConfig.deathSelector.top = this.read("deaths");
    }
    
    private Top read(final String name) {
        return this.readTop(new File(this.folder, name + ".csv"), name);
    }
    
    private void save(final String name, final Top top) {
        this.saveTop(new File(this.folder, name + ".csv"), top);
    }
    
    public void saveAll() {
        if (!this.folder.exists()) {
            this.folder.mkdir();
        }
        this.save("kills", topConfig.killSelector.getTop());
        this.save("deaths", topConfig.deathSelector.getTop());
        this.save("wins", topConfig.winSelector.getTop());
        this.save("played", topConfig.playedSelector.getTop());
    }
    
    public void saveTop(final File file, final Top top) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            final StringBuilder builder = new StringBuilder();
            final int amountTops = topConfig.amountTops;
            for (int i = 0; i < amountTops && top.getPlayers()[i] != null; ++i) {
                builder.append(top.getPlayers()[i].name);
                builder.append(',');
                builder.append(top.getPlayers()[i].score);
                builder.append('\n');
            }
            writer.write(builder.toString());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error trying to save the top " + top.getName(), e);
        }
    }
    
    public Top readTop(final File file, final String name) {
        if (!file.exists()) {
            return new Top(topConfig.amountTops, name);
        }

        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int i = 0;
            final Top top = new Top(topConfig.amountTops, name);
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] split = StringUtils.split(line, ',');
                top.getPlayers()[i++] = new TopPlayer(split[0], IntegerUtil.parsePositive(split[1], 0));
            }            
            return top;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error trying to read the top " + name, e);
            return null;
        }
    }
}