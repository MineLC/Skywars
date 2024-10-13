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
    public void load(final ConfigSection challengeConfig, final ChallengeConfig configToLoad) {
        final ItemDeserializer itemDeserializer = new ItemDeserializer(logger, "challenges.yml");

        final ConfigSection uhcSectienabled = challengeConfig.getSection("uhc");
        final ConfigSection definitiveWarriorSectienabled = challengeConfig.getSection("definitive-warrior");
        final ConfigSection withoutBlocksSectienabled = challengeConfig.getSection("without-blocks");
        final ConfigSection withoutChestsSectienabled = challengeConfig.getSection("without-chests");
        final ConfigSection archerSectienabled = challengeConfig.getSection("archer");
        final ConfigSection middleLifeSectienabled = challengeConfig.getSection("middle-life");
        final ConfigSection noobSectienabled = challengeConfig.getSection("noob");

        configToLoad.onItem = itemDeserializer.buildSafeItem(challengeConfig.getSection("on-item"), "on-item");
        configToLoad.offItem = itemDeserializer.buildSafeItem(challengeConfig.getSection("off-item"), "off-item");

        // UCH
        configToLoad.uhc.inventorySlot = uhcSectienabled.getInt("slot");
        configToLoad.uhc.name = uhcSectienabled.getString("name");
        configToLoad.uhc.coinsXKill = uhcSectienabled.getInt("coinsXKill");
        configToLoad.uhc.onItemInventory = itemDeserializer.buildSafeItem(uhcSectienabled.getSection("enabled"), "uch.enabled");
        configToLoad.uhc.offItemInventory = itemDeserializer.buildSafeItem(uhcSectienabled.getSection("disabled"), "uch.disabled");

        // DEFINITE WARRIOR
        configToLoad.definitiveWarrior.inventorySlot = definitiveWarriorSectienabled.getInt("slot");
        configToLoad.definitiveWarrior.name = definitiveWarriorSectienabled.getString("name");
        configToLoad.definitiveWarrior.onItemInventory = itemDeserializer.buildSafeItem(definitiveWarriorSectienabled.getSection("enabled"), "definitive-warrior.enabled");
        configToLoad.definitiveWarrior.coinsXKill = definitiveWarriorSectienabled.getInt("coinsXKill");
        configToLoad.definitiveWarrior.offItemInventory = itemDeserializer.buildSafeItem(definitiveWarriorSectienabled.getSection("disabled"), "definitive-warrior.disabled");

        // WITHOUT BLOCKS
        configToLoad.withoutBlocks.inventorySlot = withoutBlocksSectienabled.getInt("slot");
        configToLoad.withoutBlocks.name = withoutBlocksSectienabled.getString("name");
        configToLoad.withoutBlocks.coinsXKill = withoutBlocksSectienabled.getInt("coinsXKill");
        configToLoad.withoutBlocks.onItemInventory = itemDeserializer.buildSafeItem(withoutBlocksSectienabled.getSection("enabled"), "without-blocks.enabled");
        configToLoad.withoutBlocks.offItemInventory = itemDeserializer.buildSafeItem(withoutBlocksSectienabled.getSection("disabled"), "without-blocks.disabled");

        // WITHOUT CHESTS
        configToLoad.withoutChests.inventorySlot = withoutChestsSectienabled.getInt("slot");
        configToLoad.withoutChests.onItemInventory = itemDeserializer.buildSafeItem(withoutChestsSectienabled.getSection("enabled"), "without-chests.enabled");
        configToLoad.withoutChests.coinsXKill = withoutChestsSectienabled.getInt("coinsXKill");
        configToLoad.withoutChests.name = withoutChestsSectienabled.getString("name");
        configToLoad.withoutChests.offItemInventory = itemDeserializer.buildSafeItem(withoutChestsSectienabled.getSection("disabled"), "without-chests.disabled");

        // ARCHER
        configToLoad.archer.inventorySlot = archerSectienabled.getInt("slot");
        configToLoad.archer.coinsXKill = archerSectienabled.getInt("coinsXKill");
        configToLoad.archer.name = archerSectienabled.getString("name");
        configToLoad.archer.onItemInventory = itemDeserializer.buildSafeItem(archerSectienabled.getSection("enabled"), "archer.enabled");
        configToLoad.archer.offItemInventory = itemDeserializer.buildSafeItem(archerSectienabled.getSection("disabled"), "archer.disabled");

        // MIDDLE LIFE
        configToLoad.middleLife.inventorySlot = middleLifeSectienabled.getInt("slot");
        configToLoad.middleLife.name = middleLifeSectienabled.getString("name");
        configToLoad.middleLife.coinsXKill = middleLifeSectienabled.getInt("coinsXKill");
        configToLoad.middleLife.onItemInventory = itemDeserializer.buildSafeItem(middleLifeSectienabled.getSection("enabled"), "middle-life.enabled");
        configToLoad.middleLife.offItemInventory = itemDeserializer.buildSafeItem(middleLifeSectienabled.getSection("disabled"), "middle-life.disabled");

        // NOOB
        configToLoad.noob.inventorySlot = noobSectienabled.getInt("slot");
        configToLoad.noob.onItemInventory = itemDeserializer.buildSafeItem(noobSectienabled.getSection("enabled"), "noob.enabled");
        configToLoad.noob.name = noobSectienabled.getString("name");
        configToLoad.noob.coinsXKill = noobSectienabled.getInt("coinsXKill");
        configToLoad.noob.offItemInventory = itemDeserializer.buildSafeItem(noobSectienabled.getSection("disabled"), "noob.disabled");

        configToLoad.challengeInventoryBuilder = new ChallengeInventoryBuilder(configToLoad);
    }
}
