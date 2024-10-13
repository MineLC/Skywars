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
    public void load(final ConfigSection challengeConfig, final ChallengeConfig cenabledfigToLoad) {
        final ItemDeserializer itemDeserializer = new ItemDeserializer(logger, "challenges.yml");

        final ConfigSection uhcSectienabled = challengeConfig.getSection("uhc");
        final ConfigSection definitiveWarriorSectienabled = challengeConfig.getSection("definitive-warrior");
        final ConfigSection withoutBlocksSectienabled = challengeConfig.getSection("without-blocks");
        final ConfigSection withoutChestsSectienabled = challengeConfig.getSection("without-chests");
        final ConfigSection archerSectienabled = challengeConfig.getSection("archer");
        final ConfigSection middleLifeSectienabled = challengeConfig.getSection("middle-life");
        final ConfigSection noobSectienabled = challengeConfig.getSection("noob");

        logger.info("UCH " + uhcSectienabled.values().toString());
        // UCH
        cenabledfigToLoad.uhc.inventorySlot = uhcSectienabled.getInt("slot");
        cenabledfigToLoad.uhc.name = uhcSectienabled.getString("name");
        cenabledfigToLoad.uhc.coinsXKill = uhcSectienabled.getInt("coinsXKill");
        cenabledfigToLoad.uhc.onItemInventory = itemDeserializer.buildSafeItem(uhcSectienabled.getSection("enabled"), "uch.enabled");
        cenabledfigToLoad.uhc.offItemInventory = itemDeserializer.buildSafeItem(uhcSectienabled.getSection("disabled"), "uch.disabled");

        // DEFINITE WARRIOR
        cenabledfigToLoad.definitiveWarrior.inventorySlot = definitiveWarriorSectienabled.getInt("slot");
        cenabledfigToLoad.definitiveWarrior.name = definitiveWarriorSectienabled.getString("name");
        cenabledfigToLoad.definitiveWarrior.onItemInventory = itemDeserializer.buildSafeItem(definitiveWarriorSectienabled.getSection("enabled"), "definitive-warrior.enabled");
        cenabledfigToLoad.definitiveWarrior.coinsXKill = definitiveWarriorSectienabled.getInt("coinsXKill");
        cenabledfigToLoad.definitiveWarrior.offItemInventory = itemDeserializer.buildSafeItem(definitiveWarriorSectienabled.getSection("disabled"), "definitive-warrior.disabled");

        // WITHOUT BLOCKS
        cenabledfigToLoad.withoutBlocks.inventorySlot = withoutBlocksSectienabled.getInt("slot");
        cenabledfigToLoad.withoutBlocks.name = withoutBlocksSectienabled.getString("name");
        cenabledfigToLoad.withoutBlocks.coinsXKill = withoutBlocksSectienabled.getInt("coinsXKill");
        cenabledfigToLoad.withoutBlocks.onItemInventory = itemDeserializer.buildSafeItem(withoutBlocksSectienabled.getSection("enabled"), "without-blocks.enabled");
        cenabledfigToLoad.withoutBlocks.offItemInventory = itemDeserializer.buildSafeItem(withoutBlocksSectienabled.getSection("disabled"), "without-blocks.disabled");

        // WITHOUT CHESTS
        cenabledfigToLoad.withoutChests.inventorySlot = withoutChestsSectienabled.getInt("slot");
        cenabledfigToLoad.withoutChests.onItemInventory = itemDeserializer.buildSafeItem(withoutChestsSectienabled.getSection("enabled"), "without-chests.enabled");
        cenabledfigToLoad.withoutChests.coinsXKill = withoutChestsSectienabled.getInt("coinsXKill");
        cenabledfigToLoad.withoutChests.name = withoutChestsSectienabled.getString("name");
        cenabledfigToLoad.withoutChests.offItemInventory = itemDeserializer.buildSafeItem(withoutChestsSectienabled.getSection("disabled"), "without-chests.disabled");

        // ARCHER
        cenabledfigToLoad.archer.inventorySlot = archerSectienabled.getInt("slot");
        cenabledfigToLoad.archer.coinsXKill = archerSectienabled.getInt("coinsXKill");
        cenabledfigToLoad.archer.name = archerSectienabled.getString("name");
        cenabledfigToLoad.archer.onItemInventory = itemDeserializer.buildSafeItem(archerSectienabled.getSection("enabled"), "archer.enabled");
        cenabledfigToLoad.archer.offItemInventory = itemDeserializer.buildSafeItem(archerSectienabled.getSection("disabled"), "archer.disabled");

        // MIDDLE LIFE
        cenabledfigToLoad.middleLife.inventorySlot = middleLifeSectienabled.getInt("slot");
        cenabledfigToLoad.middleLife.name = middleLifeSectienabled.getString("name");
        cenabledfigToLoad.middleLife.coinsXKill = middleLifeSectienabled.getInt("coinsXKill");
        cenabledfigToLoad.middleLife.onItemInventory = itemDeserializer.buildSafeItem(middleLifeSectienabled.getSection("enabled"), "middle-life.enabled");
        cenabledfigToLoad.middleLife.offItemInventory = itemDeserializer.buildSafeItem(middleLifeSectienabled.getSection("disabled"), "middle-life.disabled");

        // NOOB
        cenabledfigToLoad.noob.inventorySlot = noobSectienabled.getInt("slot");
        cenabledfigToLoad.noob.onItemInventory = itemDeserializer.buildSafeItem(noobSectienabled.getSection("enabled"), "noob.enabled");
        cenabledfigToLoad.noob.name = noobSectienabled.getString("name");
        cenabledfigToLoad.noob.coinsXKill = noobSectienabled.getInt("coinsXKill");
        cenabledfigToLoad.noob.offItemInventory = itemDeserializer.buildSafeItem(noobSectienabled.getSection("disabled"), "noob.disabled");

        cenabledfigToLoad.challengeInventoryBuilder = new ChallengeInventoryBuilder(cenabledfigToLoad);
    }
}
