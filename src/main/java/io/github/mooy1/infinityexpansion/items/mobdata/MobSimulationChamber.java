package io.github.mooy1.infinityexpansion.items.mobdata;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.utils.Util;
import io.github.mooy1.infinitylib.items.StackUtils;
import io.github.mooy1.infinitylib.presets.LorePreset;
import io.github.mooy1.infinitylib.presets.MenuPreset;
import io.github.mooy1.infinitylib.slimefun.AbstractTickingContainer;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;

public final class MobSimulationChamber extends AbstractTickingContainer implements EnergyNetComponent {

    static final double XP_MULTIPLIER = InfinityExpansion.inst().getConfig().getDouble("mob-simulation-options.xp-multiplier", 0, 1000);

    private static final ItemStack NO_CARD = new CustomItem(Material.BARRIER, "&c需要空置的模拟器!");
    private static final int CARD_SLOT = MenuPreset.INPUT + 27;
    private static final int STATUS_SLOT = MenuPreset.INPUT;
    private static final int[] OUTPUT_SLOTS = Util.LARGE_OUTPUT;
    private static final int XP_SLOT = 46;

    private final int energy;
    private final int interval;

    public MobSimulationChamber(Category category, SlimefunItemStack item, RecipeType type, ItemStack[] recipe, int energy, int interval) {
        super(category, item, type, recipe);
        this.energy = energy;
        this.interval = interval;
    }

    @Override
    protected void onBreak(@Nonnull BlockBreakEvent e, @Nonnull BlockMenu menu, @Nonnull Location l) {

        menu.dropItems(l, OUTPUT_SLOTS);
        menu.dropItems(l, CARD_SLOT);

        e.getPlayer().giveExp(Util.getIntData("xp", l));
        BlockStorage.addBlockInfo(l, "xp", "0");
    }

    @Nonnull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return this.energy + Math.max(MobDataTier.BOSS.energy, this.energy * 9);
    }

    @Override
    protected void setupMenu(@Nonnull BlockMenuPreset blockMenuPreset) {
        for (int i : Util.LARGE_OUTPUT_BORDER) {
            blockMenuPreset.addItem(i, MenuPreset.OUTPUT_ITEM, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : MenuPreset.INPUT_BORDER) {
            blockMenuPreset.addItem(i, MenuPreset.STATUS_ITEM, ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : MenuPreset.INPUT_BORDER) {
            blockMenuPreset.addItem(i + 27, MenuPreset.INPUT_ITEM, ChestMenuUtils.getEmptyClickHandler());
        }
        blockMenuPreset.addItem(STATUS_SLOT, MenuPreset.LOADING, ChestMenuUtils.getEmptyClickHandler());
        blockMenuPreset.addItem(XP_SLOT, MenuPreset.LOADING, ChestMenuUtils.getEmptyClickHandler());
    }

    @Nonnull
    @Override
    protected int[] getTransportSlots(@Nonnull DirtyChestMenu menu, @Nonnull ItemTransportFlow flow, ItemStack item) {
        if (flow == ItemTransportFlow.WITHDRAW) {
            return OUTPUT_SLOTS;
        }
        return new int[0];
    }

    @Override
    public void onNewInstance(@Nonnull BlockMenu menu, @Nonnull Block b) {
        Location l = b.getLocation();
        if (BlockStorage.getLocationInfo(l, "xp") == null) {
            BlockStorage.addBlockInfo(l, "xp", "O");
        }
        menu.replaceExistingItem(XP_SLOT, makeXpItem(0));
        menu.addMenuClickHandler(XP_SLOT, (p, slot, item, action) -> {
            int xp = Util.getIntData("xp", l);
            if (xp > 0) {
                p.giveExp(xp);
                p.playSound(l, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                BlockStorage.addBlockInfo(l, "xp", "O");
                menu.replaceExistingItem(XP_SLOT, makeXpItem(0));
            }
            return false;
        });
    }

    private static ItemStack makeXpItem(int stored) {
        return new CustomItem(Material.LIME_STAINED_GLASS_PANE, "&a模拟值: " + stored, "", "&a> 点击取消");
    }

    @Override
    protected void tick(@Nonnull BlockMenu inv, @Nonnull Block b) {
        ItemStack input = inv.getItemInSlot(CARD_SLOT);

        if (input == null) {
            return;
        }

        MobDataCard card = MobDataCard.CARDS.get(StackUtils.getID(input));

        if (card == null) {
            if (inv.hasViewer()) {
                inv.replaceExistingItem(STATUS_SLOT, NO_CARD);
            }
            return;
        }

        int energy = card.tier.energy + this.energy;

        if (getCharge(b.getLocation()) < energy) {
            if (inv.hasViewer()) {
                inv.replaceExistingItem(STATUS_SLOT, MenuPreset.NO_ENERGY);
            }
            return;
        }

        removeCharge(b.getLocation(), energy);

        int xp = Util.getIntData("xp", b.getLocation());

        if (inv.hasViewer()) {
            inv.replaceExistingItem(STATUS_SLOT, new CustomItem(Material.LIME_STAINED_GLASS_PANE,
                    "&a正在模拟... (" + LorePreset.formatEnergy(energy) + " J/s)")
            );
            inv.replaceExistingItem(XP_SLOT, makeXpItem(xp));
        }

        if (InfinityExpansion.inst().getGlobalTick() % this.interval != 0) return;

        BlockStorage.addBlockInfo(b.getLocation(), "xp", String.valueOf(xp + card.tier.xp));

        ItemStack item = card.drops.getRandom();
        if (inv.fits(item, OUTPUT_SLOTS)) {
            inv.pushItem(item.clone(), OUTPUT_SLOTS);
        } else if (inv.hasViewer()) {
            inv.replaceExistingItem(STATUS_SLOT, MenuPreset.NO_ROOM);
        }
    }

}
