package io.github.mooy1.infinityexpansion.items;

import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinityexpansion.items.mobdata.MobDataCard;
import io.github.mooy1.infinityexpansion.items.mobdata.MobDataInfuser;
import io.github.mooy1.infinityexpansion.items.mobdata.MobDataTier;
import io.github.mooy1.infinityexpansion.items.mobdata.MobSimulationChamber;
import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

@UtilityClass
public final class MobData {

    private static final int CHAMBER_INTERVAL = InfinityExpansion.inst().getConfig().getInt("mob-simulation-options.ticks-per-output", 1, 1000);
    private static final int CHAMBER_BUFFER = 15000;
    private static final int CHAMBER_ENERGY = 150;
    private static final int INFUSER_ENERGY = 20000;

    public static final SlimefunItemStack EMPTY_DATA_CARD = new SlimefunItemStack(
            "EMPTY_DATA_CARD",
            Material.CHAINMAIL_CHESTPLATE,
            "&8空置模拟器",
            "&7用生物物品填充"
    );
    public static final SlimefunItemStack INFUSER = new SlimefunItemStack(
            "DATA_INFUSER",
            Material.LODESTONE,
            "&8模拟器合成机",
            "&7可以让模拟器和生物物品融合",
            "",
            LorePreset.energy(INFUSER_ENERGY) + "每次使用"
    );
    public static final SlimefunItemStack CHAMBER = new SlimefunItemStack(
            "MOB_SIMULATION_CHAMBER",
            Material.GILDED_BLACKSTONE,
            "&8模拟空间",
            "&7让模拟器可运行",
            "&7会产出对应物品",
            LorePreset.energyBuffer(CHAMBER_BUFFER),
            LorePreset.energyPerSecond(CHAMBER_ENERGY)
    );

    public static void setup(InfinityExpansion plugin) {

        new MobSimulationChamber(Categories.MOB_SIMULATION, CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_PLATE, Materials.MAGSTEEL_PLATE,
                Materials.MACHINE_CIRCUIT, SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER, Materials.MACHINE_CIRCUIT,
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_PLATE, Materials.MAGSTEEL_PLATE,
        }, CHAMBER_ENERGY, CHAMBER_INTERVAL).register(plugin);

