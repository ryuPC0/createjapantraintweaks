package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.registry.CjpttBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AdvancedSpeedSignBlock extends Block implements IBE<AdvancedSpeedSignBlockEntity>, IWrenchable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final float AABB_THICKNESS = 2.0F;
    protected static final float AABB_BOTTOM = 4.5F;
    protected static final float AABB_TOP = 12.5F;
    protected static final float AABB_OFFSET = 4.0F;
    protected static final VoxelShape SHAPE = Block.box(4.0F, 0.0F, 4.0F, 12.0F, 16.0F, 12.0F);
    private static final Map<Direction, VoxelShape> AABBS;
    static {
        AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0F, 4.5F, 14.0F, 16.0F, 12.5F, 16.0F),
                Direction.SOUTH, Block.box(0.0F, 4.5F, 0.0F, 16.0F, 12.5F, 2.0F),
                Direction.EAST, Block.box(0.0F, 4.5F, 0.0F, 2.0F, 12.5F, 16.0F),
                Direction.WEST, Block.box(14.0F, 4.5F, 0.0F, 16.0F, 12.5F, 16.0F)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder){
        pBuilder.add(WATERLOGGED,ROTATION,FACING);
    }
    public AdvancedSpeedSignBlock(Properties properties)
    {
        super(properties);
    }

    public @NotNull BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public boolean isPossibleToRespawnInThis(BlockState pState) {
        return true;
    }

    public float getYRotationDegrees(final BlockState state) {
        return RotationSegment.convertToDegrees(state.getValue(ROTATION));
    }

    public Vec3 getSignHitboxCenterPosition(BlockState pState) {
        return new Vec3(0.5F, 0.5F, 0.5F);
    }

    public @NotNull FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public Class<AdvancedSpeedSignBlockEntity> getBlockEntityClass() {
        return AdvancedSpeedSignBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AdvancedSpeedSignBlockEntity> getBlockEntityType() {
        return CjpttBlockEntities.ADVANCEDSPEEDSIGN_BLOCKENTITY.get();
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

}
