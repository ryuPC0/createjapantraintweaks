package io.github.ryuPC0.cjptt.speedSign.advanced;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class AdvancedSpeedSignStandingBlock extends AdvancedSpeedSignBlock{

    public static IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public AdvancedSpeedSignStandingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 8).setValue(WATERLOGGED, false));
    }

    @Override
    public float getYRotationDegrees(final BlockState state) {
        return RotationSegment.convertToDegrees(state.getValue(ROTATION));
    }
}
