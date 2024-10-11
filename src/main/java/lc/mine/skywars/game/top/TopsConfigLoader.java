package lc.mine.skywars.game.top;

import java.util.logging.Logger;

import lc.mine.core.CorePlugin;
import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.item.ItemDeserializer;
import lc.mine.skywars.config.message.MessageColor;
import lc.mine.skywars.game.top.gui.TopInventoryBuilder;

public class TopsConfigLoader {

    private final Logger logger;

    public TopsConfigLoader(Logger logger) {
        this.logger = logger;
    }

    public void load(final ConfigSection topConfig, CorePlugin corePlugin, final TopConfig configToLoad) {
        final ItemDeserializer itemDeserializer = new ItemDeserializer(logger, "tops.yml");

        final ConfigSection killSection = topConfig.getSection("kills");
        final ConfigSection deathSection = topConfig.getSection("deaths");
        final ConfigSection winSection = topConfig.getSection("wins");
        final ConfigSection playedSection = topConfig.getSection("played");

        configToLoad.amountTops = topConfig.getOrDefault("amount-tops", 50);

        configToLoad.deathSelector.inventorySlot = deathSection.getInt("slot");
        configToLoad.deathSelector.inventoryTitle = MessageColor.fastColor(deathSection.getOrDefault("inventory-title", "death"));
        configToLoad.deathSelector.itemInventory = itemDeserializer.buildItem(deathSection, "deaths");

        configToLoad.killSelector.inventorySlot = killSection.getInt("slot");
        configToLoad.killSelector.inventoryTitle = MessageColor.fastColor(killSection.getOrDefault("inventory-title", "kills"));
        configToLoad.killSelector.itemInventory = itemDeserializer.buildItem(killSection, "kills");

        configToLoad.playedSelector.inventorySlot = playedSection.getInt("slot");
        configToLoad.playedSelector.inventoryTitle = MessageColor.fastColor(playedSection.getOrDefault("inventory-title", "played"));
        configToLoad.playedSelector.itemInventory = itemDeserializer.buildItem(playedSection, "played");

        configToLoad.winSelector.inventorySlot = winSection.getInt("slot");
        configToLoad.winSelector.inventoryTitle = MessageColor.fastColor(winSection.getOrDefault("inventory-title", "win"));
        configToLoad.winSelector.itemInventory = itemDeserializer.buildItem(winSection, "wins");

        configToLoad.topInventoryBuilder = new TopInventoryBuilder(configToLoad, corePlugin);
    }
}
