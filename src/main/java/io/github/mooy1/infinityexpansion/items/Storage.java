package io.github.mooy1.infinityexpansion.items;

import lombok.experimental.UtilityClass;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinityexpansion.items.storage.StorageForge;
import io.github.mooy1.infinityexpansion.items.storage.StorageUnit;
import io.github.mooy1.infinitylib.presets.LorePreset;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

@UtilityClass
public final class Storage {
    
    public static final SlimefunItemStack STORAGE_FORGE = new SlimefunItemStack(
            "STORAGE_FORGE",
            Material.BEEHIVE,
            "&6储存单元制造机",
            "&7提高储存单元等级",
            "&7或者物品"
    );

    private static final int BASIC_AMOUNT = 6400;
    private static final int ADVANCED_AMOUNT = 25600;
    private static final int REINFORCED_AMOUNT = 102400;
    private static final int VOID_AMOUNT = 409600;
    private static final int INFINITY_AMOUNT = 1_600_000_000;

    public static final SlimefunItemStack BASIC_STORAGE = new SlimefunItemStack(
            "BASIC_STORAGE",
            Material.OAK_WOOD,
            "&9基础&8储存单元",
            "&6可储存: &e" + LorePreset.format(BASIC_AMOUNT) + " &e物品"
    );
    public static final SlimefunItemStack ADVANCED_STORAGE = new SlimefunItemStack(
            "ADVANCED_STORAGE",
            Material.DARK_OAK_WOOD,
            "&c高级&8储存单元",
            "&6可储存: &e" + LorePreset.format(ADVANCED_AMOUNT) + " &e物品"
    );
    public static final SlimefunItemStack REINFORCED_STORAGE = new SlimefunItemStack(
            "REINFORCED_STORAGE",
            Material.ACACIA_WOOD,
            "&f加强&8储存单元",
            "&6可储存: &e" + LorePreset.format(REINFORCED_AMOUNT) + " &e物品"
    );
    public static final SlimefunItemStack VOID_STORAGE = new SlimefunItemStack(
            "VOID_STORAGE",
            Material.CRIMSON_HYPHAE,
            "&8虚空&8储存单元",
            "&6可储存: &e" + LorePreset.format(VOID_AMOUNT) + " &e物品"
    );
    public static final SlimefunItemStack INFINITY_STORAGE = new SlimefunItemStack(
            "INFINITY_STORAGE",
            Material.WARPED_HYPHAE,
            "&b无尽&8储存单元",
            "&6可储存: &e" + LorePreset.format(INFINITY_AMOUNT) + " &e物品"
    );
    
    public static void setup(InfinityExpansion plugin) {
        new StorageForge(Categories.STORAGE, STORAGE_FORGE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                Materials.MAGSTEEL, new ItemStack(Material.ANVIL), Materials.MAGSTEEL,
                Materials.MAGSTEEL, new ItemStack(Material.CRAFTING_TABLE), Materials.MAGSTEEL,
                Materials.MAGSTEEL, new ItemStack(Material.BARREL), Materials.MAGSTEEL,
        }).register(plugin);
        new StorageUnit(BASIC_STORAGE, BASIC_AMOUNT,  new ItemStack[] {
                new ItemStack(Material.OAK_LOG), Materials.MAGSTEEL, new ItemStack(Material.OAK_LOG),
                new ItemStack(Material.OAK_LOG), new ItemStack(Material.BARREL), new ItemStack(Material.OAK_LOG),
                new ItemStack(Material.OAK_LOG), Materials.MAGSTEEL, new ItemStack(Material.OAK_LOG)
        }).register(plugin);
        new StorageUnit(ADVANCED_STORAGE, ADVANCED_AMOUNT,  new ItemStack[] {
                Materials.MAGSTEEL, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL,
                Materials.MAGSTEEL, BASIC_STORAGE, Materials.MAGSTEEL,
                Materials.MAGSTEEL, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL
        }).register(plugin);
        new StorageUnit(REINFORCED_STORAGE, REINFORCED_AMOUNT,  new ItemStack[] {
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_CIRCUIT, Materials.MAGSTEEL_PLATE,
                Materials.MAGSTEEL_PLATE, ADVANCED_STORAGE, Materials.MAGSTEEL_PLATE,
                Materials.MAGSTEEL_PLATE, Materials.MACHINE_PLATE, Materials.MAGSTEEL_PLATE
        }).register(plugin);
        new StorageUnit(VOID_STORAGE, VOID_AMOUNT,  new ItemStack[] {
                Materials.VOID_INGOT, Materials.MACHINE_PLATE, Materials.VOID_INGOT,
                Materials.MAGNONIUM, REINFORCED_STORAGE, Materials.MAGNONIUM,
                Materials.VOID_INGOT, Materials.MACHINE_CORE, Materials.VOID_INGOT
        }).register(plugin);
        new StorageUnit(INFINITY_STORAGE, INFINITY_AMOUNT, new ItemStack[] {
                Materials.INFINITE_INGOT, Materials.VOID_INGOT, Materials.INFINITE_INGOT,
                Materials.INFINITE_INGOT, VOID_STORAGE, Materials.INFINITE_INGOT,
                Materials.INFINITE_INGOT, Materials.VOID_INGOT, Materials.INFINITE_INGOT
        }).register(plugin);
    }
    
}