        new MobDataInfuser(Categories.MOB_SIMULATION, INFUSER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_CIRCUIT, SlimefunItems.REINFORCED_ALLOY_INGOT, Materials.MACHINE_CIRCUIT,
                SlimefunItems.REINFORCED_ALLOY_INGOT, Materials.MACHINE_CORE, SlimefunItems.REINFORCED_ALLOY_INGOT,
                Materials.MACHINE_CIRCUIT, SlimefunItems.REINFORCED_ALLOY_INGOT, Materials.MACHINE_CIRCUIT
        }, INFUSER_ENERGY).register(plugin);

        new SlimefunItem(Categories.MOB_SIMULATION, EMPTY_DATA_CARD, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.MAGNESIUM_INGOT, Materials.MACHINE_CIRCUIT, SlimefunItems.MAGNESIUM_INGOT,
                SlimefunItems.SYNTHETIC_SAPPHIRE, SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.SYNTHETIC_EMERALD,
                SlimefunItems.MAGNESIUM_INGOT, Materials.MACHINE_CIRCUIT, SlimefunItems.MAGNESIUM_INGOT
        }).register(plugin);

        new MobDataCard("僵尸", MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(Material.IRON_SWORD, 1), new ItemStack(Material.ROTTEN_FLESH, 16), new ItemStack(Material.IRON_SHOVEL, 1),
                new ItemStack(Material.IRON_INGOT, 64), EMPTY_DATA_CARD, new ItemStack(Material.IRON_INGOT, 1),
                new ItemStack(Material.CARROT, 64), new ItemStack(Material.ROTTEN_FLESH, 16), new ItemStack(Material.POTATO, 64)
        }).addDrop(Material.ROTTEN_FLESH, 1).register(plugin);
        new MobDataCard("史莱姆", MobDataTier.NEUTRAL, new ItemStack[] {
                new ItemStack(Material.SLIME_BLOCK, 16), new ItemStack(Material.LIME_DYE, 16), new ItemStack(Material.SLIME_BLOCK, 16),
                new ItemStack(Material.LIME_DYE, 16), EMPTY_DATA_CARD, new ItemStack(Material.LIME_DYE, 16),
                new ItemStack(Material.SLIME_BLOCK, 16), new ItemStack(Material.LIME_DYE, 16), new ItemStack(Material.SLIME_BLOCK, 16)
        }).addDrop(Material.SLIME_BALL, 1).register(plugin);
        new MobDataCard("岩浆怪", MobDataTier.NEUTRAL, new ItemStack[] {
                new ItemStack(Material.MAGMA_BLOCK, 64), new ItemStack(Material.MAGMA_CREAM, 16), new ItemStack(Material.MAGMA_BLOCK, 64),
                new ItemStack(Material.SLIME_BLOCK, 16), EMPTY_DATA_CARD, new ItemStack(Material.SLIME_BLOCK, 16),
                new ItemStack(Material.MAGMA_BLOCK, 64), new ItemStack(Material.MAGMA_CREAM, 16), new ItemStack(Material.MAGMA_BLOCK, 64)
        }).addDrop(Material.MAGMA_CREAM, 1).register(plugin);
        new MobDataCard("牛", MobDataTier.PASSIVE, new ItemStack[] {
                new ItemStack(Material.LEATHER, 64), new ItemStack(Material.BEEF, 64), new ItemStack(Material.LEATHER, 64),
                new ItemStack(Material.COOKED_BEEF, 64), EMPTY_DATA_CARD, new ItemStack(Material.COOKED_BEEF, 64),
                new ItemStack(Material.LEATHER, 64), new ItemStack(Material.BEEF, 64), new ItemStack(Material.LEATHER, 64)
        }).addDrop(Material.LEATHER, 1).addDrop(Material.BEEF, 1).register(plugin);
        new MobDataCard("羊", MobDataTier.PASSIVE, new ItemStack[] {
                new ItemStack(Material.WHITE_WOOL, 64), new ItemStack(Material.MUTTON, 64), new ItemStack(Material.WHITE_WOOL, 64),
                new ItemStack(Material.COOKED_MUTTON, 64), EMPTY_DATA_CARD, new ItemStack(Material.COOKED_MUTTON, 64),
                new ItemStack(Material.WHITE_WOOL, 64), new ItemStack(Material.MUTTON, 64), new ItemStack(Material.WHITE_WOOL, 64)
        }).addDrop(Material.WHITE_WOOL, 1).addDrop(Material.MUTTON, 1).addDrop(Material.PINK_WOOL, 10000).register(plugin);
        new MobDataCard("蜘蛛", MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(Material.COBWEB, 8), new ItemStack(Material.STRING, 64), new ItemStack(Material.COBWEB, 8),
                new ItemStack(Material.SPIDER_EYE, 32), EMPTY_DATA_CARD, new ItemStack(Material.SPIDER_EYE, 32),
                new ItemStack(Material.COBWEB, 8), new ItemStack(Material.STRING, 64), new ItemStack(Material.COBWEB, 8)
        }).addDrop(Material.STRING, 1).addDrop(Material.SPIDER_EYE, 2).register(plugin);
        new MobDataCard("骷颅", MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.BONE, 64), new ItemStack(Material.LEATHER_HELMET, 1),
                new ItemStack(Material.ARROW, 64), EMPTY_DATA_CARD, new ItemStack(Material.ARROW, 64),
                new ItemStack(Material.BOW, 1), new ItemStack(Material.BONE, 64), new ItemStack(Material.BOW, 1)
        }).addDrop(Material.BONE, 1).addDrop(Material.ARROW, 3).register(plugin);
        new MobDataCard("凋零骷髅", MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(Material.WITHER_SKELETON_SKULL, 8), new ItemStack(Material.BONE, 64), new ItemStack(Material.WITHER_SKELETON_SKULL, 8),
                new ItemStack(Material.COAL_BLOCK, 64), EMPTY_DATA_CARD, new ItemStack(Material.COAL_BLOCK, 64),
                new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.BONE, 64), new ItemStack(Material.STONE_SWORD, 1)
        }).addDrop(Material.COAL, 2, 1).addDrop(Material.BONE, 3).addDrop(Material.WITHER_SKELETON_SKULL, 15).register(plugin);
        new MobDataCard("末影人", MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(Material.ENDER_EYE, 16), new ItemStack(Material.OBSIDIAN, 64), new ItemStack(Material.ENDER_EYE, 16),
                new ItemStack(Material.ENDER_PEARL, 16), EMPTY_DATA_CARD, new ItemStack(Material.ENDER_PEARL, 16),
                new ItemStack(Material.ENDER_EYE, 16), new ItemStack(Material.OBSIDIAN, 64), new ItemStack(Material.ENDER_EYE, 16)
        }).addDrop(Material.ENDER_PEARL, 1).register(plugin);
        new MobDataCard("苦力怕", MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(Material.TNT, 16), new ItemStack(Material.GREEN_DYE, 64), new ItemStack(Material.TNT, 16),
                new ItemStack(Material.GUNPOWDER, 16), EMPTY_DATA_CARD, new ItemStack(Material.GUNPOWDER, 16),
                new ItemStack(Material.TNT, 16), new ItemStack(Material.GREEN_DYE, 64), new ItemStack(Material.TNT, 16)
        }).addDrop(Material.GUNPOWDER, 1).register(plugin);
        new MobDataCard("守卫者", MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(Material.COD, 16), new ItemStack(Material.PRISMARINE_SHARD, 64), new ItemStack(Material.PRISMARINE_CRYSTALS, 64),
                new ItemStack(Material.SPONGE, 4), EMPTY_DATA_CARD, new ItemStack(Material.PUFFERFISH, 4),
                new ItemStack(Material.PRISMARINE_CRYSTALS, 64), new ItemStack(Material.PRISMARINE_SHARD, 64), new ItemStack(Material.COOKED_COD, 16)
        }).addDrop(Material.PRISMARINE_SHARD, 1).addDrop(Material.PRISMARINE_CRYSTALS, 2)
                .addDrop(Material.COD, 3).addDrop(Material.SPONGE, 40).register(plugin);
        new MobDataCard("鸡", MobDataTier.PASSIVE, new ItemStack[] {
                new ItemStack(Material.CHICKEN, 64), new ItemStack(Material.FEATHER, 64), new ItemStack(Material.COOKED_CHICKEN, 64),
                new ItemStack(Material.EGG, 16), EMPTY_DATA_CARD, new ItemStack(Material.EGG, 16),
                new ItemStack(Material.COOKED_CHICKEN, 64), new ItemStack(Material.FEATHER, 64), new ItemStack(Material.CHICKEN, 64)
        }).addDrop(Material.CHICKEN, 1).addDrop(Material.FEATHER, 2).register(plugin);
        new MobDataCard("铁傀儡", MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(Material.IRON_BLOCK, 64), new ItemStack(Material.PUMPKIN, 16), new ItemStack(Material.IRON_BLOCK, 64),
                new ItemStack(Material.POPPY, 16), EMPTY_DATA_CARD, new ItemStack(Material.POPPY, 16),
                new ItemStack(Material.IRON_BLOCK, 64), new ItemStack(Material.PUMPKIN, 16), new ItemStack(Material.IRON_BLOCK, 64)
        }).addDrop(Material.IRON_INGOT, 2, 1).addDrop(Material.POPPY, 3).addDrop(SlimefunItems.BASIC_CIRCUIT_BOARD, 3).register(plugin);
        new MobDataCard("烈焰人", MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(Material.MAGMA_BLOCK, 64), new ItemStack(Material.BLAZE_ROD, 64), new ItemStack(Material.MAGMA_BLOCK, 64),
                new ItemStack(Material.BLAZE_ROD, 64), EMPTY_DATA_CARD, new ItemStack(Material.BLAZE_ROD, 64),
                new ItemStack(Material.MAGMA_BLOCK, 64), new ItemStack(Material.BLAZE_ROD, 64), new ItemStack(Material.MAGMA_BLOCK, 64)
        }).addDrop(Material.BLAZE_ROD, 1).register(plugin);
        new MobDataCard("凋零", MobDataTier.MINI_BOSS, new ItemStack[] {
                new ItemStack(Material.WITHER_SKELETON_SKULL, 64), new ItemStack(Material.WITHER_SKELETON_SKULL, 64), new ItemStack(Material.WITHER_SKELETON_SKULL, 64),
                new SlimefunItemStack(SlimefunItems.WITHER_PROOF_OBSIDIAN, 64), EMPTY_DATA_CARD, new SlimefunItemStack(SlimefunItems.WITHER_PROOF_OBSIDIAN, 64),
                new SlimefunItemStack(Materials.VOID_INGOT, 4), new SlimefunItemStack(SlimefunItems.WITHER_ASSEMBLER, 4), new SlimefunItemStack(Materials.VOID_INGOT, 4)
        }).addDrop(Material.NETHER_STAR, 1).addDrop(SlimefunItems.COMPRESSED_CARBON, 8, 2).register(plugin);
        new MobDataCard("末影龙", MobDataTier.BOSS, new ItemStack[] {
                new ItemStack(Material.END_CRYSTAL, 64), new SlimefunItemStack(Materials.VOID_INGOT, 32), new ItemStack(Material.CHORUS_FLOWER, 64),
                SlimefunItems.INFUSED_ELYTRA, EMPTY_DATA_CARD, new ItemStack(Material.DRAGON_HEAD, 1),
                new SlimefunItemStack(SlimefunItems.ENDER_LUMP_3, 64), new SlimefunItemStack(Materials.VOID_INGOT, 32), new ItemStack(Material.DRAGON_BREATH, 64)
        }).addDrop(Materials.VOID_DUST, 1).addDrop(Materials.ENDER_ESSENCE, 4).addDrop(Material.DRAGON_EGG, 1_000_000).register(plugin);
        new MobDataCard("蜜蜂", MobDataTier.NEUTRAL, new ItemStack[] {
                new ItemStack(Material.HONEYCOMB_BLOCK, 16), new ItemStack(Material.HONEY_BLOCK, 16), new ItemStack(Material.HONEYCOMB_BLOCK, 16),
                new ItemStack(Material.HONEY_BLOCK, 16), EMPTY_DATA_CARD, new ItemStack(Material.HONEY_BLOCK, 16),
                new ItemStack(Material.HONEYCOMB_BLOCK, 16), new ItemStack(Material.HONEY_BLOCK, 16), new ItemStack(Material.HONEYCOMB_BLOCK, 16)
        }).addDrop(Material.HONEYCOMB, 1).register(plugin);
        new MobDataCard("村民", MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(Material.EMERALD, 64), new ItemStack(Material.POTATO, 64), new ItemStack(Material.EMERALD, 64),
                new ItemStack(Material.CARROT, 64), EMPTY_DATA_CARD, new ItemStack(Material.WHEAT, 64),
                new ItemStack(Material.EMERALD, 64), new ItemStack(Material.PUMPKIN, 64), new ItemStack(Material.EMERALD, 64)
        }).addDrop(Material.EMERALD, 1).register(plugin);
        new MobDataCard("女巫", MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(Material.REDSTONE_BLOCK, 64), new ItemStack(Material.GLASS, 64), new ItemStack(Material.SUGAR, 64),
                new ItemStack(Material.GLOWSTONE, 64), EMPTY_DATA_CARD, new ItemStack(Material.GLOWSTONE, 64),
                new ItemStack(Material.SUGAR, 64), new ItemStack(Material.GLASS, 64), new ItemStack(Material.REDSTONE_BLOCK, 64)
        }).addDrop(Material.SUGAR, 1).addDrop(Material.REDSTONE, 1)
                .addDrop(Material.GLASS_BOTTLE, 1).addDrop(Material.GLOWSTONE_DUST, 1).register(plugin);
    }

}
