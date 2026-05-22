package io.github.ryuPC0.cjptt.speedSign.simple;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.registry.CjpttBlockEntities;
import net.minecraft.world.level.block.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SpeedSignBlock extends Block implements IBE<SpeedSignBlockEntity>, IWrenchable {
    public SpeedSignBlock(Properties properties)
    {
        super(properties);
    }


    @Override
    public Class<SpeedSignBlockEntity> getBlockEntityClass() {
        return SpeedSignBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SpeedSignBlockEntity> getBlockEntityType() {
        return CjpttBlockEntities.SPEEDSIGN_BLOCKENTITY.get();
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }
}
