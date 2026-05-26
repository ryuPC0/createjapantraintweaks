package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.jetbrains.annotations.NotNull;

import static io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlock.ROTATION;
import static io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlock.WATERLOGGED;
import static net.minecraft.world.level.material.Fluids.WATER;

public class AdvancedSpeedSignItem extends TrackTargetingBlockItem {
    private final Direction attachmentDirection;

    public static <T extends Block> NonNullBiFunction<? super T, Properties, TrackTargetingBlockItem> ofType(
            EdgePointType<?> type) {
        return (b, p) -> new AdvancedSpeedSignItem(b, p, type);
    }
    protected boolean canPlace(final LevelReader level, final BlockState possibleState, final BlockPos pos) {
        return possibleState.canSurvive(level, pos);
    }
    public AdvancedSpeedSignItem(Block pBlock,Properties pProperties, EdgePointType<?> type) {
        super(pBlock, pProperties, type);
        this.attachmentDirection = Direction.DOWN;
    }
    @Override
    protected BlockState getPlacementState(@NotNull BlockPlaceContext pContext) {
        return getBlock().getStateForPlacement(pContext);
    }
}
