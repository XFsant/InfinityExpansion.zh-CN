package io.github.mooy1.infinityexpansion;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.commands.GiveRecipe;
import io.github.mooy1.infinityexpansion.commands.PrintItem;
import io.github.mooy1.infinityexpansion.commands.SetData;
import io.github.mooy1.infinityexpansion.items.Blocks;
import io.github.mooy1.infinityexpansion.items.Gear;
import io.github.mooy1.infinityexpansion.items.Generators;
import io.github.mooy1.infinityexpansion.items.Machines;
import io.github.mooy1.infinityexpansion.items.Materials;
import io.github.mooy1.infinityexpansion.items.MobData;
import io.github.mooy1.infinityexpansion.items.Quarries;
import io.github.mooy1.infinityexpansion.items.Researches;
import io.github.mooy1.infinityexpansion.items.SlimefunExtension;
import io.github.mooy1.infinityexpansion.items.Storage;
import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.metrics.bukkit.Metrics;

public final class InfinityExpansion extends AbstractAddon {

    public InfinityExpansion() {
        super("Mooy1", "InfinityExpansion", "master", "auto-update");
    }

    @Override
    protected void enable() {
        new Metrics(this, 8991);

        Plugin lx = getServer().getPluginManager().getPlugin("LiteXpansion");
        if (lx != null && lx.getConfig().getBoolean("options.nerf-other-addons")) {
            Scheduler.run(() -> log(Level.WARNING,
                    "########################################################",
                    "      LiteXpansion附属减少了某些物品的能量生产.",
                    "你可以在LiteXpansion附属中的对应名称文件夹内里的config.",
                    "来设置'options:' add 'nerf-other-addons: false'",
                    "########################################################"
            ));
        }

        getAddonCommand().addSub(new GiveRecipe()).addSub(new SetData()).addSub(new PrintItem());

        Groups.setup(this);
        MobData.setup(this);
        Materials.setup(this);
        Machines.setup(this);
        Quarries.setup(this);
        Gear.setup(this);
        Blocks.setup(this);
        Storage.setup(this);
        Generators.setup(this);
        SlimefunExtension.setup(this);

        if (getConfig().getBoolean("balance-options.enable-researches")) {
            Researches.setup();
        }
    }

    @Override
    public void disable() {

    }

}
