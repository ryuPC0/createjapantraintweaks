package io.github.ryuPC0.cjptt.registry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntity;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntityRenderer;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSignBlockEntity;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSignBlockRenderer;

import static io.github.ryuPC0.cjptt.Cjptt.REGISTRATE;
import static io.github.ryuPC0.cjptt.registry.CjpttBlocks.*;

public class CjpttBlockEntities {
    public static final BlockEntityEntry<SpeedSignBlockEntity> SPEEDSIGN_BLOCKENTITY = REGISTRATE.get().blockEntity("simplespeedsign",SpeedSignBlockEntity::new)/*.visual(() -> SpeedSignBlockRenderer::new)*/.renderer(() -> SpeedSignBlockRenderer::new).validBlock(SPEEDSIGN_BLOCK).register();

    public static final BlockEntityEntry<AdvancedSpeedSignBlockEntity> ADVANCEDSPEEDSIGN_BLOCKENTITY = REGISTRATE.get().blockEntity("advancedspeedsign",AdvancedSpeedSignBlockEntity::new).renderer(()-> AdvancedSpeedSignBlockEntityRenderer::new).validBlock(ADVANCED_SPEEDSIGN_BLOCK).register();
    public static void register() {
    }
}
