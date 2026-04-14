package io.github.ryuPC0.cjptt.speedSign.simple;

import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import net.minecraft.world.level.block.Block;

public class SpeedSignBlockItem extends TrackTargetingBlockItem
{
    public static <T extends Block> NonNullBiFunction<? super T, Properties, TrackTargetingBlockItem> ofType(
            EdgePointType<?> type) {
        return (b, p) -> new SpeedSignBlockItem(b, p, type);
    }
    public SpeedSignBlockItem(Block pBlock, Properties pProperties, EdgePointType<?> type) {
        super(pBlock, pProperties, type);
    }

}
