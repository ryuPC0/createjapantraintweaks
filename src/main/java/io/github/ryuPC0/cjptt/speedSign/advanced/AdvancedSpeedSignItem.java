package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
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
    protected Block wallBlock;
    private final Direction attachmentDirection;

    public static <T extends Block> NonNullBiFunction<? super T, Properties, TrackTargetingBlockItem> ofType(
            EdgePointType<?> type,Block pwallblock) {
        return (b, p) -> new AdvancedSpeedSignItem(b, pwallblock,p, type);
    }
    protected boolean canPlace(final LevelReader level, final BlockState possibleState, final BlockPos pos) {
        return possibleState.canSurvive(level, pos);
    }
    public AdvancedSpeedSignItem(Block pBlock,Block pWallBlock, Properties pProperties, EdgePointType<?> type) {
        super(pBlock, pProperties, type);
        wallBlock = pWallBlock;
        this.attachmentDirection = Direction.DOWN;
    }
    @Override
    protected BlockState getPlacementState(BlockPlaceContext pContext) {
        BlockState blockstate = this.wallBlock.getStateForPlacement(pContext);
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

    @Override
    public void registerBlocks(final Map<Block, Item> map, final Item item) {
        super.registerBlocks(map, item);
        map.put(this.wallBlock, item);
    }
}
