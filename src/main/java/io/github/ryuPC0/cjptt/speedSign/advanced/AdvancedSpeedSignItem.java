package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import io.github.ryuPC0.cjptt.registry.CjpttBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.Map;

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
    protected BlockState getPlacementState(BlockPlaceContext pContext) {
        BlockState blockstate = null;
        BlockState blockstate1 = null;
        LevelReader levelreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();

        for(Direction direction : pContext.getNearestLookingDirections()) {
            if (direction != this.attachmentDirection.getOpposite()) {
                BlockState blockstate2 = direction == this.attachmentDirection ? this.getBlock().getStateForPlacement(pContext) : blockstate;
                if (blockstate2 != null && this.canPlace(levelreader, blockstate2, blockpos)) {
                    blockstate1 = blockstate2;
                    break;
                }
            }
        }

        return blockstate1 != null && levelreader.isUnobstructed(blockstate1, blockpos, CollisionContext.empty()) ? blockstate1 : null;
    }
}
