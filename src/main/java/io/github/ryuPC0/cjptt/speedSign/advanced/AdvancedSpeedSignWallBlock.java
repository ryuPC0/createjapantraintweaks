package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class AdvancedSpeedSignWallBlock extends AdvancedSpeedSignBlock{

    public static final DirectionProperty FACING;
    protected static final float AABB_THICKNESS = 2.0F;
    protected static final float AABB_BOTTOM = 4.5F;
    protected static final float AABB_TOP = 12.5F;
    private static final Map<Direction, VoxelShape> AABBS;
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0F, 4.5F, 14.0F, 16.0F, 12.5F, 16.0F), Direction.SOUTH, Block.box(0.0F, 4.5F, 0.0F, 16.0F, 12.5F, 2.0F), Direction.EAST, Block.box(0.0F, 4.5F, 0.0F, 2.0F, 12.5F, 16.0F), Direction.WEST, Block.box(14.0F, 4.5F, 0.0F, 16.0F, 12.5F, 16.0F)));
    }

    public AdvancedSpeedSignWallBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public float getYRotationDegrees(BlockState state) {
        return state.getValue(FACING).toYRot();
    }
}
