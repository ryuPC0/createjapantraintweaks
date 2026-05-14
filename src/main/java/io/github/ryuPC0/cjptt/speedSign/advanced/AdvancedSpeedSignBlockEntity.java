package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.api.contraption.transformable.TransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSign;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class AdvancedSpeedSignBlockEntity extends SmartBlockEntity implements TransformableBlockEntity {
    public TrackTargetingBehaviour<AdvancedSpeedSign> edgePoint;
    public AdvancedSpeedSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void transform(BlockEntity blockEntity, StructureTransform structureTransform) {
        edgePoint.transform(blockEntity,structureTransform);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {

    }
}
