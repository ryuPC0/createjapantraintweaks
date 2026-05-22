package io.github.ryuPC0.cjptt.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegistryObject;

import static io.github.ryuPC0.cjptt.Cjptt.CREATIVE_MODE_TABS;
import static io.github.ryuPC0.cjptt.registry.CjpttBlocks.SPEEDSIGN_BLOCK;

public class CjpttCreativeModeTab {
    public static final RegistryObject<CreativeModeTab> CJPTTTAB = CREATIVE_MODE_TABS.register("createjapantraintweaks", () -> CreativeModeTab.builder().displayItems((parameters, output) -> {
        output.accept(SPEEDSIGN_BLOCK.get()); output.accept(CjpttBlocks.EXTENDED_TRAIN_CONTROLS); output.accept(CjpttBlocks.ADVANCED_SPEEDSIGN_BLOCK);
    }).build());
    public static void register() {
    }
}
