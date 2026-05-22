package io.github.ryuPC0.cjptt.registry;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsBlock;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsInteractionBehaviour;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsMovementBehaviour;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlock;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignItem;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSignBlock;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSignBlockItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;

import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static io.github.ryuPC0.cjptt.Cjptt.REGISTRATE;
import static io.github.ryuPC0.cjptt.registry.CjpttEdgePointType.ADVANCED_SPEEDSIGN;
import static io.github.ryuPC0.cjptt.registry.CjpttEdgePointType.SPEEDSIGN;

@SuppressWarnings("removal")
public class CjpttBlocks {
    public static final BlockEntry<ExtendedControlsBlock> EXTENDED_TRAIN_CONTROLS = REGISTRATE.get().block("extendedcontrols", ExtendedControlsBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_BROWN)
                    .sound(SoundType.NETHERITE_BLOCK))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.horizontalBlock(c.get(),
                    s -> AssetLookup.partialBaseModel(c, p,
                            s.getValue(ExtendedControlsBlock.VIRTUAL) ? "virtual" : s.getValue(ExtendedControlsBlock.OPEN) ? "open" : "closed")))
            .onRegister(movementBehaviour(new ExtendedControlsMovementBehaviour()))
            .onRegister(interactionBehaviour(new ExtendedControlsInteractionBehaviour()))
            .lang("Extended Train Controls")
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<SpeedSignBlock> SPEEDSIGN_BLOCK = REGISTRATE.get().block("simplespeedsign",SpeedSignBlock::new).item(SpeedSignBlockItem.ofType(SPEEDSIGN)).transform(customItemModel()).register();
    public static final BlockEntry<AdvancedSpeedSignBlock> ADVANCED_SPEEDSIGN_BLOCK = REGISTRATE.get().block("advancedspeedsignblock", AdvancedSpeedSignBlock::new)
            .blockstate((c,p) -> p.getVariantBuilder(c.getEntry()).forAllStatesExcept((state) ->{

                Direction dir = state.getValue(AdvancedSpeedSignBlock.FACING);
                return ConfiguredModel.builder().build();
            },AdvancedSpeedSignBlock.WATERLOGGED,AdvancedSpeedSignBlock.FACING,AdvancedSpeedSignBlock.ROTATION)).item(AdvancedSpeedSignItem.ofType(ADVANCED_SPEEDSIGN)).transform(customItemModel()).register();

    public static void register() {
    }
}
