package io.github.mooy1.infinityexpansion.implementation.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Categories;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.persistence.PersistenceUtils;
import io.github.mooy1.infinitylib.slimefun.abstracts.AbstractContainer;
import io.github.mooy1.infinitylib.slimefun.presets.MenuPreset;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.collections.Pair;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

/**
 * A block that stored large amounts of 1 item
 *
 * @author Mooy1
 *
 * Thanks to FluffyBear for stuff to learn from
 */
public final class StorageUnit extends AbstractContainer {

    /* Namespaced keys */
    static final NamespacedKey EMPTY_KEY = InfinityExpansion.inst().getKey("empty"); // key for empty item
    static final NamespacedKey DISPLAY_KEY = InfinityExpansion.inst().getKey("display"); // key for display item
    private static final NamespacedKey OLD_ITEM_KEY = InfinityExpansion.inst().getKey("stored_item"); // old item key in pdc
    private static final NamespacedKey ITEM_KEY = InfinityExpansion.inst().getKey("item"); // item key for item pdc
    private static final NamespacedKey AMOUNT_KEY = InfinityExpansion.inst().getKey("stored"); // amount key for item pdc

    /* Menu slots */
    static final int INPUT_SLOT = MenuPreset.slot1;
    static final int DISPLAY_SLOT = MenuPreset.slot2;
    static final int STATUS_SLOT = DISPLAY_SLOT - 9;
    static final int OUTPUT_SLOT = MenuPreset.slot3;
    static final int INTERACT_SLOT = DISPLAY_SLOT + 9;

    /* Menu items */
    private static final ItemStack INTERACTION_ITEM = new CustomItem(Material.LIME_STAINED_GLASS_PANE,
            "&a快速拿出",
            "&b左键: &7取出1个物品",
            "&b右键: &7取出1组物品",
            "&b蹲下左键: &7放入全部",
            "&b蹲下右键: &7取出全部"
    );
    private static final ItemStack LOADING_ITEM = new CustomItem(Material.CYAN_STAINED_GLASS_PANE,
            "&b组",
            "&7在弄了在弄了..."
    );

    /* Instance constants */
    private final Map<DirtyChestMenu, StorageCache> caches = new HashMap<>();
    final int max;

    public StorageUnit(SlimefunItemStack item, int max, ItemStack[] recipe) {
        super(Categories.STORAGE, item, StorageForge.TYPE, recipe);
        this.max = max;

        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return true;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                StorageCache cache = StorageUnit.this.caches.get(BlockStorage.getInventory(b));
                // TODO remove null check
                if (cache != null) {
                    cache.tick(b);
                }
            }
        });
    }

    @Override
    protected void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        this.caches.put(menu, new StorageCache(this, menu));
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu, @Nonnull Location l) {
        StorageCache cache = this.caches.remove(menu);
        if (cache != null) {
            cache.destroy(l, e);
        }
        menu.dropItems(l, INPUT_SLOT, OUTPUT_SLOT);
    }

    @Override
    protected void onPlace(@Nonnull BlockPlaceEvent e, @Nonnull Block b) {
        Pair<ItemStack, Integer> data = loadFromStack(e.getItemInHand().getItemMeta());
        if (data != null) {
            InfinityExpansion.inst().runSync(() -> {
                StorageCache cache = this.caches.get(BlockStorage.getInventory(b));
                cache.load(data.getFirstValue(), data.getFirstValue().getItemMeta());
                cache.setAmount(data.getSecondValue());
            });
        }
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        MenuPreset.setupBasicMenu(blockMenuPreset);
        blockMenuPreset.addMenuClickHandler(DISPLAY_SLOT, ChestMenuUtils.getEmptyClickHandler());
        blockMenuPreset.addItem(INTERACT_SLOT, INTERACTION_ITEM);
        blockMenuPreset.addItem(STATUS_SLOT, LOADING_ITEM);
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu dirtyChestMenu, @Nonnull ItemTransportFlow flow, @Nonnull ItemStack itemStack) {
        StorageCache cache = this.caches.get(dirtyChestMenu);
        // TODO remove null check
        if (cache != null) {
            if (flow == ItemTransportFlow.WITHDRAW) {
                return new int[] {OUTPUT_SLOT};
            } else if (flow == ItemTransportFlow.INSERT && (cache.isEmpty() || cache.matches(itemStack))) {
                cache.input();
                return new int[] {INPUT_SLOT};
            }
        }
        return new int[0];
    }

    public void reloadCache(Block b) {
        this.caches.get(BlockStorage.getInventory(b.getLocation())).reloadData();
    }

    static void transferToStack(@Nonnull ItemStack source, @Nonnull ItemStack target) {
        Pair<ItemStack, Integer> data = loadFromStack(source.getItemMeta());
        if (data != null) {
            target.setItemMeta(saveToStack(target.getItemMeta(), data.getFirstValue(),
                    StackUtils.getDisplayName(data.getFirstValue()), data.getSecondValue()));
        }
    }

    static ItemMeta saveToStack(ItemMeta meta, ItemStack displayItem, String displayName, int amount) {
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            lore.add(ChatColor.GOLD + "Stored: " + displayName + ChatColor.YELLOW + " x " + amount);
            meta.setLore(lore);
        }
        meta.getPersistentDataContainer().set(ITEM_KEY, PersistenceUtils.ITEM_STACK, displayItem);
        meta.getPersistentDataContainer().set(AMOUNT_KEY, PersistentDataType.INTEGER, amount);
        return meta;
    }

    private static Pair<ItemStack, Integer> loadFromStack(ItemMeta meta) {
        // get amount
        Integer amount = meta.getPersistentDataContainer().get(AMOUNT_KEY, PersistentDataType.INTEGER);
        if (amount != null) {

            // check for old id
            String oldID = meta.getPersistentDataContainer().get(OLD_ITEM_KEY, PersistentDataType.STRING);
            if (oldID != null) {
                ItemStack item = StackUtils.getItemByIDorType(oldID);
                if (item != null) {
                    // add the display key to it
                    ItemMeta update = item.getItemMeta();
                    update.getPersistentDataContainer().set(DISPLAY_KEY, PersistentDataType.BYTE, (byte) 1);
                    item.setItemMeta(update);
                    return new Pair<>(item, amount);
                }
            }

            // get item
            ItemStack item = meta.getPersistentDataContainer().get(ITEM_KEY, PersistenceUtils.ITEM_STACK);
            if (item != null) {
                return new Pair<>(item, amount);
            }
        }
        return null;
    }

}
