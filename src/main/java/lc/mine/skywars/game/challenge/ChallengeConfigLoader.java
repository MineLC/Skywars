package lc.mine.skywars.game.challenge;

import lc.mine.skywars.config.ConfigSection;
import lc.mine.skywars.config.item.ItemDeserializer;
import lc.mine.skywars.game.challenge.gui.ChallengeInventoryBuilder;

import java.util.logging.Logger;

public class ChallengeConfigLoader {

    private final Logger logger;

    public ChallengeConfigLoader(Logger logger) {
        this.logger = logger;
    }

    @SuppressWarnings("DataFlowIssue")
    public void load(final ConfigSection topConfig, final ChallengeConfig configToLoad) {
        final ItemDeserializer itemDeserializer = new ItemDeserializer(logger, "challenges.yml");

        final ConfigSection uhcSection = topConfig.getSection("uhc");
        final ConfigSection definitiveWarriorSection = topConfig.getSection("definitive-warrior");
        final ConfigSection withoutBlocksSection = topConfig.getSection("without-blocks");
        final ConfigSection withoutChestsSection = topConfig.getSection("without-chests");
        final ConfigSection archerSection = topConfig.getSection("archer");
        final ConfigSection middleLifeSection = topConfig.getSection("middle-life");
        final ConfigSection noobSection = topConfig.getSection("noob");

        // UCH
        configToLoad.uhc.inventorySlot = uhcSection.getInt("slot");
        configToLoad.uhc.name = uhcSection.getString("name");
        configToLoad.uhc.coinsXKill = uhcSection.getInt("coinsXKill");
        configToLoad.uhc.onItemInventory = itemDeserializer.buildSafeItem(uhcSection.getSection("on"), "on");
        configToLoad.uhc.offItemInventory = itemDeserializer.buildSafeItem(uhcSection.getSection("off"), "off");

        // DEFINITE WARRIOR
        configToLoad.definitiveWarrior.inventorySlot = definitiveWarriorSection.getInt("slot");
        configToLoad.definitiveWarrior.name = definitiveWarriorSection.getString("name");
        configToLoad.definitiveWarrior.onItemInventory = itemDeserializer.buildSafeItem(definitiveWarriorSection.getSection("on"), "on");
        configToLoad.definitiveWarrior.coinsXKill = definitiveWarriorSection.getInt("coinsXKill");
        configToLoad.definitiveWarrior.offItemInventory = itemDeserializer.buildSafeItem(definitiveWarriorSection.getSection("off"), "off");

        // WITHOUT BLOCKS
        configToLoad.withoutBlocks.inventorySlot = withoutBlocksSection.getInt("slot");
        configToLoad.withoutBlocks.name = withoutBlocksSection.getString("name");
        configToLoad.withoutBlocks.coinsXKill = withoutBlocksSection.getInt("coinsXKill");
        configToLoad.withoutBlocks.onItemInventory = itemDeserializer.buildSafeItem(withoutBlocksSection.getSection("on"), "on");
        configToLoad.withoutBlocks.offItemInventory = itemDeserializer.buildSafeItem(withoutBlocksSection.getSection("off"), "off");

        // WITHOUT CHESTS
        configToLoad.withoutChests.inventorySlot = withoutChestsSection.getInt("slot");
        configToLoad.withoutChests.onItemInventory = itemDeserializer.buildSafeItem(withoutChestsSection.getSection("on"), "on");
        configToLoad.withoutChests.coinsXKill = withoutChestsSection.getInt("coinsXKill");
        configToLoad.withoutChests.name = withoutChestsSection.getString("name");
        configToLoad.withoutChests.offItemInventory = itemDeserializer.buildSafeItem(withoutChestsSection.getSection("off"), "off");

        // ARCHER
        configToLoad.archer.inventorySlot = archerSection.getInt("slot");
        configToLoad.archer.coinsXKill = archerSection.getInt("coinsXKill");
        configToLoad.archer.name = archerSection.getString("name");
        configToLoad.archer.onItemInventory = itemDeserializer.buildSafeItem(archerSection.getSection("on"), "on");
        configToLoad.archer.offItemInventory = itemDeserializer.buildSafeItem(archerSection.getSection("off"), "off");

        // MIDDLE LIFE
        configToLoad.middleLife.inventorySlot = middleLifeSection.getInt("slot");
        configToLoad.middleLife.name = middleLifeSection.getString("name");
        configToLoad.middleLife.coinsXKill = middleLifeSection.getInt("coinsXKill");
        configToLoad.middleLife.onItemInventory = itemDeserializer.buildSafeItem(middleLifeSection.getSection("on"), "on");
        configToLoad.middleLife.offItemInventory = itemDeserializer.buildSafeItem(middleLifeSection.getSection("off"), "off");

        // NOOB
        configToLoad.noob.inventorySlot = noobSection.getInt("slot");
        configToLoad.noob.onItemInventory = itemDeserializer.buildSafeItem(noobSection.getSection("on"), "on");
        configToLoad.noob.name = noobSection.getString("name");
        configToLoad.noob.coinsXKill = noobSection.getInt("coinsXKill");
        configToLoad.noob.offItemInventory = itemDeserializer.buildSafeItem(noobSection.getSection("off"), "off");

        configToLoad.challengeInventoryBuilder = new ChallengeInventoryBuilder(configToLoad);
    }
}
